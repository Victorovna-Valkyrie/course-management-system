package com.valkyrie.enrollment.client;

import com.valkyrie.enrollment.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "user-service",
    fallback = UserClientFallback.class
)
public interface UserClient {
    
    @GetMapping("/api/users/students/{id}")
    Student getStudent(@PathVariable("id") Long id);
}