# 開發環境設置指南

## 環境需求
- JDK 17+
- Maven
- Docker & Docker Compose
- IDE (推薦使用 IntelliJ IDEA 或 VS Code)

## 數據庫設置

### 使用 Docker 啟動 PostgreSQL（生產環境）
1. 確保 Docker 和 Docker Compose 已安裝
2. 在專案根目錄執行：
```bash
docker-compose up -d
```
這將啟動 PostgreSQL 16 容器，配置如下：
- Port: 5433 (外部訪問端口)
- Database: ticket_booking
- Username: postgres
- Password: 123456

### 驗證數據庫連接
使用 DBeaver 或其他數據庫管理工具連接：
- Host: localhost
- Port: 5433
- Database: ticket_booking
- Username: postgres
- Password: 123456

## 啟動應用程序

### 開發環境（使用 H2 內存數據庫）
```bash
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
如果是用jar啟動服務，才會使用 spring.profiles.active=dev
```

### 生產環境（使用 PostgreSQL）
```bash
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
如果是用jar啟動服務，才會使用 spring.profiles.active=prod
```

## 訪問應用程序

### API 文檔
- Swagger UI: http://localhost:8080/swagger-ui.html
- API 文檔: http://localhost:8080/v3/api-docs

### 開發環境工具
- H2 Console (僅開發環境): http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:ticket_booking
  - Username: sa
  - Password: (留空)

## 配置文件說明

### 應用程序配置
- `application.yml`: 共用配置
- `application-dev.yml`: 開發環境配置（H2 數據庫）
- `application-prod.yml`: 生產環境配置（PostgreSQL）

### Docker 配置
- `docker-compose.yml`: 定義 PostgreSQL 容器配置

## 常見問題處理

### 端口衝突
如果 5433 端口被占用，可以在 `docker-compose.yml` 中修改端口映射：
```yaml
ports:
  - "新端口:5432"
```
記得同時更新 `application-prod.yml` 中的數據庫連接 URL。

### 數據庫連接問題
1. 確保 Docker 容器正在運行：
```bash
docker ps
```
2. 查看容器日誌：
```bash
docker logs ticket_booking_db
```

## 開發工具建議

### IDE 插件
- Lombok
- Spring Boot
- Docker
- Maven

### 推薦的開發工具
- DBeaver: 數據庫管理
- Postman: API 測試
- Docker Desktop: 容器管理 