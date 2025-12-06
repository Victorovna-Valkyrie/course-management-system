# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制 Maven 配置文件
COPY pom.xml .

# 复制源代码
COPY src ./src

# 执行 Maven 构建，跳过测试
RUN mvn clean package -DskipTests

# 多阶段构建 - 运行阶段
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 JAR 文件
COPY --from=builder /app/target/enrollment-service-0.0.1-SNAPSHOT.jar app.jar

# 创建非 root 用户
RUN addgroup -S appgroup && \
    adduser -S appuser -G appgroup

# 更改 JAR 文件所有权
RUN chown appuser:appgroup app.jar

# 切换到非 root 用户
USER appuser

# 暴露端口
EXPOSE 8082

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]