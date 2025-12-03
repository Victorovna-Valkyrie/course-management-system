package com.valkyrie.course.controller;

import com.valkyrie.course.dto.ApiResponse;
import com.valkyrie.course.model.Enrollment;
import com.valkyrie.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    // 学生选课
    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> enrollStudent(@RequestBody Map<String, String> request) {
        String courseId = request.get("courseId");
        String studentId = request.get("studentId");
        
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(courseId, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(enrollment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    // 学生退课
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> unenrollStudent(@PathVariable String id) {
        try {
            enrollmentService.unenrollStudent(id);
            return ResponseEntity.ok(ApiResponse.success("Success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    // 查询选课记录
    @GetMapping
    public ResponseEntity<ApiResponse<List<Enrollment>>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.findAll();
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }
    
    // 按课程查询
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByCourse(@PathVariable String courseId) {
        List<Enrollment> enrollments = enrollmentService.findByCourseId(courseId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }
    
    // 按学生查询
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByStudent(@PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.findByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }
    
    // 查询单个选课记录
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> getEnrollmentById(@PathVariable String id) {
        Optional<Enrollment> enrollment = enrollmentService.findById(id);
        
        if (enrollment.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(enrollment.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Enrollment not found"));
        }
    }
}