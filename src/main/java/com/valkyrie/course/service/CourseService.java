package com.valkyrie.course.service;

import com.valkyrie.course.model.Course;
import com.valkyrie.course.repository.CourseRepository;
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
    
    public Course save(Course course) {
        return courseRepository.save(course);
    }
    
    public void deleteById(String id) {
        courseRepository.deleteById(id);
    }
}