package com.valkyrie.course.service;

import com.valkyrie.course.model.Student;
import com.valkyrie.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }
    
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteById(String id) {
        studentRepository.deleteById(id);
    }
    
    public boolean existsByStudentId(String studentId) {
        return !studentRepository.findByStudentId(studentId).isEmpty();
    }
    
    public boolean existsByEmail(String email) {
        return !studentRepository.findByEmail(email).isEmpty();
    }
}