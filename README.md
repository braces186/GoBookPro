# 票務預訂平台 (Ticket Booking Platform)

## 📋 專案概述
這是一個模擬商業票務預訂平台，提供場地預訂、付款整合、通知發送等功能。專案採用微服務架構，將逐步從單體架構演進到分散式系統。

## 🎯 專案目標
- 建立一個完整的票務預訂系統
- 展示從單體到微服務架構的演進過程
- 提供良好的用戶體驗和後台管理功能

## 🛠 技術架構

### 階段一：MVP 基礎功能（單體架構）
- **後端技術**
  - Java 17+
  - Spring Boot 3.2.3
  - PostgreSQL
  - Spring Security + JWT
  - Swagger / OpenAPI

- **前端技術**
  - Vue 3
  - Vite

## 📦 功能模組
- 使用者管理
  - 註冊/登入
  - 角色區分（User / Merchant）
- 場地管理
  - 新增場地
  - 設定時段與價格
- 預訂系統
  - 瀏覽場地
  - 建立預訂
  - 訂單狀態管理

## 🚀 開發指南

詳細的開發環境設置和啟動說明請參考 [DEVELOPMENT.md](DEVELOPMENT.md)

### 環境需求
- JDK 17+
- Node.js 16+
- Docker & Docker Compose
- PostgreSQL 16 (可選，也可使用 Docker)

### 專案結構
```
ticket-booking-platform/
├── backend/           # 後端專案
├── frontend/          # 前端專案
└── docs/             # 文件
```

### 開發流程
1. 設定開發環境
2. 建立資料庫
3. 啟動後端服務
4. 啟動前端服務

## 📝 授權
MIT License 