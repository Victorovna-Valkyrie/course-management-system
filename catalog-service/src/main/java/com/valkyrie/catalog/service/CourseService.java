package com.valkyrie.catalog.service;

import com.valkyrie.catalog.model.Course;
import com.valkyrie.catalog.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Course> findAll() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> findById(String id) {
        return courseRepository.findById(id);
    }
    
    public List<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }
    
    public Course save(Course course) {
        return courseRepository.save(course);
    }
    
    public Course update(String id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        course.setCode(courseDetails.getCode());
        course.setTitle(courseDetails.getTitle());
        course.setInstructor(courseDetails.getInstructor());
        course.setSchedule(courseDetails.getSchedule());
        course.setCapacity(courseDetails.getCapacity());
        course.setEnrolled(courseDetails.getEnrolled());
        
        return courseRepository.save(course);
    }
    
    public void deleteById(String id) {
        courseRepository.deleteById(id);
    }
}