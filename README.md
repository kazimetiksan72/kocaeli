# Kocaeli Şehir Yapı Kontrol, Proje İzleme ve Yönetim Sistemi

Sürüm 1 operasyonel MVP. Uygulama; talebin kaydı, konumlandırılması, atanması, saha keşfi, sıralı onay ve proje adayı/faaliyete dönüşüm akışını kapsar.

## Hızlı başlangıç

```bash
cp .env.example .env
docker compose up --build
```

- Uygulama: http://localhost:5173
- API / Swagger: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health/readiness
- Mailpit: http://localhost:8025
- MinIO: http://localhost:9001

Mock giriş ekranında demo kullanıcı seçilir. Varsayılan yönetici `admin`, teknik kullanıcı `teknik.izmit`, yönetici `mudur.izmit`, diğer birim kullanıcısı `teknik.gebze` hesabıdır. Mock giriş `MOCK_AUTH_ENABLED=false` ile tamamen kapanır.

## Yerel geliştirme

Java 21 ve Node 22+ gerekir. PostgreSQL/PostGIS çalışırken:

```bash
cd backend && ./mvnw spring-boot:run
cd frontend && npm ci && npm run dev
```

## Test

```bash
make test
cd frontend && npx playwright install chromium && npm run e2e
```

Testcontainers testleri Docker gerektirir. Kabul senaryosu [docs/ACCEPTANCE.md](docs/ACCEPTANCE.md), mimari belgeler [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md), bilinen sınırlar [docs/KNOWN_LIMITATIONS.md](docs/KNOWN_LIMITATIONS.md) içindedir.

