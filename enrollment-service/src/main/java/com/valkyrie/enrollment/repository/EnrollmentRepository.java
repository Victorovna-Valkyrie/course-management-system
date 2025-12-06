package com.valkyrie.enrollment.repository;

import com.valkyrie.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    List<Enrollment> findByCourseId(String courseId);
    List<Enrollment> findByStudentId(String studentId);
    List<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);
    boolean existsByCourseIdAndStudentId(String courseId, String studentId);
}