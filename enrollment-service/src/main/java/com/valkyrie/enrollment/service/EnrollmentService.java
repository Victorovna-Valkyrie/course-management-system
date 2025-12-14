package com.valkyrie.enrollment.service;

import com.valkyrie.enrollment.client.CatalogClient;
import com.valkyrie.enrollment.client.UserClient;
import com.valkyrie.enrollment.model.Enrollment;
import com.valkyrie.enrollment.model.EnrollmentStatus;
import com.valkyrie.enrollment.model.Student;
import com.valkyrie.enrollment.repository.EnrollmentRepository;
import com.valkyrie.enrollment.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EnrollmentService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserClient userClient;
    
    @Autowired
    private CatalogClient catalogClient;
    
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }
    
    public Optional<Enrollment> findById(String id) {
        return enrollmentRepository.findById(id);
    }
    
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }
    
    public void deleteById(String id) {
        enrollmentRepository.deleteById(id);
    }
    
    public List<Enrollment> findByCourseId(String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
    
    public List<Enrollment> findByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
    
    @Transactional
    public Enrollment enroll(String courseId, String studentId) {
        // 1. 验证学生是否存在（使用Feign客户端）
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
        
        // 2. 调用课程目录服务验证课程是否存在（使用Feign客户端）
        Map<String, Object> courseResponse;
        try {
            courseResponse = catalogClient.getCourse(courseId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve course: " + e.getMessage());
        }
        
        // 3. 从响应中提取课程信息
        Integer capacity = (Integer) courseResponse.get("capacity");
        Integer enrolled = (Integer) courseResponse.get("enrolled");
        
        // 4. 检查课程容量
        if (enrolled >= capacity) {
            throw new RuntimeException("Course is full: " + courseId);
        }
        
        // 5. 检查重复选课
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new RuntimeException("Already enrolled in this course: " + courseId);
        }
        
        // 6. 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setId(java.util.UUID.randomUUID().toString());
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        Enrollment saved = enrollmentRepository.save(enrollment);
        
        // 7. 更新课程的已选人数（调用catalog-service）
        updateCourseEnrolledCount(courseId, enrolled + 1);
        
        return saved;
    }
    
    private void updateCourseEnrolledCount(String courseId, int newCount) {
        Map<String, Object> updateData = Map.of("enrolled", newCount);
        try {
            catalogClient.updateCourse(courseId, updateData);
        } catch (Exception e) {
            // 记录日志但不影响主流程
            System.err.println("Failed to update course enrolled count: " + e.getMessage());
        }
    }
    
    @Transactional
    public void unenroll(String enrollmentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findById(enrollmentId);
        if (!enrollmentOpt.isPresent()) {
            throw new RuntimeException("Enrollment not found: " + enrollmentId);
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        
        // Delete enrollment
        enrollmentRepository.deleteById(enrollmentId);
        
        try {
            Map<String, Object> courseResponse = catalogClient.getCourse(enrollment.getCourseId());
            if (courseResponse != null) {
                Integer enrolled = (Integer) courseResponse.get("enrolled");
                updateCourseEnrolledCount(enrollment.getCourseId(), enrolled - 1);
            }
        } catch (Exception e) {
            // 记录日志但不影响主流程
            System.err.println("Failed to update course enrolled count: " + e.getMessage());
        }
    }
}