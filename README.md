# 课程管理系统

这是一个基于Spring Boot的课程管理系统，实现了课程管理、学生管理和选课管理等功能。系统提供了完整的RESTful API接口，支持课程创建、学生注册、选课操作等核心功能。

## 技术栈

- **后端框架**: Spring Boot 2.7.18
- **编程语言**: Java 11
- **构建工具**: Maven
- **数据库**: MySQL 8.0
- **ORM框架**: Hibernate/JPA
- **数据库连接池**: HikariCP
- **前端展示**: HTML/CSS/JavaScript (仅用于数据展示)

## 项目结构

```
src/
├── main/
│   ├── java/com/valkyrie/course/
│   │   ├── CourseApplication.java         # 应用启动类
│   │   ├── config/
│   │   │   └── DataInitializer.java      # 初始化测试数据
│   │   ├── controller/                   # 控制器层
│   │   │   ├── CourseController.java
│   │   │   ├── StudentController.java
│   │   │   └── EnrollmentController.java
│   │   ├── dto/
│   │   │   └── ApiResponse.java          # 统一API响应格式
│   │   ├── model/                        # 实体类
│   │   │   ├── Course.java
│   │   │   ├── Instructor.java
│   │   │   ├── ScheduleSlot.java
│   │   │   ├── Student.java
│   │   │   └── Enrollment.java
│   │   ├── repository/                   # 数据访问层
│   │   │   ├── CourseRepository.java
│   │   │   ├── StudentRepository.java
│   │   │   └── EnrollmentRepository.java
│   │   └── service/                      # 业务逻辑层
│   │       ├── CourseService.java
│   │       ├── StudentService.java
│   │       └── EnrollmentService.java
│   └── resources/
│       ├── application.properties         # 配置文件
│       ├── application-docker.yml        # Docker环境配置文件
│       └── static/
│           └── index.html                # 前端展示页面
└── test/                                 # 测试代码
```

## 核心功能

### 1. 课程管理
- 查询所有课程: `GET /api/courses`
- 查询单个课程: `GET /api/courses/{id}`
- 创建课程: `POST /api/courses`
- 更新课程: `PUT /api/courses/{id}`
- 删除课程: `DELETE /api/courses/{id}`

### 2. 学生管理
- 创建学生: `POST /api/students`
- 查询所有学生: `GET /api/students`
- 根据ID查询学生: `GET /api/students/{id}`
- 更新学生信息: `PUT /api/students/{id}`
- 删除学生: `DELETE /api/students/{id}`

### 3. 选课管理
- 学生选课: `POST /api/enrollments`
- 学生退课: `DELETE /api/enrollments/{id}`
- 查询所有选课记录: `GET /api/enrollments`
- 按课程查询选课记录: `GET /api/enrollments/course/{courseId}`
- 按学生查询选课记录: `GET /api/enrollments/student/{studentId}`

## 业务规则

1. **课程容量限制**: 课程选课人数不能超过容量（capacity）
2. **重复选课检查**: 同一学生不能重复选择同一门课程
3. **课程存在性检查**: 选课时必须验证课程是否存在
4. **学生验证**: 选课时必须验证学生是否存在，学生不存在时返回404错误
5. **级联更新**: 学生选课成功后，课程的enrolled字段自动增加
6. **数据唯一性**: 学生学号和邮箱必须全局唯一

## 开发逻辑

### 实体关系设计
- **Course（课程）**: 包含课程代码、标题、讲师信息、时间安排、容量等属性
- **Student（学生）**: 包含学号、姓名、专业、年级、邮箱等属性
- **Enrollment（选课记录）**: 关联学生和课程的中间实体
- **Instructor（讲师）**: 作为嵌入对象存在于Course中
- **ScheduleSlot（时间安排）**: 作为嵌入对象存在于Course中

### API设计规范
所有API均遵循统一的响应格式：
```json
{
  "code": 200,
  "message": "Success",
  "data": { ... }
}
```

错误响应格式：
```json
{
  "code": 404,
  "message": "Resource not found",
  "data": null
}
```

### 数据库设计
使用MySQL数据库，包含以下表：
- `courses`: 存储课程信息
- `students`: 存储学生信息
- `enrollments`: 存储选课记录

## 环境配置

### 系统要求
- Java 11或更高版本
- MySQL 8.0或更高版本
- Maven 3.6或更高版本

### 数据库配置
1. 创建MySQL数据库用户:
   ```sql
   CREATE USER 'valkyrie'@'localhost' IDENTIFIED BY '2501005';
   ```

