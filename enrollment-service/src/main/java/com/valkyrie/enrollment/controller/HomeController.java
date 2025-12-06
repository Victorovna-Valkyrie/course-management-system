package com.valkyrie.enrollment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<String> home() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Enrollment Service</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            max-width: 800px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #f5f5f5;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: white;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .endpoint {\n" +
                "            background-color: #f8f9fa;\n" +
                "            border-left: 4px solid #007bff;\n" +
                "            padding: 15px;\n" +
                "            margin: 15px 0;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        code {\n" +
                "            background-color: #e9ecef;\n" +
                "            padding: 2px 4px;\n" +
                "            border-radius: 3px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Enrollment Service</h1>\n" +
                "        <p>欢迎使用学生选课服务！以下是可用的API端点：</p>\n" +
                "        \n" +
                "        <div class=\"endpoint\">\n" +
                "            <h3>学生管理</h3>\n" +
                "            <p><strong>GET</strong> <code>/api/students</code> - 获取所有学生</p>\n" +
                "            <p><strong>GET</strong> <code>/api/students/{id}</code> - 获取特定学生</p>\n" +
                "            <p><strong>POST</strong> <code>/api/students</code> - 创建学生</p>\n" +
                "            <p><strong>PUT</strong> <code>/api/students/{id}</code> - 更新学生</p>\n" +
                "            <p><strong>DELETE</strong> <code>/api/students/{id}</code> - 删除学生</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"endpoint\">\n" +
                "            <h3>选课管理</h3>\n" +
                "            <p><strong>GET</strong> <code>/api/enrollments</code> - 获取所有选课记录</p>\n" +
                "            <p><strong>GET</strong> <code>/api/enrollments/course/{courseId}</code> - 按课程查询选课</p>\n" +
                "            <p><strong>GET</strong> <code>/api/enrollments/student/{studentId}</code> - 按学生查询选课</p>\n" +
                "            <p><strong>POST</strong> <code>/api/enrollments</code> - 学生选课</p>\n" +
                "            <p><strong>DELETE</strong> <code>/api/enrollments/{id}</code> - 学生退课</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        return ResponseEntity.ok(html);
    }
}