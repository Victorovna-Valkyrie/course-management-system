package com.valkyrie.enrollment.controller;

import com.valkyrie.enrollment.model.Student;
import com.valkyrie.enrollment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // 获取所有学生
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }
    
    // 获取单个学生
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // 创建学生
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        // Generate ID if not provided
        if (student.getId() == null || student.getId().isEmpty()) {
            student.setId(java.util.UUID.randomUUID().toString());
        }
        
        Student savedStudent = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }
    
    // 更新学生
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student studentDetails) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            Student updatedStudent = student.get();
            // Update fields
            updatedStudent.setStudentId(studentDetails.getStudentId());
            updatedStudent.setName(studentDetails.getName());
            updatedStudent.setMajor(studentDetails.getMajor());
            updatedStudent.setGrade(studentDetails.getGrade());
            updatedStudent.setEmail(studentDetails.getEmail());
            
            studentService.save(updatedStudent);
            
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // 删除学生
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            studentService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}