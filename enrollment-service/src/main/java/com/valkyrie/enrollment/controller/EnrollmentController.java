package com.valkyrie.enrollment.controller;

import com.valkyrie.enrollment.dto.ApiResponse;
import com.valkyrie.enrollment.model.Enrollment;
import com.valkyrie.enrollment.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    private static final Logger logger = Logger.getLogger(EnrollmentController.class.getName());
    
    @Value("${server.port}")
    private String serverPort;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @GetMapping
    public ApiResponse<List<Enrollment>> getAllEnrollments() {
        logger.info("Getting all enrollments from instance on port: " + serverPort);
        List<Enrollment> enrollments = enrollmentService.findAll();
        return new ApiResponse<>(200, "Success", enrollments);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Enrollment> getEnrollmentById(@PathVariable String id) {
        logger.info("Getting enrollment by id: " + id + " from instance on port: " + serverPort);
        Optional<Enrollment> enrollment = enrollmentService.findById(id);
        if (enrollment.isPresent()) {
            return new ApiResponse<>(200, "Success", enrollment.get());
        } else {
            return new ApiResponse<>(404, "Enrollment not found", null);
        }
    }
    
    @PostMapping
    public ApiResponse<Enrollment> createEnrollment(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Username") String username,
            @RequestBody Enrollment enrollment) {
        logger.info("User " + username + " (ID: " + userId + ") creating enrollment from instance on port: " + serverPort);
        Enrollment savedEnrollment = enrollmentService.save(enrollment);
        return new ApiResponse<>(200, "Success", savedEnrollment);
    }
    
    @PostMapping("/enroll")
    public ApiResponse<Enrollment> enroll(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Username") String username,
            @RequestParam String courseId, 
            @RequestParam String studentId) {
        logger.info("User " + username + " (ID: " + userId + ") enrolling student " + studentId + " to course " + courseId + " from instance on port: " + serverPort);
        try {
            Enrollment enrollment = enrollmentService.enroll(courseId, studentId);
            return new ApiResponse<>(200, "Success", enrollment);
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        }
    }
    
    @DeleteMapping("/unenroll/{id}")
    public ApiResponse<Void> unenroll(@PathVariable String id) {
        logger.info("Unenrolling enrollment " + id + " from instance on port: " + serverPort);
        try {
            enrollmentService.unenroll(id);
            return new ApiResponse<>(200, "Success", null);
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEnrollment(@PathVariable String id) {
        logger.info("Deleting enrollment by id: " + id + " from instance on port: " + serverPort);
        enrollmentService.deleteById(id);
        return new ApiResponse<>(200, "Success", null);
    }
}