# RabbitMQ Java: hai service

Ví dụ học RabbitMQ với Spring Boot và Gradle multi-project:

- `producer-service`: HTTP `POST /messages` ở cổng `8081`, publish JSON vào exchange `learning.events` với routing key `message.created`.
- `consumer-service`: chạy ở cổng `8082`, lắng nghe queue `learning.messages` và in message ra log.

## Chạy RabbitMQ

```bash
docker compose up -d
```

RabbitMQ Management UI: http://localhost:15672 (user: `guest`, password: `guest`).

## Chạy hai service

Mở hai terminal:

```bash
./gradlew :consumer-service:bootRun
```

```bash
./gradlew :producer-service:bootRun
```

Gửi message:

```bash
curl -X POST http://localhost:8081/messages \
  -H 'Content-Type: application/json' \
  -d '{"text":"Hello from producer"}'
```

Trong log của `consumer-service` sẽ có:

```text
Received message from RabbitMQ: Hello from producer
```

Có thể đổi kết nối RabbitMQ bằng `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME` và `RABBITMQ_PASSWORD`.
