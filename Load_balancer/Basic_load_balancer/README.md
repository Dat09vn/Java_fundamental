# Spring Cloud Load Balancer Demo

This project demonstrates a simple microservice-style architecture with two separate services:

- `order-service`: receives order requests and calls the payment service
- `payment-service`: processes payment requests

The `order-service` uses Spring Cloud LoadBalancer to distribute requests across multiple `payment-service` instances.

## Project structure

```text
Basic_load_balancer/
├── pom.xml
├── order-service/
│   ├── pom.xml
│   └── src/main/java/com/example/order/
│       ├── OrderServiceApplication.java
│       ├── OrderController.java
│       ├── OrderConfig.java
│       ├── PaymentClient.java
│       └── PaymentResponse.java
├── payment-service/
│   ├── pom.xml
│   └── src/main/java/com/example/payment/
│       ├── PaymentServiceApplication.java
│       └── PaymentController.java
└── README.md
```

## How it works

1. `order-service` exposes an endpoint:
   - `GET /api/orders/checkout`
2. When the endpoint is called, it calls:
   - `http://payment-service/api/payment`
3. In `OrderConfig`, two payment instances are registered:
   - `localhost:8081`
   - `localhost:8082`
4. Spring Cloud LoadBalancer selects one instance for each request.

## Run the project

### 1. Build the project

```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Basic_load_balancer
mvn clean install
```

### 2. Start payment instances

Terminal 1:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Basic_load_balancer/payment-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081 --instance.id=payment-instance-1"
```

Terminal 2:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Basic_load_balancer/payment-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082 --instance.id=payment-instance-2"
```

### 3. Start order service

Terminal 3:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Basic_load_balancer/order-service
mvn spring-boot:run
```

### 4. Test the API

```bash
curl http://localhost:8080/api/orders/checkout
```

Example response:

```json
{
  "orderId": "...",
  "status": "PAID",
  "payment": {
    "message": "Payment processed",
    "serviceName": "payment-service",
    "instanceId": "payment-instance-2",
    "port": 8082,
    "status": "SUCCESS"
  }
}
```

## Notes

- This demo is intentionally simple and does not use Eureka or API Gateway.
- It focuses on the concept of service-to-service communication and client-side load balancing.
- The load balancing is handled by Spring Cloud LoadBalancer using a custom list of payment instances.
