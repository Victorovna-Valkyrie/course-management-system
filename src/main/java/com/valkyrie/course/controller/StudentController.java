package com.valkyrie.course.controller;

import com.valkyrie.course.model.Student;
import com.valkyrie.course.service.StudentService;
import com.valkyrie.course.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // 创建学生
    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(@RequestBody Student student) {
        // Validate studentId uniqueness
        if (studentService.existsByStudentId(student.getStudentId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "Student ID already exists"));
        }
        
        // Validate email format (basic validation)
        if (student.getEmail() == null || !student.getEmail().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "Invalid email format"));
        }
        
        // Validate email uniqueness
        if (studentService.existsByEmail(student.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "Email already exists"));
        }
        
        // Generate ID if not provided
        if (student.getId() == null || student.getId().isEmpty()) {
            student.setId(java.util.UUID.randomUUID().toString());
        }
        
        // Set creation timestamp
        student.setCreatedAt(LocalDateTime.now());
        
        Student savedStudent = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(savedStudent));
    }
    
    // 查询所有学生
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return ResponseEntity.ok(ApiResponse.success(students));
    }
    
    // 根据 ID 查询学生
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable String id) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(student.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Student not found"));
        }
    }
    
    // 更新学生信息
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudent(@PathVariable String id, @RequestBody Student studentDetails) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            Student updatedStudent = student.get();
            
            // Check if studentId is being changed and if it's unique
            if (!updatedStudent.getStudentId().equals(studentDetails.getStudentId()) 
                    && studentService.existsByStudentId(studentDetails.getStudentId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(400, "Student ID already exists"));
            }
            
            // Check if email is being changed and if it's unique
            if (!updatedStudent.getEmail().equals(studentDetails.getEmail())
                    && studentService.existsByEmail(studentDetails.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(400, "Email already exists"));
            }
            
            // Update fields
            updatedStudent.setStudentId(studentDetails.getStudentId());
            updatedStudent.setName(studentDetails.getName());
            updatedStudent.setMajor(studentDetails.getMajor());
            updatedStudent.setGrade(studentDetails.getGrade());
            updatedStudent.setEmail(studentDetails.getEmail());
            
            studentService.save(updatedStudent);
            
            return ResponseEntity.ok(ApiResponse.success("Success", updatedStudent));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Student not found"));
        }
    }
    
    // 删除学生
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            // In a full implementation, we would check for enrollments here
            // For now, we'll just delete the student
            studentService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Success", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Student not found"));
        }
    }
}