# Distributed Tracing & Observability Demo with Spring Boot, OTel, Jaeger, Loki and Grafana

Dự án này là một ví dụ thực tế về distributed tracing và observability trong hệ thống microservice bằng Spring Boot 4 + OpenTelemetry + Jaeger + Loki + Grafana.

Mục tiêu:
- Hiển thị chuỗi request trong hệ thống multi-service
- Gửi trace tới Jaeger qua OTLP
- Gửi log tới Loki qua Promtail
- Giám sát log và trace trên Grafana
- Truyền trace context giữa các service để xem toàn bộ hành trình request

---

## 1. Tổng quan kiến trúc

Hệ thống gồm 3 service:

- order-service: service gốc, chạy trên port 8081
- gateway-service: service entrypoint, chạy trên port 8080
- payment-service: service thanh toán, chạy trên port 8082

Chuỗi request mẫu:

- Client gọi `GET /gateway?userId=u123`
- `gateway-service` tạo span `gateway.createOrder`
- `gateway-service` gọi `order-service` qua HTTP
- `order-service` tạo span `order.controller.createOrder`
- `order-service` gọi `payment-service`
- `payment-service` tạo span `payment.process`
- Toàn bộ trace được export lên Jaeger qua OTLP
- Log của ứng dụng được ghi vào file `/tmp/spring-boot.log` và được Promtail đưa lên Loki

```text
Client
  |
  v
gateway-service:8080 /gateway
  |
  +--> order-service:8081 /orders
         |
         +--> payment-service:8082 /payments
```

---

## 2. Công nghệ sử dụng

- Java 17
- Spring Boot 4.1.1
- Gradle multi-project
- OpenTelemetry SDK
- Micrometer Tracing + OTel bridge
- Jaeger all-in-one
- Loki + Promtail
- Grafana
- Prometheus
- Logback + Logstash JSON encoder

---

## 3. Cấu trúc project

```text
logging/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── README.md
├── docker-compose.yml
├── docker/
│   ├── prometheus.yml
│   ├── promtail-config.yml
│   └── grafana/
│       └── provisioning/
│           └── datasources/
│               └── loki.yml
├── src/
│   ├── main/
│   │   ├── java/com/example/logging/
│   │   │   ├── LoggingApplication.java
│   │   │   ├── OrderController.java
│   │   │   ├── OrderService.java
│   │   │   └── ...
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback-spring.xml
│   └── test/java/com/example/logging/
├── gateway-service/
│   ├── build.gradle
│   ├── src/main/java/com/example/gateway/
│   │   ├── GatewayApplication.java
│   │   ├── GatewayController.java
│   │   ├── RestClientConfig.java
│   │   └── TelemetryConfig.java
│   └── src/main/resources/
│       ├── application.properties
│       └── logback-spring.xml
├── payment-service/
│   ├── build.gradle
│   ├── src/main/java/com/example/payment/
│   │   ├── PaymentApplication.java
│   │   ├── PaymentController.java
│   │   ├── RestClientConfig.java
│   │   └── TelemetryConfig.java
│   └── src/main/resources/
│       ├── application.properties
│       └── logback-spring.xml
└── gradle/wrapper/
```

---

## 4. Yêu cầu môi trường

Trước khi chạy, bạn cần:

- Java 17
- Gradle (hoặc dùng `./gradlew` sẵn có)
- Docker + Docker Compose

Kiểm tra phiên bản:

```bash
java -version
docker --version
docker compose version
```

---

## 5. Khởi động stack observability (Jaeger, Loki, Prometheus, Grafana)

Từ root project:

```bash
cd /home/dat123/Desktop/Java_123/logging
docker compose up -d
```

Các port chính:

- Jaeger UI: http://localhost:16686
- OTLP gRPC: localhost:4317
- OTLP HTTP: localhost:4318
- Loki: http://localhost:3100
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

Tài khoản Grafana mặc định:

- Username: `admin`
- Password: `admin`

---

