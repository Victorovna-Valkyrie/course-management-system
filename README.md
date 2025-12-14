# 课程管理系统 (v2.0)

这是一个基于Spring Boot和Spring Cloud的微服务课程管理系统，实现了课程管理、学生管理和选课管理等功能。

## 技术架构

- **后端框架**: Spring Boot 2.7.18, Spring Cloud 2021.0.8
- **编程语言**: Java 11
- **构建工具**: Maven
- **数据库**: MySQL 8.0
- **服务发现**: Nacos
- **API网关**: Spring Cloud Gateway
- **服务调用**: OpenFeign
- **熔断器**: Resilience4j
- **安全认证**: JWT

## 微服务架构

系统采用微服务架构，包含以下服务：

1. **API网关 (Gateway Service)** - 端口8090
   - 统一入口，负责请求路由和转发
   - JWT认证和权限校验
   - CORS跨域支持

2. **用户服务 (User Service)** - 端口8080
   - 用户管理功能
   - 用户认证接口

3. **目录服务 (Catalog Service)** - 端口8081
   - 课程目录管理功能

4. **注册服务 (Enrollment Service)** - 端口8082
   - 学生选课注册功能
   - 通过OpenFeign调用其他服务

## 快速开始

### 环境要求
- Java 11+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置
```sql
CREATE USER 'valkyrie'@'localhost' IDENTIFIED BY '2501005';
CREATE DATABASE course_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON course_management.* TO 'valkyrie'@'localhost';
FLUSH PRIVILEGES;
```

### 构建和运行
```bash
# 克隆项目
git clone <项目地址>
cd course

# 构建项目
mvn clean install

# 运行各服务
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl catalog-service
mvn spring-boot:run -pl enrollment-service
mvn spring-boot:run -pl gateway-service
```

## API网关和认证

### 认证流程
1. 客户端通过 `/api/auth/login` 接口进行身份认证
2. 认证成功后，服务器生成JWT Token并返回给客户端
3. 客户端在后续请求中通过 Authorization Header 携带 Token
4. Gateway验证Token有效性，解析用户信息并添加到请求头中
5. 转发请求到下游服务，下游服务从请求头中获取用户信息

### 路由配置
- 用户服务: `/api/users/**` -> user-service
- 课程服务: `/api/courses/**` -> catalog-service
- 选课服务: `/api/enrollments/**` -> enrollment-service

### 白名单路径
- `/api/auth/login` - 登录接口
- `/api/auth/register` - 注册接口

## 容器化部署

使用Docker Compose进行一键部署:

```bash
docker-compose up -d
```

包含以下服务:
- MySQL数据库
- Nacos服务发现
- 所有微服务

## 版本历史

### v2.0 (当前版本)
- 新增API网关服务
- 集成JWT统一认证机制
- 实现基于请求头的用户信息传递

### v1.4
- 集成Nacos服务注册发现
- 优化enrollment-service前端界面

### v1.3
- 集成OpenFeign实现服务间通信
- 添加Resilience4j熔断机制

### v1.2
- 实现完整的微服务架构
- 独立的用户、课程、选课服务

### v1.0
- 单体应用版本
- 基本的课程管理功能
