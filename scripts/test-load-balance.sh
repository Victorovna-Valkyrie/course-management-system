#!/bin/bash

# 测试负载均衡和OpenFeign功能的脚本

echo "=== 测试负载均衡和OpenFeign功能 ==="

# 等待服务启动
echo "等待服务启动..."
sleep 30

# 检查Nacos是否运行正常
echo "检查Nacos服务..."
curl -s http://localhost:8848/nacos > /dev/null
if [ $? -eq 0 ]; then
    echo "✓ Nacos服务运行正常"
else
    echo "✗ Nacos服务无法访问"
    exit 1
fi

# 检查各服务实例是否注册到Nacos
echo "检查服务注册情况..."
SERVICES=("catalog-service" "user-service" "enrollment-service")
for service in "${SERVICES[@]}"; do
    response=$(curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=$service")
    if echo "$response" | grep -q '"hosts"'; then
        echo "✓ $service 已注册到Nacos"
    else
        echo "✗ $service 未注册到Nacos"
    fi
done

# 创建测试学生 (在enrollment-service中创建学生)
echo "在enrollment服务中创建学生记录..."
enrollment_student_response=$(curl -s -X POST http://localhost:8082/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "id": "stud-001",
    "studentId": "S001",
    "name": "张三",
    "major": "计算机科学",
    "grade": 3,
    "email": "zhangsan@example.com"
  }')

echo "enrollment服务学生创建响应: $enrollment_student_response"

# 在user-service中创建学生
echo "在user-service中创建学生记录..."
user_student_response=$(curl -s -X POST http://localhost:8087/api/users/students \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "studentId": "S001",
    "name": "张三",
    "major": "计算机科学",
    "grade": 3,
    "email": "zhangsan@example.com"
  }')

echo "user-service学生创建响应: $user_student_response"

# 创建测试课程 (使用更新后的端口)
echo "创建测试课程..."
course_response=$(curl -s -X POST http://localhost:8091/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "id": "course-001",
    "code": "CS101",
    "title": "计算机基础",
    "instructor": {
      "id": "inst-001",
      "name": "李老师",
      "email": "li.teacher@example.com"
    },
    "schedule": {
      "dayOfWeek": "MONDAY",
      "startTime": "09:00",
      "endTime": "11:00",
      "expectedAttendance": 30
    },
    "capacity": 5,
    "enrolled": 0
  }')

echo "课程创建响应: $course_response"

# 进行选课操作测试负载均衡
echo "进行选课操作测试负载均衡..."
for i in {1..3}; do
    echo "第 $i 次选课..."
    enroll_response=$(curl -s -X POST "http://localhost:8082/api/enrollments" \
      -H "Content-Type: application/json" \
      -d '{
        "id": "enroll-'$i'",
        "courseId": "course-001",
        "studentId": "stud-001",
        "status": "ACTIVE"
      }')
    echo "选课响应: $enroll_response"
    sleep 2
done

# 测试熔断机制
echo "测试熔断机制..."
echo "停止所有user-service实例..."
docker stop course_user-service-1_1 course_user-service-2_1 course_user-service-3_1

echo "等待服务停止..."
sleep 10

echo "尝试进行选课操作，应触发熔断..."
enroll_response=$(curl -s -X POST "http://localhost:8082/api/enrollments" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "enroll-melt",
    "courseId": "course-001",
    "studentId": "stud-001",
    "status": "ACTIVE"
  }')
echo "熔断测试响应: $enroll_response"

echo "重启user-service实例..."
docker start course_user-service-1_1 course_user-service-2_1 course_user-service-3_1

echo "等待服务重启..."
sleep 30

echo "=== 测试完成 ==="