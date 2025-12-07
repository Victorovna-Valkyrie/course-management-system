# 课程管理系统

这是一个基于Spring Boot的课程管理系统，实现了课程管理、学生管理和选课管理等功能。系统提供了完整的RESTful API接口，支持课程创建、学生注册、选课操作等核心功能。

该系统采用微服务架构，分为三个主要服务：
- 主服务（Main Service）：提供核心的课程、学生和选课管理功能，运行在端口8080
- 目录服务（Catalog Service）：专门处理课程目录相关功能，运行在端口8081
- 注册服务（Enrollment Service）：专门处理学生选课注册相关功能，运行在端口8082

> 当前版本：v1.4  
> [版本更新说明](RELEASE_NOTES_v1.4.md)

## 技术栈

- **后端框架**: Spring Boot 2.7.18
- **编程语言**: Java 11
- **构建工具**: Maven
- **数据库**: MySQL 8.0
- **ORM框架**: Hibernate/JPA
- **数据库连接池**: HikariCP
- **前端展示**: HTML/CSS/JavaScript (仅用于数据展示)
- **容器化**: Docker & Docker Compose
- **服务发现**: Nacos

## 项目结构

```
.
├── src/main/java/com/valkyrie/course/              # 主服务源代码
│   ├── CourseApplication.java                      # 主服务启动类
│   ├── config/
│   │   └── DataInitializer.java                   # 初始化测试数据
│   ├── controller/                                # 主服务控制器层
│   │   ├── CourseController.java
│   │   ├── StudentController.java
│   │   └── EnrollmentController.java
│   ├── dto/
│   │   └── ApiResponse.java                       # 统统API响应格式
│   ├── model/                                     # 主服务实体类
│   │   ├── Course.java
│   │   ├── Instructor.java
│   │   ├── ScheduleSlot.java
│   │   ├── Student.java
│   │   └── Enrollment.java
│   ├── repository/                                # 主服务数据访问层
│   │   ├── CourseRepository.java
│   │   ├── StudentRepository.java
│   │   └── EnrollmentRepository.java
│   └── service/                                   # 主服务业务逻辑层
│       ├── CourseService.java
│       ├── StudentService.java
│       └── EnrollmentService.java
├── catalog-service/                               # 课程目录服务
│   └── src/main/java/com/valkyrie/catalog/
│       ├── CatalogServiceApplication.java         # 目录服务启动类
│       ├── controller/
│       │   └── CourseController.java             # 课程相关API
│       ├── dto/
│       │   └── ApiResponse.java                  # 统一API响应格式
│       ├── model/
│       │   ├── Course.java
│       │   ├── Instructor.java
│       │   └── ScheduleSlot.java
│       ├── repository/
│       │   └── CourseRepository.java
│       └── service/
│           └── CourseService.java
├── enrollment-service/                            # 学生选课服务
│   └── src/main/java/com/valkyrie/enrollment/
│       ├── EnrollmentServiceApplication.java      # 选课服务启动类
│       ├── config/
│       │   └── AppConfig.java                    # 配置类
│       ├── controller/
│       │   ├── EnrollmentController.java         # 选课相关API
│       │   ├── HomeController.java               # 主页控制器
│       │   └── StudentController.java            # 学生相关API
│       ├── model/
│       │   ├── Enrollment.java
│       │   ├── EnrollmentStatus.java
│       │   └── Student.java
│       ├── repository/
│       │   ├── EnrollmentRepository.java
│       │   └── StudentRepository.java
│       └── service/
│           ├── EnrollmentService.java
│           └── StudentService.java
└── src/main/resources/
    ├── application.properties                      # 主服务配置文件
    ├── application-docker.yml                     # Docker环境配置文件
    └── static/
        └── index.html                             # 前端展示页面
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
```
git clone <项目地址>
cd course
```

### 2. 配置数据库
按照上述"数据库配置"章节完成MySQL配置

### 3. 构建项目
```
mvn clean install
```

### 4. 运行项目
```
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

容器化部署采用微服务架构，包含以下组件：

- **主应用服务 (app)**: 基于Spring Boot的课程管理系统主服务，监听端口8080
- **目录服务 (catalog-service)**: 课程目录管理服务，监听端口8081
- **注册服务 (enrollment-service)**: 学生选课注册服务，监听端口8082
- **数据库服务 (mysql)**: MySQL 8.0数据库
- **网络**: 自定义桥接网络 `coursehub-network`
- **数据持久化**: 命名卷 `mysql_data` 存储数据库文件

当系统运行后，可以通过以下URL访问各个服务：
- 主服务API: http://localhost:8080
- 课程目录服务API: http://localhost:8081
- 学生选课服务API: http://localhost:8082
- 数据库连接: localhost:3306

### Dockerfile 设计

#### 多阶段构建策略

采用多阶段构建优化镜像大小：

```
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

##### 主应用服务 (app)
```
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

