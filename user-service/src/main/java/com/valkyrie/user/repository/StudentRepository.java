package com.valkyrie.user.repository;

import com.valkyrie.user.model.Student;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StudentRepository {
    
    // 这里应该是JPA Repository，但由于我们使用的是内存存储，所以只是一个占位符
    // 在实际应用中，这里应该是 extends JpaRepository<Student, Long>
}