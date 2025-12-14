package com.valkyrie.enrollment.client;

import com.valkyrie.enrollment.model.Student;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    
    @Override
    public Student getStudent(Long id) {
        throw new RuntimeException("用户服务暂时不可用，请稍后再试");
    }
}