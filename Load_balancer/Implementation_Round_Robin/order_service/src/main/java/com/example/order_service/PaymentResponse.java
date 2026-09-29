package com.example.order_service;

public record PaymentResponse(
    String message,
    String serviceName,
    String instanceId,
    int port,
    String status
) {
}