## 6. Khởi động ứng dụng Spring Boot

Khởi động order-service:

```bash
cd /home/dat123/Desktop/Java_123/logging
./gradlew bootRun
```

Trường hợp muốn chạy riêng từng service:

```bash
./gradlew :gateway-service:bootRun
./gradlew :payment-service:bootRun
```

Lưu ý quan trọng:
- `gateway-service` nên chạy trên port 8080
- `order-service` chạy trên port 8081
- `payment-service` chạy trên port 8082

Nếu từng service chạy riêng, phải đảm bảo không có port conflict.

---

## 7. Gọi request test

Test gateway endpoint:

```bash
curl -sS 'http://localhost:8080/gateway?userId=u123'
```

Kết quả mong đợi ví dụ:

```text
Created order ORD-u123-1788064045294 | Payment successful for order ORD-u123-1788064045294
```

Test order-service trực tiếp:

```bash
curl -sS 'http://localhost:8081/orders?userId=u123'
```

Test payment-service trực tiếp:

```bash
curl -sS 'http://localhost:8082/payments?orderId=ORD-u123-123456'
```

---

## 8. Kiểm tra trace trên Jaeger

Mở trình duyệt:

- http://localhost:16686

Trong Jaeger UI:

1. Chọn service `gateway-service`
2. Chọn `Find Traces`
3. Click vào trace gần nhất
4. Xem chuỗi span:
   - `gateway.createOrder`
   - `order.controller.createOrder`
   - `payment.process`

Nếu propagation hoạt động đúng, các span sẽ có cùng `traceID` và thể hiện một request duy nhất đang đi qua các service.

---

## 9. Kiểm tra log trên Loki/Grafana

### 9.1 Grafana

- Mở: http://localhost:3000
- Login bằng `admin` / `admin`
- Trong Grafana, vào `Explore`
- Chọn datasource `Loki`
- Query ví dụ:

```logql
{job="spring-boot"}
```

Hoặc lọc theo traceId:

```logql
{job="spring-boot"} |= "traceId=a743e558188d747ee458d9c1eb28dcaa"
```

### 9.2 Loki

Loki được cấu hình để nhận log từ file `/tmp/spring-boot.log` thông qua Promtail.

File cấu hình:

- `docker/promtail-config.yml`

Promtail config cơ bản:

```yaml
server:
  http_listen_port: 9080
  grpc_listen_port: 0

clients:
  - url: http://loki:3100/loki/api/v1/push

scrape_configs:
  - job_name: spring-boot
    static_configs:
      - targets:
          - localhost
        labels:
          job: spring-boot
          __path__: /tmp/spring-boot.log
```

---

## 10. Logback và structured logging

Mỗi service sử dụng Logback với định dạng log có chứa:

- app name
- traceId
- spanId
- logger name
- message

Ví dụ log:

```text
2026-08-30 11:26:34.991 INFO [gateway-service] traceId=a743e558188d747ee458d9c1eb28dcaa spanId=e2a9c64bb84352a1 c.example.gateway.GatewayController - Gateway received request userId=u123
```

Cấu hình log trong:

- `src/main/resources/logback-spring.xml`
- `gateway-service/src/main/resources/logback-spring.xml`
- `payment-service/src/main/resources/logback-spring.xml`

Log được ghi đồng thời:
- console
- file `/tmp/spring-boot.log`

Điều này giúp Promtail đẩy log lên Loki để dashboard hóa trên Grafana.

---

## 11. OpenTelemetry và trace propagation

Mỗi service cấu hình exporter OTLP theo endpoint:

```properties
otel.exporter.otlp.endpoint=http://localhost:4318
otel.exporter.otlp.protocol=http/protobuf
```

Về mặt propagation, gateway tự tạo `traceparent` header và gửi sang upstream service:

```java
.header("traceparent", currentTraceParent())
```

Cách tạo header dựa trên hiện tại span:

