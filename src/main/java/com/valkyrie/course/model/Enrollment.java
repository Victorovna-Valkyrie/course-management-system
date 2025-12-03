package com.valkyrie.course.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    private String id;
    private String courseId;
    private String studentId;
    private LocalDateTime enrolledAt;

    // Constructors
    public Enrollment() {}

    public Enrollment(String id, String courseId, String studentId, LocalDateTime enrolledAt) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.enrolledAt = enrolledAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}