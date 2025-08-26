FROM openjdk:17-jdk-slim

WORKDIR /app
##环境设置
ENV JAR_FILE="tree-hole-server-1.0.0.jar"
#运行目录
COPY ./target/${JAR_FILE} /app
# 声明时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
# 创建日志目录
RUN mkdir -p /app/logs && chmod 755 /app/logs

#声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "tree-hole-server-1.0.0.jar"]