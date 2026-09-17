.PHONY: up down test
up:
	docker compose up --build
down:
	docker compose down
test:
	cd backend && ./mvnw test
	cd frontend && npm ci && npm run test && npm run build

