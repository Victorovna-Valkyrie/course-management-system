package com.valkyrie.user.controller;

import com.valkyrie.user.model.Student;
import com.valkyrie.user.service.StudentService;
import com.valkyrie.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    // 临时存储用户凭证，实际项目中应使用加密存储
    private Map<String, String> userCredentials = new HashMap<>();

    @PostConstruct
    public void init() {
        // 初始化测试用户
        userCredentials.put("admin", "admin123");
        userCredentials.put("zhangsan", "password123");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 验证用户名和密码
        String storedPassword = userCredentials.get(request.getUsername());
        if (storedPassword == null || !storedPassword.equals(request.getPassword())) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }

        // 查找用户信息
        Student user = findUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body("用户不存在");
        }

        // 生成 JWT Token
        String userId = user.getId();
        String username = user.getName();
        String role = "USER"; // 默认角色
        if ("admin".equals(request.getUsername())) {
            role = "ADMIN";
        }
        String token = jwtUtil.generateToken(userId, request.getUsername(), role);

        // 返回 Token 和用户信息
        LoginResponse response = new LoginResponse(token, user);
        return ResponseEntity.ok(response);
    }

    private Student findUserByUsername(String username) {
        // 简化实现，实际应该查询数据库
        if ("admin".equals(username)) {
            Student admin = new Student();
            admin.setId("1");
            admin.setStudentId("ADMIN001");
            admin.setName("管理员");
            admin.setMajor("系统管理");
            admin.setGrade(4);
            admin.setEmail("admin@example.com");
            return admin;
        }
        
        // 查找学生用户
        return studentService.findById(username.equals("zhangsan") ? "1" : null);
    }

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String token;
        private Student user;

        public LoginResponse(String token, Student user) {
            this.token = token;
            this.user = user;
        }

        // Getters and setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Student getUser() {
            return user;
        }

        public void setUser(Student user) {
            this.user = user;
        }
    }
}