package com.example.order_service.service;

import com.example.order_service.client.PaymentClient;
import com.example.order_service.dto.PaymentResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentCircuitBreakerService {

    private final PaymentClient paymentClient;

    public PaymentCircuitBreakerService(
            PaymentClient paymentClient) {

        this.paymentClient = paymentClient;
    }

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public PaymentResponse callPayment(
            Long orderId,
            BigDecimal amount) {

        System.out.println(
                "Calling Payment Service..."
        );

        return paymentClient.createPayment(
                orderId,
                amount
        );
    }

    public PaymentResponse paymentFallback(
            Long orderId,
            BigDecimal amount,
            Throwable throwable) {

        System.out.println(
                "========== PAYMENT FALLBACK =========="
        );

        System.out.println(
                "orderId = " + orderId
        );

        System.out.println(
                "error = " + throwable
        );

        System.out.println(
                "======================================"
        );

        PaymentResponse response =
                new PaymentResponse();

        response.setPaymentId(null);
        response.setOrderId(orderId);
        response.setAmount(amount);
        response.setStatus("PAYMENT_UNAVAILABLE");
        response.setMessage(
                "Payment service is temporarily unavailable. Please try again later."
        );

        return response;
    }
}