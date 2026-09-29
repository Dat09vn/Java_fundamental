package com.example.circuite_breaker;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final PaymentServiceClient paymentServiceClient;

    public OrderService(PaymentServiceClient paymentServiceClient) {
        this.paymentServiceClient = paymentServiceClient;
    }

    public Map<String, String> payOrder(String orderId) {
        String paymentStatus = paymentServiceClient.callPaymentService();
        return Map.of(
                "orderId", orderId,
                "paymentStatus", paymentStatus,
                "message", "Order Service gọi Payment Service qua Circuit Breaker để tránh lỗi lan sang hệ thống"
        );
    }

    public String getPaymentCircuitState() {
        return paymentServiceClient.getCircuitState();
    }
}
