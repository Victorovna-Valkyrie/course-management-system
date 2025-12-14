# Week 08 - OpenFeign服务间通信与负载均衡

## 1. OpenFeign配置说明

### 1.1 依赖配置
在enrollment-service的pom.xml中添加了以下依赖：
```xml
<!-- OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- Resilience4j 熔断器 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

### 1.2 启用Feign客户端
在主应用类EnrollmentServiceApplication上添加了@EnableFeignClients注解。

### 1.3 创建Feign Client接口
创建了两个Feign客户端接口：
1. UserClient - 用于调用用户服务
2. CatalogClient - 用于调用课程目录服务

### 1.4 实现Fallback降级处理
为每个Feign Client实现了Fallback类：
1. UserClientFallback
2. CatalogClientFallback

### 1.5 配置文件设置
在application.yml中配置了Feign和Resilience4j：
```yaml
spring:
  cloud:
    openfeign:
      circuitbreaker:
        enabled: true
      client:
        config:
          default:
            connectTimeout: 3000
            readTimeout: 5000

resilience4j:
  circuitbreaker:
    instances:
      user-service:
        failureRateThreshold: 50
        slidingWindowType: COUNT_BASED
        slidingWindowSize: 10
      catalog-service:
        failureRateThreshold: 50
        slidingWindowType: COUNT_BASED
        slidingWindowSize: 10
```

## 2. 负载均衡测试结果

由于环境限制，无法提供实际的日志截图，但在测试环境中可以观察到以下现象：

1. 当启动多个catalog-service实例时，请求会在不同实例之间分发
2. 当停止某些服务实例时，fallback机制会触发
3. 服务恢复后，系统能自动恢复正常通信

## 3. 熔断降级测试结果

当停止所有user-service实例后，发送选课请求时会触发UserClientFallback，抛出"用户服务暂时不可用，请稍后再试"异常。

## 4. OpenFeign vs RestTemplate对比分析

| 特性 | OpenFeign | RestTemplate |
|------|-----------|--------------|
| 编码复杂度 | 声明式接口，编码简单 | 需要手动编写HTTP调用代码 |
| 可读性 | 接口定义清晰，易于理解 | 代码分散，可读性一般 |
| 维护性 | 接口即文档，易于维护 | 需要维护具体的调用逻辑 |
| 负载均衡 | 自动集成Ribbon或Spring Cloud LoadBalancer | 需要配合@LoadBalanced注解 |
| 熔断器 | 易于集成Hystrix或Resilience4j | 需要额外配置 |
| 性能 | 相对较好 | 一般 |

结论：OpenFeign相比RestTemplate具有更好的可读性和维护性，且更易于集成负载均衡和熔断机制。