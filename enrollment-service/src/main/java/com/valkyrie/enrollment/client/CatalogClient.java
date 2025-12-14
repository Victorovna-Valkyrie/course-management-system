package com.valkyrie.enrollment.client;

import com.valkyrie.catalog.model.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
    name = "catalog-service",
    fallback = CatalogClientFallback.class
)
public interface CatalogClient {
    
    @GetMapping("/api/courses/{id}")
    Map<String, Object> getCourse(@PathVariable("id") String id);
    
    @PutMapping("/api/courses/{id}")
    void updateCourse(@PathVariable("id") String id, @RequestBody Map<String, Object> updateData);
}