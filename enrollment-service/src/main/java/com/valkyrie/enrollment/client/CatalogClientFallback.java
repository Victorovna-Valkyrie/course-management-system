package com.valkyrie.enrollment.client;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CatalogClientFallback implements CatalogClient {
    
    @Override
    public Map<String, Object> getCourse(String id) {
        throw new RuntimeException("课程服务暂时不可用，请稍后再试");
    }
    
    @Override
    public void updateCourse(String id, Map<String, Object> updateData) {
        throw new RuntimeException("课程服务暂时不可用，请稍后再试");
    }
}