#!/bin/bash

# 测试脚本 - 课程管理系统API

echo "=== 课程管理系统API测试 ==="

# 1. 获取所有课程
echo -e "\n1. 获取所有课程:"
curl -X GET http://localhost:8080/api/courses

# 2. 获取所有学生
echo -e "\n2. 获取所有学生:"
curl -X GET http://localhost:8080/api/students

# 3. 创建新课程
echo -e "\n3. 创建新课程:"
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
  "code": "CS105",
  "title": "Web开发技术",
  "instructor": {
    "id": "T004",
    "name": "陈教授",
    "email": "chen@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "FRIDAY",
    "startTime": "14:00",
    "endTime": "16:00",
    "expectedAttendance": 40
  },
  "capacity": 50
}'

# 4. 创建新学生
echo -e "\n4. 创建新学生:"
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
  "studentId": "2024006",
  "name": "孙八",
  "major": "网络工程",
  "grade": 2024,
  "email": "sunba@example.com"
}'

echo -e "\n=== 测试完成 ==="