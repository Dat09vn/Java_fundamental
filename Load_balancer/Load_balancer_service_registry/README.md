# Spring Cloud Load Balancer with Eureka

This project demonstrates a simple microservice architecture where:

- `order-service` receives order requests and calls the payment service
- `payment-service` handles payment processing
- `eureka-server` acts as the Service Discovery registry

The `order-service` calls `http://payment-service/api/payment` via Spring Cloud LoadBalancer, and the actual payment instance is discovered dynamically from Eureka.

## Project structure

```text
Load_balancer_service_registry/
├── pom.xml
├── eureka-server/
│   ├── pom.xml
│   └── src/main/java/com/example/eureka/
│       └── EurekaServerApplication.java
├── order-service/
│   ├── pom.xml
│   └── src/main/java/com/example/order/
│       ├── OrderConfig.java
│       ├── OrderController.java
│       ├── OrderServiceApplication.java
│       ├── PaymentClient.java
│       └── PaymentResponse.java
├── payment-service/
│   ├── pom.xml
│   └── src/main/java/com/example/payment/
│       ├── PaymentController.java
│       └── PaymentServiceApplication.java
└── README.md
```

## How it works

1. `eureka-server` starts at `http://localhost:8761`
2. `payment-service` registers itself to Eureka with name `payment-service`
3. `order-service` reads the list of available payment instances from Eureka
4. Spring Cloud LoadBalancer picks one available instance and sends the request

## Run the project

### 1. Build the project

```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Load_balancer_service_registry
mvn clean install
```

### 2. Start Eureka Server

Terminal 1:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Load_balancer_service_registry/eureka-server
mvn spring-boot:run
```

### 3. Start payment instances

Terminal 2:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Load_balancer_service_registry/payment-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081 --instance.id=payment-instance-1"
```

Terminal 3:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Load_balancer_service_registry/payment-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082 --instance.id=payment-instance-2"
```

### 4. Start order service

Terminal 4:
```bash
cd /home/dat123/Desktop/Java_123/Load_balancer/Load_balancer_service_registry/order-service
mvn spring-boot:run
```

### 5. Test the API

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

- This demo uses Spring Cloud Eureka for Service Discovery.
- Client-side load balancing is still handled by Spring Cloud LoadBalancer.
- The order service no longer contains a static list of payment instances.
