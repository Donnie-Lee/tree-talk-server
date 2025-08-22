# 心语 TreeTalk - 轻心理疗愈应用服务端

一个基于Spring Boot的微心理疗愈应用服务端，提供情绪记录、日记、AI聊天等功能。

## 功能特性
- **用户管理**: 用户注册、登录、JWT认证
- **情绪记录**: 记录每日情绪状态，支持情绪分析和统计
- **日记功能**: 创建、编辑、删除个人日记
- **AI聊天**: 与AI助手进行心理健康对话
- **用户资料**: 管理个人设置和偏好
- **数据统计**: 情绪趋势分析、使用统计

## 技术栈

- **后端框架**: Spring Boot 3.2+
- **数据库**: PostgreSQL (主数据库) + Redis (缓存) + MongoDB (日志)
- **安全**: Spring Security + JWT
- **文档**: OpenAPI 3.0 (Swagger)
- **容器化**: Docker + Docker Compose
- **构建工具**: Maven

## 快速开始

### 环境要求

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Redis 7+
- MongoDB 7+
- Docker & Docker Compose (推荐)

### 方式一：使用Docker Compose (推荐)

```bash
# 克隆项目
git clone <repository-url>
cd tree_hole_server

# 启动所有服务
docker-compose up -d

# 查看日志（可选）
docker-compose logs -f app
```

### 方式二：本地运行

1. **安装依赖服务**
   ```bash
   docker-compose up -d postgres redis mongodb
   ```

2. **配置环境变量（可选）**
   ```bash
   export DB_USERNAME=treetalk
   export DB_PASSWORD=treetalk123
   export MONGODB_URI=mongodb://treetalk:treetalk123@localhost:27017/treetalk
   export REDIS_HOST=localhost
   ```

3. **构建和运行**
   ```bash
   ./mvnw clean package -DskipTests
   java -jar target/tree-hole-server-1.0.0.jar
   ```
   
4. **访问API文档**
   ```
   http://localhost:8080/doc.html
   ```

### 构建和部署

```bash
# 构建项目
./mvnw clean package -DskipTests

# 构建Docker镜像
docker build -t tree-hole-server .

# 运行容器
docker run -p 8080:8080 tree-hole-server
```

## API接口

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录

### 情绪记录接口
- `POST /api/emotion/record` - 创建情绪记录
- `GET /api/emotion/today` - 获取今日情绪记录
- `GET /api/emotion/records` - 按日期范围查询情绪记录

### 日记接口
- `POST /api/diary` - 创建日记
- `GET /api/diary` - 获取所有日记
- `GET /api/diary/{diaryId}` - 获取指定日记
- `PUT /api/diary/{diaryId}` - 更新日记
- `DELETE /api/diary/{diaryId}` - 删除日记

### 聊天接口
- `POST /api/chat/session` - 创建聊天会话
- `GET /api/chat/sessions` - 获取用户会话列表
- `POST /api/chat/session/{sessionId}/message` - 发送消息
- `GET /api/chat/session/{sessionId}/messages` - 获取会话消息

### 用户资料接口
- `GET /api/profile` - 获取用户资料
- `PUT /api/profile` - 更新用户资料
- `POST /api/profile/check-in` - 每日签到

## 项目结构

```
tree_hole_server/
├── src/main/java/com/treetalk/
│   ├── TreeHoleApplication.java    # 主启动类
│   ├── config/                     # 配置类
│   ├── controller/                 # REST控制器
│   ├── dto/                        # 数据传输对象
│   ├── entity/                     # 实体类
│   ├── repository/                 # 数据访问层
│   ├── security/                   # 安全配置
│   ├── service/                    # 业务逻辑层
│   └── util/                       # 工具类
├── src/main/resources/
│   ├── application.yml            # 配置文件
│   └── application-docker.yml     # Docker环境配置
├── sql/                           # 数据库脚本
├── docker-compose.yml             # Docker Compose配置
├── Dockerfile                     # Docker镜像配置
└── pom.xml                        # Maven配置
```

## 开发指南

### 代码规范
- 遵循Java编码规范
- 使用Lombok简化代码
- 添加必要的注释和文档

### 分支策略
- `main`: 主分支，稳定版本
- `develop`: 开发分支
- `feature/*`: 功能分支

### 测试
```bash
# 运行单元测试
./mvnw test

# 运行集成测试
./mvnw verify
```

## 环境配置

### 开发环境
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tree_hole_db
    username: postgres
    password: postgres
  
logging:
  level:
    com.treetalk: DEBUG
```

### 生产环境
```yaml
# application-prod.yml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
logging:
  level:
    com.treetalk: INFO
```

## 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目维护者: TreeTalk Team
- 邮箱: support@treetalk.com
- 项目主页: https://github.com/treetalk/tree-hole-server