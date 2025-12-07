#!/bin/bash
echo "启动所有服务..."
docker-compose up -d
echo "等待服务启动..."
sleep 30
echo "检查 Nacos 控制台..."
curl http://localhost:8848/nacos/
echo "检查服务注册情况..."
curl -X GET "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=catalog-service"
echo "测试服务调用..."
for i in {1..10}; do
 echo "第 $i 次请求:"
 curl http://localhost:8082/api/enrollments/test
done
echo "查看容器状态..."
docker-compose ps