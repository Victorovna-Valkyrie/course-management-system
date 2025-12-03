package com.valkyrie.course.repository;

import com.valkyrie.course.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByStudentId(String studentId);
    List<Student> findByEmail(String email);
}