package com.example.order_service.client;

import com.example.order_service.dto.PaymentResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public PaymentResponse createPayment(
            Long orderId,
            BigDecimal amount) {

        return restClient
                .post()
                .uri("/payments")
                .body(new PaymentRequest(
                        orderId,
                        amount
                ))
                .retrieve()
                .body(PaymentResponse.class);
    }

    private record PaymentRequest(
            Long orderId,
            BigDecimal amount
    ) {
    }
}