package com.valkyrie.enrollment.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    private String id;
    
    private String courseId;
    private String studentId;
    
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
    
    private LocalDateTime enrolledAt;

    // Constructors
    public Enrollment() {}

    public Enrollment(String id, String courseId, String studentId, EnrollmentStatus status, LocalDateTime enrolledAt) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.status = status;
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

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}