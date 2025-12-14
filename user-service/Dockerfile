# 使用Maven镜像构建应用
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml文件
COPY pom.xml .

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 使用较小的JRE镜像运行应用
FROM eclipse-temurin:17-jre-alpine

# 创建非root用户
RUN addgroup -g 1001 -S valkyrie &&\
    adduser -u 1001 -S valkyrie -G valkyrie
USER valkyrie

# 设置工作目录
WORKDIR /app

# 从builder阶段复制jar文件
COPY --from=builder /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8081

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]