##### 目录服务 (catalog-service)
```
catalog-service:
  build:
    context: ./catalog-service
  ports:
    - "8081:8081"
  environment:
    - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8&useUnicode=true
    - SPRING_DATASOURCE_USERNAME=valkyrie
    - SPRING_DATASOURCE_PASSWORD=2501005
  depends_on:
    - mysql
  networks:
    - coursehub-network
  restart: unless-stopped
```

##### 注册服务 (enrollment-service)
```
enrollment-service:
  build:
    context: ./enrollment-service
  ports:
    - "8082:8082"
  environment:
    - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8&useUnicode=true
    - SPRING_DATASOURCE_USERNAME=valkyrie
    - SPRING_DATASOURCE_PASSWORD=2501005
    - CATALOG_SERVICE_URL=http://catalog-service:8081
  depends_on:
    - mysql
  networks:
    - coursehub-network
  restart: unless-stopped
```

##### 数据库服务 (mysql)
```
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

```
networks:
  coursehub-network:
    driver: bridge
```

#### 数据卷配置

```
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

```
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
```
docker compose build
```

#### 2. 启动服务
```
docker compose up -d
```

系统启动后，可以通过以下URL访问各个服务：
- 主服务Web界面: http://localhost:8080
- 主服务API接口: http://localhost:8080/api
- 课程目录服务API: http://localhost:8081/api
- 学生选课服务API: http://localhost:8082/api
- 数据库连接: localhost:3306

#### 3. 查看服务状态
```
docker compose ps
```

#### 4. 查看日志
```
# 查看主应用日志
docker compose logs -f app

# 查看目录服务日志
docker compose logs -f catalog-service

# 查看注册服务日志
docker compose logs -f enrollment-service

# 查看数据库日志
docker compose logs -f mysql
```

#### 5. 停止服务
```
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

## Nacos服务发现集成

本项目已集成Nacos作为服务发现组件，实现服务的自动注册与发现，替代原有的硬编码服务地址方式。

### Nacos配置要求

在各服务的 `application.yml` 中添加了Nacos配置:

```yaml
spring:
  application:
    name: catalog-service # 服务名: catalog-service 或 enrollment-service
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        namespace: dev
        group: COURSEHUB_GROUP
        ephemeral: true
        heart-beat-interval: 5000
        heart-beat-timeout: 15000
```

### Docker Compose更新

docker-compose.yml文件已更新，添加了Nacos服务:

```yaml
services:
  nacos:
    image: nacos/nacos-server:v2.2.3
    container_name: nacos
    environment:
      - MODE=standalone
    ports:
      - "8848:8848"
      - "9848:9848"
    networks:
      - coursehub-network
    restart: unless-stopped
```

### 服务间调用方式变化

Enrollment Service现在通过服务名调用Catalog Service，而不是使用硬编码的URL地址。

### 健康检查配置

通过Spring Boot Actuator配置了健康检查接口，确保Nacos能够准确监控服务状态:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### Nacos控制台访问

- 控制台地址: http://localhost:8848/nacos
- 默认用户名: nacos
- 默认密码: nacos

## API接口详情

### 主服务接口

#### 课程相关接口
- 获取所有课程: `GET http://localhost:8080/api/courses`
- 获取指定课程: `GET http://localhost:8080/api/courses/{id}`
- 创建课程: `POST http://localhost:8080/api/courses`
- 更新课程: `PUT http://localhost:8080/api/courses/{id}`
- 删除课程: `DELETE http://localhost:8080/api/courses/{id}`

#### 学生相关接口
- 创建学生: `POST http://localhost:8080/api/students`
- 获取所有学生: `GET http://localhost:8080/api/students`
- 根据ID获取学生: `GET http://localhost:8080/api/students/{id}`
- 更新学生信息: `PUT http://localhost:8080/api/students/{id}`
- 删除学生: `DELETE http://localhost:8080/api/students/{id}`

#### 选课相关接口
- 学生选课: `POST http://localhost:8080/api/enrollments`
- 学生退课: `DELETE http://localhost:8080/api/enrollments/{id}`
- 获取所有选课记录: `GET http://localhost:8080/api/enrollments`
- 按课程查询选课记录: `GET http://localhost:8080/api/enrollments/course/{courseId}`
- 按学生查询选课记录: `GET http://localhost:8080/api/enrollments/student/{studentId}`

### 目录服务接口 (端口8081)

#### 课程相关接口
- 获取所有课程: `GET http://localhost:8081/api/courses`
- 获取指定课程: `GET http://localhost:8081/api/courses/{id}`
- 创建课程: `POST http://localhost:8081/api/courses`
- 更新课程: `PUT http://localhost:8081/api/courses/{id}`
- 删除课程: `DELETE http://localhost:8081/api/courses/{id}`

### 注册服务接口 (端口8082)

