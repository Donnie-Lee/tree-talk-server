FROM openjdk:17-jdk-slim

WORKDIR /app

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 复制Maven包装器和配置
COPY mvnw .
COPY .mvn .mvn

# 复制Maven依赖文件
COPY pom.xml .

# 下载依赖
RUN ./mvnw dependency:go-offline -B

# 复制源代码
COPY src src

# 构建应用
RUN ./mvnw clean package -DskipTests

# 创建运行用户
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

# 设置工作目录权限
RUN chown -R appuser:appgroup /app

USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", "-jar", "target/tree-hole-server-1.0.0.jar"]