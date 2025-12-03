package com.valkyrie.course.controller;

import com.valkyrie.course.model.Course;
import com.valkyrie.course.service.CourseService;
import com.valkyrie.course.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    // 查询所有课程
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.findAll();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }
    
    // 查询单个课程
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable String id) {
        Optional<Course> course = courseService.findById(id);
        
        if (course.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(course.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Course not found"));
        }
    }
    
    // 创建课程
    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        // Generate ID if not provided
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId(java.util.UUID.randomUUID().toString());
        }
        
        Course savedCourse = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(savedCourse));
    }
    
    // 更新课程
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable String id, @RequestBody Course courseDetails) {
        Optional<Course> course = courseService.findById(id);
        
        if (course.isPresent()) {
            Course updatedCourse = course.get();
            // Update fields
            updatedCourse.setCode(courseDetails.getCode());
            updatedCourse.setTitle(courseDetails.getTitle());
            updatedCourse.setInstructor(courseDetails.getInstructor());
            updatedCourse.setSchedule(courseDetails.getSchedule());
            updatedCourse.setCapacity(courseDetails.getCapacity());
            
            courseService.save(updatedCourse);
            
            return ResponseEntity.ok(ApiResponse.success("Success", updatedCourse));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Course not found"));
        }
    }
    
    // 删除课程
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable String id) {
        Optional<Course> course = courseService.findById(id);
        
        if (course.isPresent()) {
            courseService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Success", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Course not found"));
        }
    }
}