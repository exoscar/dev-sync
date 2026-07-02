# 🚀 DevSync Backend

> 🚀 Current Release: **v1.0.0 - Production-Ready Collaboration Platform**
>
> A production-inspired **Jira-like Project Management Backend** built with **Java 25 + Spring Boot**, focused on real-world backend architecture, security, collaboration, analytics, and scalable design.

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-red)
![Architecture](https://img.shields.io/badge/Architecture-Layered-success)

---

# 🧠 Overview

DevSync is a multi-tenant collaborative workspace and issue tracking platform inspired by Jira.

The project was built to practice production-grade backend engineering and real-world software design.

### Core Capabilities

- 🔐 Authentication & Authorization
- 🏢 Multi-Workspace Collaboration
- 📁 Project Management
- 🎯 Issue Tracking
- 💬 Team Collaboration
- 🔔 Event-Driven Notifications
- ✉️ Email Notifications
- 📎 File Attachments
- 🔍 Global Search
- 📊 Dashboards & Analytics
- 📝 Activity Auditing
- 📖 API Documentation
- 🐳 Dockerized Deployment
- ⚡ Production-Oriented Architecture

This is not a CRUD demo project. The focus is on scalable architecture, security, maintainability, and backend engineering best practices.

---

# 🛠️ Tech Stack

### Backend

- ☕ Java 25
- 🍃 Spring Boot 3
- 🛡️ Spring Security
- 🔑 JWT Authentication
- 🐘 PostgreSQL
- 📦 Spring Data JPA / Hibernate
- 🔍 JPA Specifications
- 📖 SpringDoc OpenAPI (Swagger)
- 🚀 Maven
- 🧰 Lombok

### Search

- PostgreSQL Full Text Search (FTS)
- `tsvector`
- `ts_rank`
- `websearch_to_tsquery`

### Deployment

- Docker
- Docker Compose
- Docker Volumes

---

# 🏗️ Architecture

The project follows a layered architecture with clear separation of concerns.

```text
Controller
    ↓
Service
    ↓
Access / Authorization / Validation Services
    ↓
Repository
    ↓
Database
```

### Design Patterns & Architectural Concepts

- ✅ Service Layer
- ✅ Repository Pattern
- ✅ DTO Mapping
- ✅ Context Objects
- ✅ Access Services
- ✅ Authorization Services
- ✅ Validation Services
- ✅ Event-Driven Architecture
- ✅ Domain Events
- ✅ Observer Pattern
- ✅ Global Exception Handling

---

# 🔐 Authentication & Security

### Features

- ✅ User Registration
- ✅ Login
- ✅ JWT Access Tokens
- ✅ JWT Refresh Tokens
- ✅ Refresh Token Persistence
- ✅ Refresh Token Revocation
- ✅ Logout
- ✅ BCrypt Password Hashing
- ✅ Role-Based Authorization (RBAC)
- ✅ Global Exception Handling

---

# 🏢 Workspace Management

### Roles

```text
OWNER
 └─ MAINTAINER
      └─ MEMBER
           └─ VIEWER
```

### Features

- ✅ Create Workspace
- ✅ View Workspace
- ✅ List User Workspaces
- ✅ Invite Members
- ✅ Update Member Roles
- ✅ Remove Members
- ✅ Workspace Access Validation

---

# 📁 Project Management

### Features

- ✅ Create Project
- ✅ Get Projects
- ✅ Get Project
- ✅ Update Project
- ✅ Delete Project

### Business Rules

- 🔒 Project belongs to a Workspace
- 🔒 Project cannot be moved between Workspaces
- 🔒 Duplicate project names are not allowed inside the same Workspace

---

# 🎯 Issue Management

### Statuses

- TODO
- IN_PROGRESS
- DONE

### Priorities

- LOW
- MEDIUM
- HIGH
- CRITICAL

### Features

- ✅ Create Issue
- ✅ View Issue
- ✅ List Issues
- ✅ Update Issue
- ✅ Delete Issue
- ✅ Change Status
- ✅ Change Priority
- ✅ Assign User

---

# 🔍 Filtering, Sorting & Pagination

Implemented using **JPA Specifications**.

### Supported Filters

- Status
- Priority
- Assignee

### Features

- ✅ Dynamic Filtering
- ✅ Pagination
- ✅ Sorting

---

# 🔍 Global Search

Implemented using **PostgreSQL Full Text Search (FTS)**.

### Search Targets

- Issues
- Projects
- Users

### Features

- ✅ Workspace Scoped Search
- ✅ Relevance Ranking
- ✅ PostgreSQL Full Text Search
- ✅ Top Results Aggregation
- ✅ Fast Search Queries

### PostgreSQL Concepts Used

- to_tsvector()
- websearch_to_tsquery()
- ts_rank()
- GIN Indexes
- Search Vectors

---

# 💬 Comment System

### Features

- ✅ Create Comment
- ✅ View Comments
- ✅ Update Comment
- ✅ Delete Comment

### Rules

- 👤 Authors can edit/delete their own comments
- 🛡️ OWNER and MAINTAINER can manage comments

---

# 🤝 Collaboration Features

## 🏷️ Labels

- ✅ Workspace-Level Labels
- ✅ Create Labels
- ✅ Update Labels
- ✅ Archive Labels
- ✅ Assign Labels to Issues
- ✅ Remove Labels from Issues
- ✅ View Issue Labels

## 👀 Watchers

- ✅ Watch Issues
- ✅ Unwatch Issues
- ✅ Automatic Creator Watchers
- ✅ Automatic Assignee Watchers
- ✅ View Watchers

## 🔔 Notifications

### Event-Driven Notifications

Implemented using:

- ApplicationEventPublisher
- @TransactionalEventListener
- AFTER_COMMIT Processing

### Supported Notifications

- Issue Assigned
- Issue Status Changed
- Issue Priority Changed
- Comment Added
- Label Added
- Label Removed

### Features

- ✅ Notification Feed
- ✅ Workspace Notifications
- ✅ Project Notifications
- ✅ Unread Count
- ✅ Mark Read
- ✅ Mark All Read

## ✉️ Email Notifications

### Features

- ✅ Issue Assignment Email Notifications
- ✅ Issue Status Change Email Notifications
- ✅ HTML Email Templates
- ✅ JavaMailSender Integration
- ✅ SMTP Configuration
- ✅ Recipient Targeting via Events

### Technical Concepts

- Spring Mail
- JavaMailSender
- MimeMessage
- MimeMessageHelper
- SMTP
- Event-Driven Email Delivery

## 📎 Attachments

- ✅ Upload Attachments
- ✅ Download Attachments
- ✅ Delete Attachments
- ✅ List Attachments
- ✅ File Type Validation
- ✅ File Size Validation
- ✅ Local File Storage
- ✅ Storage Abstraction Layer

---

# 📝 Activity Tracking

### Activity Types

- ISSUE_CREATED
- ISSUE_ASSIGNED
- ISSUE_STATUS_CHANGED
- ISSUE_PRIORITY_CHANGED
- ISSUE_UPDATED
- ISSUE_DELETED
- COMMENT_ADDED
- COMMENT_UPDATED
- COMMENT_DELETED

### Features

- ✅ Automatic Activity Recording
- ✅ Issue Activity Timeline
- ✅ Workspace Activity Feed

---

# 📊 Dashboard & Analytics

## 🏢 Workspace Dashboard

- Total Projects
- Total Members
- Total Issues
- Issue Status Distribution

## 📁 Project Dashboard

- Total Issues
- Status Distribution
- Priority Distribution
- Assigned vs Unassigned Issues

## 👤 My Assigned Issues

- Pagination
- Filtering
- Sorting

## 📈 Workspace Member Statistics

- Assigned Issues
- Completed Issues
- Completion Rate

---

# 🔑 Authorization Model

| Action | OWNER | MAINTAINER | MEMBER | VIEWER |
|----------|----------|----------|----------|----------|
| Manage Workspace | ✅ | ❌ | ❌ | ❌ |
| Manage Members | ✅ | ✅ | ❌ | ❌ |
| Manage Projects | ✅ | ✅ | ❌ | ❌ |
| Manage Issues | ✅ | ✅ | Limited | ❌ |
| Read Data | ✅ | ✅ | ✅ | ✅ |

---

# 📖 API Documentation

Interactive API documentation is available through Swagger/OpenAPI.

### Features

- ✅ Interactive API Testing
- ✅ JWT Authentication Support
- ✅ Endpoint Documentation
- ✅ Request / Response Schemas

```text
/swagger-ui/index.html
```

---

# 🐳 Docker Support

### Features

- ✅ Dockerized Spring Boot Application
- ✅ PostgreSQL Container
- ✅ Docker Compose Setup
- ✅ Environment Variable Configuration
- ✅ Persistent Database Volumes
- ✅ Container Networking
- ✅ Production-Like Local Deployment

### Deployment

```bash
docker compose up -d
```

---

# 🌐 API Highlights

### Authentication

```http
POST /auth/register
POST /auth/login
POST /auth/refresh
POST /auth/logout
```

### Dashboards

```http
GET /workspaces/{workspaceId}/dashboard
GET /projects/{projectId}/dashboard
GET /projects/{projectId}/issues/my
GET /workspaces/{workspaceId}/activity-feed
GET /workspaces/{workspaceId}/member-statistics
```

---

# 🧩 Engineering Concepts Demonstrated

- 🔐 JWT Authentication
- 🔄 Refresh Token Strategy
- 🛡️ RBAC Authorization
- 🏢 Multi-Tenant Design
- 📊 Aggregation Queries
- 📝 Audit Logging
- 🔍 JPA Specifications
- 🔍 PostgreSQL Full Text Search
- 📄 DTO Mapping
- ⚡ Service Decomposition
- 🧠 Clean Architecture Principles
- 📈 Dashboard Analytics
- 📎 Multipart File Uploads
- 🔔 Event-Driven Architecture
- 🎯 Domain Events
- 👀 Observer Pattern
- 📖 OpenAPI / Swagger
- 🐳 Docker & Docker Compose
- 🌐 Container Networking
- 💾 Persistent Volumes
- ✉️ SMTP Email Integration
- 🚀 Production-Oriented Design

---

# 📍 Current Status

## ✅ Phase 1 — Core Platform

Completed

## ✅ Phase 2 — Dashboard & Analytics

Completed

## ✅ Phase 3 — Collaboration Features

Completed

### Delivered Features

- Labels
- Watchers
- Event-Driven Notifications
- Email Notifications
- File Attachments
- Global Search
- Swagger/OpenAPI Documentation

---

# 🔮 Roadmap

## Phase 4 — Production Readiness

- ✅ Global Search
- ✅ Docker Support
- ✅ Email Notifications
- 🧪 Integration Testing
- 📊 Advanced Reporting

## Phase 5 — Scalability & Performance

- ⚡ Redis Caching
- 🔐 JWT Blacklisting
- 📊 Dashboard Caching
- 🔔 Notification Counter Caching
- 🚀 Async Processing

## Future Enhancements

- 📡 WebSocket Notifications
- 📨 Kafka Integration
- ☁️ Cloud Deployment
- 🏗️ Microservices Exploration

---

# 👨‍💻 Author

Built as a portfolio project to practice production-grade backend engineering using Spring Boot and modern Java.

⭐ If you found this project interesting, consider giving it a star.