```java
private String currentTraceParent() {
    Span current = Span.current();
    if (!current.getSpanContext().isValid()) {
        return "00-00000000000000000000000000000000-0000000000000000-01";
    }
    return "00-" + current.getSpanContext().getTraceId() + "-" + current.getSpanContext().getSpanId() + "-01";
}
```

Nhờ vậy, request giữa gateway → order → payment có thể thuộc cùng 1 trace, tạo ra chain rõ ràng trong Jaeger.

---

## 12. Cách chạy đầy đủ stack theo thứ tự khuyến nghị

1. Bật Docker stack:

```bash
docker compose up -d
```

2. Chạy `order-service`:

```bash
./gradlew bootRun
```

3. Chạy `gateway-service`:

```bash
./gradlew :gateway-service:bootRun
```

4. Chạy `payment-service`:

```bash
./gradlew :payment-service:bootRun
```

5. Gọi request:

```bash
curl -sS 'http://localhost:8080/gateway?userId=u123'
```

6. Mở Jaeger và Grafana để xem trace/log.

---

## 13. Troubleshooting

### 13.1 `No qualifying bean of type 'org.springframework.web.client.RestClient$Builder' available`

Nguyên nhân: controller tạo `RestClient.Builder` trực tiếp mà không có bean trong Spring context.

Cách fix: chuyển bean vào lớp config riêng, ví dụ:

```java
@Configuration
public class RestClientConfig {
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
```

### 13.2 Gateway trả 500 vì không kết nối được `localhost:8081`

Nguyên nhân: `order-service` chưa chạy hoặc port 8081 đang bị conflict.

Cách kiểm tra:

```bash
ss -lntp | grep ':8080\|:8081\|:8082' || true
```

Nếu cần, dọn process cũ:

```bash
pkill -f 'GatewayApplication|PaymentApplication|LoggingApplication' || true
```

### 13.3 Không thấy log trong Loki

Kiểm tra Promtail và file log:

```bash
ls -l /tmp/spring-boot.log
cat /tmp/spring-boot.log | tail
```

Kiểm tra container:

```bash
docker compose ps
docker compose logs promtail loki grafana
```

### 13.4 Không thấy trace trên Jaeger

Kiểm tra exporter endpoint:

```properties
otel.exporter.otlp.endpoint=http://localhost:4318
otel.exporter.otlp.protocol=http/protobuf
```

Kiểm tra Jaeger có bật OTLP:

```yaml
environment:
  COLLECTOR_OTLP_ENABLED: "true"
```

---

## 14. Kết luận

Dự án này là ví dụ đầy đủ cho việc xây dựng một hệ thống microservice có tracing và log aggregation. Bạn không chỉ xem được trace trên Jaeger, mà còn có thể truy vết log trên Grafana/Loki và tìm ra root cause của request trong môi trường gần production.

Nó phù hợp cho:
- demo observability
- lab distributed tracing
- training về microservices monitoring
- bài thuyết trình kỹ thuật về tracing/logging/metrics

---

## 15. Tài liệu tham khảo

- Spring Boot: https://spring.io/projects/spring-boot
- OpenTelemetry: https://opentelemetry.io/
- Jaeger: https://www.jaegertracing.io/
- Loki: https://grafana.com/oss/loki/
- Grafana: https://grafana.com/
- Prometheus: https://prometheus.io/

---

## 16. Quick start command summary

```bash
cd /home/dat123/Desktop/Java_123/logging
docker compose up -d
./gradlew bootRun
./gradlew :gateway-service:bootRun
./gradlew :payment-service:bootRun
curl -sS 'http://localhost:8080/gateway?userId=u123'
```

Sau đó mở:

- Jaeger: http://localhost:16686
- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090

---

Nếu bạn muốn, tôi có thể tiếp tục viết thêm:
1. phiên bản README theo kiểu project chuẩn GitHub (badge, screenshot, architecture diagram)
2. file `docker-compose.override.yml` cho môi trường dev
3. file `docs/ARCHITECTURE.md` tách riêng từng mô tả kỹ thuật
