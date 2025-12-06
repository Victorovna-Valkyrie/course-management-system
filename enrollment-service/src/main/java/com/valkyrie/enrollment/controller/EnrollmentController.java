package com.valkyrie.enrollment.controller;

import com.valkyrie.enrollment.model.Enrollment;
import com.valkyrie.enrollment.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    // 获取所有选课记录
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.findAll();
        return ResponseEntity.ok(enrollments);
    }
    
    // 按课程查询选课
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseId(@PathVariable String courseId) {
        List<Enrollment> enrollments = enrollmentService.findByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }
    
    // 按学生查询选课
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentId(@PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.findByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    // 学生选课
    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestBody Map<String, String> request) {
        try {
            String courseId = request.get("courseId");
            String studentId = request.get("studentId");
            
            Enrollment enrollment = enrollmentService.enroll(courseId, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    // 学生退课
    @DeleteMapping("/{id}")
    public ResponseEntity<?> unenrollStudent(@PathVariable String id) {
        try {
            enrollmentService.unenroll(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}