2. 创建数据库并授权:
   ```sql
   CREATE DATABASE course_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   GRANT ALL PRIVILEGES ON course_management.* TO 'valkyrie'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. 配置文件位于`src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&characterEncoding=utf8&useUnicode=true
   spring.datasource.username=valkyrie
   spring.datasource.password=2501005
   ```

## 使用教程

### 1. 克隆项目
```bash
git clone <项目地址>
cd course
```

### 2. 配置数据库
按照上述"数据库配置"章节完成MySQL配置

### 3. 构建项目
```bash
mvn clean install
```

### 4. 运行项目
```bash
mvn spring-boot:run
```

或者在IDE中直接运行`CourseApplication.java`主类

### 5. 访问应用
- API根路径: http://localhost:8080/api
- 前端展示页面: http://localhost:8080
- 默认端口为8080，可通过配置文件修改

### 6. 测试API
系统启动时会自动创建测试数据：
- 3门课程：计算机科学导论、数据结构、算法分析
- 5名学生：张三、李四、王五、赵六、钱七

可以通过前端页面或Postman等工具测试API接口。

## 容器化部署

### 容器化架构概览

容器化部署采用以下架构：

- **应用服务 (app)**: 基于Spring Boot的课程管理系统
- **数据库服务 (mysql)**: MySQL 8.0数据库
- **网络**: 自定义桥接网络 `coursehub-network`
- **数据持久化**: 命名卷 `mysql_data` 存储数据库文件

### Dockerfile 设计

#### 多阶段构建策略

采用多阶段构建优化镜像大小：

```dockerfile
# 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder
# ... 构建应用

# 运行阶段
FROM eclipse-temurin:17-jre-alpine
# ... 运行应用
```

#### 安全性考虑

1. 使用非root用户运行应用
2. 采用Alpine Linux基础镜像减小攻击面
3. 仅包含运行应用所需的最小依赖

#### 构建优化

1. 使用 `.dockerignore` 排除不必要的文件
2. 分层复制提高构建缓存命中率
3. 多阶段构建减少最终镜像大小

### Docker Compose 配置

#### 服务定义

##### 应用服务 (app)
```yaml
app:
  build:
    context: .
    dockerfile: Dockerfile
  ports:
    - "8080:8080"
  environment:
    - SPRING_PROFILES_ACTIVE=docker
    - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8&useUnicode=true
    - SPRING_DATASOURCE_USERNAME=valkyrie
    - SPRING_DATASOURCE_PASSWORD=2501005
  depends_on:
    - mysql
  networks:
    - coursehub-network
  restart: unless-stopped
```

##### 数据库服务 (mysql)
```yaml
mysql:
  image: mysql:8.0
  ports:
    - "3306:3306"
  environment:
    - MYSQL_DATABASE=course_management
    - MYSQL_USER=valkyrie
    - MYSQL_PASSWORD=2501005
    - MYSQL_ROOT_PASSWORD=rootpassword
  volumes:
    - mysql_data:/var/lib/mysql
  networks:
    - coursehub-network
  restart: unless-stopped
  command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

#### 网络配置

```yaml
networks:
  coursehub-network:
    driver: bridge
```

#### 数据卷配置

```yaml
volumes:
  mysql_data:
```

### 容器化特性
- 多阶段构建，优化镜像大小
- 使用非root用户运行应用
- 支持Docker Compose一键部署
- 数据持久化存储
- 环境变量配置支持

### 文件说明
- `Dockerfile`: 定义应用镜像构建过程
- `docker-compose.yml`: 编排应用和数据库服务
- `application-docker.yml`: Docker环境专用配置

### 应用配置调整

#### Docker专用配置文件

创建 `src/main/resources/application-docker.yml`：

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://mysql:3306/course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8&useUnicode=true}
    username: ${SPRING_DATASOURCE_USERNAME:valkyrie}
    password: ${SPRING_DATASOURCE_PASSWORD:2501005}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

#### 关键配置要点

1. 数据库URL使用服务名 `mysql` 而非 `localhost`
2. 通过环境变量支持灵活配置
3. JPA的 `ddl-auto` 设为 `update` 以自动创建表结构
4. 使用Docker专用的profile: `spring.profiles.active=docker`

### 部署操作步骤

#### 1. 构建镜像
```bash
docker compose build
```

#### 2. 启动服务
```bash
docker compose up -d
```

#### 3. 查看服务状态
```bash
docker compose ps
```

#### 4. 查看日志
```bash
# 查看应用日志
docker compose logs -f app

# 查看数据库日志
docker compose logs -f mysql
```