#### 学生相关接口
- 创建学生: `POST http://localhost:8082/api/students`
- 获取所有学生: `GET http://localhost:8082/api/students`
- 根据ID获取学生: `GET http://localhost:8082/api/students/{id}`
- 更新学生信息: `PUT http://localhost:8082/api/students/{id}`
- 删除学生: `DELETE http://localhost:8082/api/students/{id}`

#### 选课相关接口
- 学生选课: `POST http://localhost:8082/api/enrollments`
- 学生退课: `DELETE http://localhost:8082/api/enrollments/{id}`
- 获取所有选课记录: `GET http://localhost:8082/api/enrollments`
- 按课程查询选课记录: `GET http://localhost:8082/api/enrollments/course/{courseId}`
- 按学生查询选课记录: `GET http://localhost:8082/api/enrollments/student/{studentId}`

## 功能验证

### API测试

#### 获取所有课程
```
# 从主服务获取课程
curl http://localhost:8080/api/courses

# 从目录服务获取课程
curl http://localhost:8081/api/courses
```

#### 创建新课程
```
# 通过主服务创建课程
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

# 通过目录服务创建课程
curl -X POST http://localhost:8081/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "CS103",
    "title": "操作系统原理",
    "instructor": {
      "id": "T003",
      "name": "王教授",
      "email": "wang@example.edu.cn"
    },
    "schedule": {
      "dayOfWeek": "WEDNESDAY",
      "startTime": "14:00",
      "endTime": "16:00",
      "expectedAttendance": 35
    },
    "capacity": 45
  }'
```

### Web界面访问

访问以下URL查看各服务的Web界面：
- 主服务界面: http://localhost:8080
- 注册服务界面: http://localhost:8082

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

## 开发过程记录

### 版本 v1.4 更新内容

此版本主要集成了Nacos服务注册发现功能，优化了enrollment-service的前端界面。

#### Nacos服务注册发现集成
- 集成了Nacos作为服务注册与发现组件
- 实现了微服务的自动注册与发现
- 替代了原有的硬编码服务地址方式
- 添加了Nacos健康检查配置

#### enrollment-service 前端界面优化
- 重构了学生管理和选课管理界面，采用标签页设计
- 统一了界面风格，与catalog-service保持一致
- 改进了数据表格展示，使信息更加清晰易读
- 完善了所有CRUD操作的前端实现
- 增加了详细的错误提示和成功反馈
- 优化了JSON数据的输入和展示方式

### 服务注册问题修复

在项目初期部署过程中，我们遇到了微服务无法正常注册到Nacos的问题。经过分析和排查，我们进行了以下关键修改：

#### 问题现象
- Nacos控制台服务列表为空
- 微服务启动正常，但未在Nacos中显示
- 无明显的错误日志指示注册失败原因

#### 问题分析与解决过程
1. **检查@EnableDiscoveryClient注解**
   - 确认所有微服务启动类均已添加`@EnableDiscoveryClient`注解
   - catalog-service初始缺少该注解，已补充添加

2. **验证Nacos配置**
   - 检查各服务的application.yml配置文件
   - 确保`spring.cloud.nacos.discovery`配置正确
   - 移除了可能导致问题的`namespace: dev`配置，改用默认命名空间

3. **添加必要的依赖**
   - 为主服务添加了Nacos Discovery Starter依赖
   - 确保所有服务的pom.xml都包含必要的Nacos依赖

4. **配置优化**
   - 在所有服务中显式启用了服务发现功能：
     ```yaml
     spring.cloud.discovery.enabled: true
     spring.cloud.nacos.discovery.enabled: true
     ```
   - 添加了健康检查配置以确保Nacos能正确监控服务状态

5. **健康检查端点**
   - 为所有服务添加了健康检查控制器，提供`/health`端点

6. **Docker Compose优化**
   - 添加了服务健康检查机制
   - 设置了服务依赖关系，确保Nacos和MySQL健康后再启动应用服务

#### 最终效果
经过以上修改，所有微服务（course-service、catalog-service、enrollment-service）都能成功注册到Nacos，并在Nacos控制台的服务列表中正常显示。

### enrollment-service前端界面优化

为了提升用户体验，我们对enrollment-service的前端界面进行了全面优化：

#### 优化内容
1. **界面重构**：
   - 采用了标签页设计，将学生管理和选课管理功能分离
   - 统一了界面风格，使其与catalog-service保持一致
   - 改进了数据表格展示，使信息更加清晰易读

2. **功能增强**：
   - 完善了所有CRUD操作的前端实现
   - 增加了详细的错误提示和成功反馈
   - 优化了JSON数据的输入和展示方式

3. **交互改善**：
   - 提供了更友好的用户操作引导
   - 增加了示例数据模板，方便用户快速上手
   - 改进了响应结果显示方式，支持表格和JSON两种形式

#### 使用方法
访问 http://localhost:8082 即可使用优化后的界面，通过标签页切换学生管理和选课管理功能：
- 学生管理：支持学生信息的增删改查操作
- 选课管理：支持选课记录查询、学生选课和退课操作

## 贡献指南

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目仅供学习和参考使用。