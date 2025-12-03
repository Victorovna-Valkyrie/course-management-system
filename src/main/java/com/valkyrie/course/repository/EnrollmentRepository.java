package com.valkyrie.course.repository;

import com.valkyrie.course.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    List<Enrollment> findByCourseId(String courseId);
    List<Enrollment> findByStudentId(String studentId);
    List<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);
}