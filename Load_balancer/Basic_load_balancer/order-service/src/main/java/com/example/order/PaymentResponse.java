package com.example.order;

public record PaymentResponse(
    String message,
    String serviceName,
    String instanceId,
    int port,
    String status
) {
}
