# Mimari

```mermaid
flowchart LR
  UI[React SPA] --> API[Spring MVC /api/v1]
  API --> IAM[Identity + Organization]
  API --> FLOW[Request + Inspection + Approval]
  FLOW --> OUT[Project + Activity]
  FLOW --> AUDIT[Audit + Notification]
  FLOW --> PG[(PostgreSQL + PostGIS)]
  API --> S3[(MinIO/S3)]
  API --> MAIL[Mailpit/SMTP]
```

Backend, domain sınırları paketlerle ayrılmış modüler monolittir. Controller'lar DTO kabul eder; iş akışları application servislerinde, kalıcılık infrastructure repository'lerinde bulunur. Modüller repository paylaşmaz. Birim kapsamı `UnitScopeService` üzerinden her sorguya uygulanır.

## ER özeti

```mermaid
erDiagram
  ORG_UNIT ||--o{ APP_USER : contains
  ORG_UNIT ||--o{ CITY_REQUEST : owns
  CITY_REQUEST ||--o{ REQUEST_TRANSITION : records
  CITY_REQUEST ||--o{ REQUEST_ASSIGNMENT : records
  CITY_REQUEST ||--o{ INSPECTION : has
  CITY_REQUEST ||--o{ APPROVAL_INSTANCE : requests
  CITY_REQUEST }o--o{ PROJECT_CANDIDATE : converts
  CITY_REQUEST ||--o{ ACTIVITY : converts
  APP_USER ||--o{ AUDIT_LOG : performs
```

