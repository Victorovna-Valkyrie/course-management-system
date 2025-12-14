package com.valkyrie.user.controller;

import com.valkyrie.user.dto.ApiResponse;
import com.valkyrie.user.model.Student;
import com.valkyrie.user.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users/students")
public class StudentController {
    
    private static final Logger logger = Logger.getLogger(StudentController.class.getName());
    
    @Value("${server.port}")
    private String serverPort;
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        logger.info("Getting all students from instance on port: " + serverPort);
        // 这里应该从service获取所有学生，但为了简化，我们只返回空列表
        List<Student> studentList = new ArrayList<>();
        return new ApiResponse<>(200, "Success", studentList);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Student> getStudentById(@PathVariable String id) {
        logger.info("Getting student by id: " + id + " from instance on port: " + serverPort);
        Student student = studentService.findById(id);
        if (student != null) {
            return new ApiResponse<>(200, "Success", student);
        } else {
            return new ApiResponse<>(404, "Student not found", null);
        }
    }
    
    @PostMapping
    public ApiResponse<Student> createStudent(@RequestBody Student student) {
        logger.info("Creating student from instance on port: " + serverPort);
        Student savedStudent = studentService.save(student);
        return new ApiResponse<>(200, "Success", savedStudent);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Student> updateStudent(@PathVariable String id, @RequestBody Student studentDetails) {
        logger.info("Updating student by id: " + id + " from instance on port: " + serverPort);
        try {
            Student updatedStudent = studentService.update(id, studentDetails);
            return new ApiResponse<>(200, "Success", updatedStudent);
        } catch (RuntimeException e) {
            return new ApiResponse<>(404, e.getMessage(), null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable String id) {
        logger.info("Deleting student by id: " + id + " from instance on port: " + serverPort);
        studentService.deleteById(id);
        return new ApiResponse<>(200, "Success", null);
    }
}