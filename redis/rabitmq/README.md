# RabbitMQ Tutorial với Spring Boot

Dự án này là ví dụ mẫu để học RabbitMQ theo hướng thực tế với Spring Boot.

## 1. Kiến trúc ví dụ

- Producer: gửi tin nhắn tới RabbitMQ
- Exchange: `demo.exchange`
- Queue: `demo.queue`
- Routing key: `demo.routing`
- Consumer: lắng nghe queue và ghi lại tin nhắn cuối cùng
- Controller: API để gửi tin nhắn và xem tin nhắn cuối cùng

## 2. Cấu trúc project

- `src/main/java/com/example/rabitmq/config/RabbitMQConfig.java` - cấu hình queue, exchange, binding
- `src/main/java/com/example/rabitmq/service/RabbitMQProducer.java` - gửi tin nhắn
- `src/main/java/com/example/rabitmq/service/RabbitMQConsumer.java` - nhận tin nhắn
- `src/main/java/com/example/rabitmq/controller/MessageController.java` - API
- `src/main/resources/application.properties` - cấu hình RabbitMQ

## 3. Cài đặt RabbitMQ

### Cách nhanh nhất: dùng Docker

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Sau khi chạy:

- RabbitMQ port: `5672`
- Management UI: `http://localhost:15672`
- Tài khoản mặc định: `guest` / `guest`

## 4. Chạy ứng dụng Spring Boot

```bash
./gradlew bootRun
```

## 5. Gửi tin nhắn thử

### Gửi bằng cURL

```bash
curl -X POST http://localhost:8080/api/send \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello RabbitMQ from Spring Boot"}'
```

### Xem tin nhắn cuối cùng

```bash
curl http://localhost:8080/api/last-message
```

## 6. Kiểm tra trong RabbitMQ UI

Mở trình duyệt:

```text
http://localhost:15672
```

Đăng nhập bằng `guest` / `guest`.

Bạn sẽ thấy:

- queue `demo.queue`
- message đã được đưa vào queue
- consumer nhận và xử lý tin nhắn

## 7. Giải thích ngắn gọn

RabbitMQ là hệ thống message broker, hoạt động theo model:

```text
Producer -> Exchange -> Queue -> Consumer
```

- Producer: nơi phát tin nhắn
- Exchange: định tuyến tin nhắn
- Queue: nơi lưu trữ tạm thời
- Consumer: người nhận và xử lý

## 8. Bài học quan trọng để hiểu RabbitMQ

### a) Queue
Queue là nơi lưu tin nhắn chờ xử lý. Khi consumer chạy, nó sẽ lấy tin từ queue.

### b) Exchange
Exchange quyết định gửi message tới queue nào dựa trên binding và routing key.

### c) Routing key
Routing key là nhãn dùng để exchange định tuyến tin nhắn.

### d) Durable queue
Queue có `true` trong `new Queue(queueName, true)` nghĩa là queue được lưu bền bỉ trên disk.

### e) Consumer
Consumer sử dụng `@RabbitListener` để lắng nghe queue và xử lý message.

## 9. Ví dụ thông dụng trong thực tế

- Email queue
- SMS queue
- Log queue
- Notification service
- Job processing background
- Event-driven architecture

## 10. Bài tập gợi ý

1. Thay đổi queue name và routing key
2. Tạo 2 consumer cùng nghe 1 queue
3. Thử gửi 10 tin nhắn và xem thứ tự xử lý
4. Tạo topic exchange khác cho nhiều loại message
5. Tìm hiểu `Work Queue`, `Publish/Subscribe`, `Routing`, `Topics`

## 11. Một số câu hỏi thường gặp

### RabbitMQ không chạy thì sao?
Kiểm tra container hoặc service RabbitMQ đã chạy chưa.

### Không nhận được tin nhắn?
- Kiểm tra `application.properties`
- Kiểm tra exchange / queue / routing key
- Kiểm tra log của Spring Boot

### Không thấy hàng trong UI?
Thường là do queue chưa được bind với exchange hoặc routing key không khớp.

## 12. Kết luận

Ví dụ này là nền tảng cơ bản nhất để học RabbitMQ với Spring Boot. Sau khi nắm vững kiến thức này, bạn có thể mở rộng sang:

- work queue
- fanout exchange
- topic exchange
- dead letter queue
- retry và timeout
- tích hợp microservices

Nếu bạn muốn, tôi có thể tiếp tục viết cho bạn:

1. ví dụ RabbitMQ theo mô hình Work Queue
2. ví dụ Publish/Subscribe
3. ví dụ Dead Letter Queue
4. ví dụ Spring Boot + RabbitMQ + Redis
