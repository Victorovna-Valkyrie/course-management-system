package com.valkyrie.course.service;

import com.valkyrie.course.model.Course;
import com.valkyrie.course.model.Enrollment;
import com.valkyrie.course.model.Student;
import com.valkyrie.course.repository.CourseRepository;
import com.valkyrie.course.repository.EnrollmentRepository;
import com.valkyrie.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
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
    
    public Enrollment enrollStudent(String courseId, String studentId) throws Exception {
        // Check if course exists
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (!courseOpt.isPresent()) {
            throw new Exception("Course not found");
        }
        
        Course course = courseOpt.get();
        
        // Check if student exists
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            throw new Exception("Student not found");
        }
        
        // Check if student is already enrolled in this course
        List<Enrollment> existingEnrollments = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId);
        if (!existingEnrollments.isEmpty()) {
            throw new Exception("Student already enrolled in this course");
        }
        
        // Check if course capacity is full
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new Exception("Course capacity is full");
        }
        
        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setId(java.util.UUID.randomUUID().toString());
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setEnrolledAt(LocalDateTime.now());
        
        // Save enrollment
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
        // Update course enrolled count
        course.incrementEnrolled();
        courseRepository.save(course);
        
        return savedEnrollment;
    }
    
    public void unenrollStudent(String enrollmentId) throws Exception {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findById(enrollmentId);
        if (!enrollmentOpt.isPresent()) {
            throw new Exception("Enrollment not found");
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        
        // Delete enrollment
        enrollmentRepository.deleteById(enrollmentId);
        
        // Update course enrolled count
        Optional<Course> courseOpt = courseRepository.findById(enrollment.getCourseId());
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.decrementEnrolled();
            courseRepository.save(course);
        }
    }
}