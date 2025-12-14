# Week 09 - API网关与JWT统一认证

## 1. Gateway路由配置说明

### 1.1 依赖配置
在gateway-service的pom.xml中添加了以下依赖：
```xml
<!-- Spring Cloud Gateway -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<!-- JWT 依赖 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
    </dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

### 1.2 路由配置
在application.yml中配置了以下路由规则：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
        - id: catalog-service
          uri: lb://catalog-service
          predicates:
            - Path=/api/courses/**
          filters:
            - StripPrefix=1
        - id: enrollment-service
          uri: lb://enrollment-service
          predicates:
            - Path=/api/enrollments/**
          filters:
            - StripPrefix=1
```

### 1.3 CORS跨域配置
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOriginPatterns: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
```

### 1.4 JWT配置
```yaml
jwt:
  # HS512算法密钥，至少512位(64字节)
  secret: "courseCloudGatewaySecretKeycourseCloudGatewaySecretKeycourseCloudGatewaySecretKeycourseCloudGatewaySecretKey"
  expiration: 86400000
```

## 2. JWT认证流程说明

### 2.1 认证流程
1. 客户端通过 `/api/auth/login` 接口进行身份认证
2. 认证成功后，服务器生成JWT Token并返回给客户端
3. 客户端在后续请求中通过 Authorization Header 携带 Token
4. Gateway拦截请求，验证Token有效性
5. Token验证通过后，解析用户信息并添加到请求头中
6. 转发请求到下游服务，下游服务从请求头中获取用户信息

### 2.2 JWT工具类实现
JWT工具类提供了三个核心方法：
1. `generateToken()` - 生成Token
2. `parseToken()` - 解析Token
3. `validateToken()` - 验证Token

### 2.3 认证过滤器实现
JwtAuthenticationFilter实现了以下功能：
1. 白名单路径直接放行（如登录接口）
2. 从Authorization请求头获取Token
3. 验证Token有效性
4. 解析Token获取用户信息
5. 将用户信息添加到请求头（X-User-Id、X-Username、X-User-Role）
6. 转发请求到下游服务

## 3. 测试结果说明

由于环境限制，无法提供实际的测试截图，但按设计应能观察到以下现象：

### 3.1 登录测试
- POST /api/auth/login 成功返回Token和用户信息

### 3.2 未认证访问测试
- 不携带Token访问受保护资源，返回401 Unauthorized

### 3.3 认证访问测试
- 携带有效Token访问受保护资源，返回200 OK及相应数据

### 3.4 路由转发测试
- 请求能正确转发到对应的微服务

### 3.5 用户信息传递测试
- 下游服务能从请求头中获取到用户信息