#### 5. 停止服务
```bash
# 停止服务但保留数据
docker compose down

# 停止服务并删除数据卷
docker compose down -v
```

### 环境变量配置
可以通过环境变量自定义配置：
- `SPRING_DATASOURCE_URL`: 数据库连接URL
- `SPRING_DATASOURCE_USERNAME`: 数据库用户名
- `SPRING_DATASOURCE_PASSWORD`: 数据库密码

### 数据持久化
MySQL数据通过命名卷持久化存储，即使容器重启数据也不会丢失。

## API接口详情

### 课程相关接口

#### 获取所有课程
```http
GET http://localhost:8080/api/courses
```

#### 获取指定课程
```http
GET http://localhost:8080/api/courses/{id}
```

#### 创建课程
```http
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "code": "CS101",
  "title": "计算机科学导论",
  "instructor": {
    "id": "T001",
    "name": "张教授",
    "email": "zhang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 50
  },
  "capacity": 60
}
```

#### 更新课程
```http
PUT http://localhost:8080/api/courses/{id}
Content-Type: application/json

{
  "code": "CS101",
  "title": "计算机科学导论-更新版",
  "instructor": {
    "id": "T001",
    "name": "张教授",
    "email": "zhang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 50
  },
  "capacity": 60
}
```

#### 删除课程
```http
DELETE http://localhost:8080/api/courses/{id}
```

### 学生相关接口

#### 获取所有学生
```http
GET http://localhost:8080/api/students
```

#### 获取指定学生
```http
GET http://localhost:8080/api/students/{id}
```

#### 创建学生
```http
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S2024001",
  "name": "张三",
  "major": "计算机科学与技术",
  "grade": 2024,
  "email": "zhangsan@example.com"
}
```

#### 更新学生
```http
PUT http://localhost:8080/api/students/{id}
Content-Type: application/json

{
  "studentId": "S2024001",
  "name": "张三丰",
  "major": "软件工程",
  "grade": 2024,
  "email": "zhangsanfeng@example.com"
}
```

#### 删除学生
```http
DELETE http://localhost:8080/api/students/{id}
```

### 选课相关接口

#### 学生选课
```http
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "课程ID",
  "studentId": "学生ID"
}
```

#### 学生退课
```http
DELETE http://localhost:8080/api/enrollments/{id}
```

#### 获取所有选课记录
```http
GET http://localhost:8080/api/enrollments
```

#### 按课程查询选课记录
```http
GET http://localhost:8080/api/enrollments/course/{courseId}
```

#### 按学生查询选课记录
```http
GET http://localhost:8080/api/enrollments/student/{studentId}
```

## 功能验证

### API测试

#### 获取所有课程
```bash
curl http://localhost:8080/api/courses
```

#### 创建新课程
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "CS102",
    "title": "数据结构与算法",
    "instructor": {
      "id": "T002",
      "name": "李教授",
      "email": "li@example.edu.cn"
    },
    "schedule": {
      "dayOfWeek": "TUESDAY",
      "startTime": "10:00",
      "endTime": "12:00",
      "expectedAttendance": 40
    },
    "capacity": 50
  }'
```

### Web界面访问

访问 http://localhost:8080 查看Web界面。

## 测试数据

系统启动时会自动创建以下测试数据：

### 课程数据
1. 计算机科学导论 (CS101)
2. 数据结构 (CS201)
3. 算法分析 (CS301)

### 学生数据
1. 张三 - 计算机科学与技术专业
2. 李四 - 软件工程专业
3. 王五 - 信息安全专业
4. 赵六 - 人工智能专业
5. 钱七 - 大数据技术专业

## 注意事项

1. 系统使用UTF-8字符编码，支持中文内容正确显示
2. 数据库表会在应用启动时自动创建（通过`spring.jpa.hibernate.ddl-auto=create-drop`配置）
3. 生产环境中应将`ddl-auto`配置改为`validate`或`update`
4. 所有API接口均返回统一格式的JSON响应
5. 系统默认端口为8080，可根据需要在配置文件中修改

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查MySQL服务是否启动
   - 确认数据库用户名和密码正确
   - 确认数据库已创建并授权

2. **中文显示乱码**
   - 确认数据库使用utf8mb4字符集
   - 检查连接字符串是否包含characterEncoding=utf8参数

3. **端口被占用**
   - 修改`application.properties`中的server.port配置
   - 或停止占用8080端口的其他应用

### 日志查看
应用日志会输出到控制台，包含启动信息、数据库操作、API调用等详细信息，有助于排查问题。

## 贡献指南

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目仅供学习和参考使用。