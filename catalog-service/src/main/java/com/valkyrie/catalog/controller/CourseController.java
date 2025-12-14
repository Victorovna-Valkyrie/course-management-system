package com.valkyrie.catalog.controller;

import com.valkyrie.catalog.dto.ApiResponse;
import com.valkyrie.catalog.model.Course;
import com.valkyrie.catalog.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    private static final Logger logger = Logger.getLogger(CourseController.class.getName());
    
    @Value("${server.port}")
    private String serverPort;
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public ApiResponse<List<Course>> getAllCourses() {
        logger.info("Getting all courses from instance on port: " + serverPort);
        List<Course> courses = courseService.findAll();
        return new ApiResponse<>(200, "Success", courses);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Course> getCourseById(@PathVariable String id) {
        logger.info("Getting course by id: " + id + " from instance on port: " + serverPort);
        Course course = courseService.findById(id).orElse(null);
        if (course != null) {
            return new ApiResponse<>(200, "Success", course);
        } else {
            return new ApiResponse<>(404, "Course not found", null);
        }
    }
    
    @PostMapping
    public ApiResponse<Course> createCourse(@RequestBody Course course) {
        logger.info("Creating course from instance on port: " + serverPort);
        Course savedCourse = courseService.save(course);
        return new ApiResponse<>(200, "Success", savedCourse);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Course> updateCourse(@PathVariable String id, @RequestBody Course courseDetails) {
        logger.info("Updating course by id: " + id + " from instance on port: " + serverPort);
        Course updatedCourse = courseService.update(id, courseDetails);
        return new ApiResponse<>(200, "Success", updatedCourse);
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable String id) {
        logger.info("Deleting course by id: " + id + " from instance on port: " + serverPort);
        courseService.deleteById(id);
        return new ApiResponse<>(200, "Success", null);
    }
}