package com.example.circuite_breaker;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

@Service
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final CircuitBreaker circuitBreaker;
    private final String paymentServiceUrl;

    public PaymentServiceClient(RestTemplate restTemplate,
                               CircuitBreaker paymentCircuitBreaker,
                               @Value("${payment.service.url}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.circuitBreaker = paymentCircuitBreaker;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public String callPaymentService() {
        Supplier<String> supplier = CircuitBreaker.decorateSupplier(circuitBreaker, () ->
                restTemplate.getForObject(paymentServiceUrl, String.class));

        try {
            return supplier.get();
        } catch (Exception ex) {
            return fallbackPaymentService(ex);
        }
    }

    public String fallbackPaymentService(Exception ex) {
        return "fallback: Payment Service đang lỗi, Order Service không gọi tiếp Payment. "
                + "Lý do: " + ex.getMessage() + ". "
                + "Trạng thái circuit breaker: " + getCircuitState();
    }

    public String getCircuitState() {
        return circuitBreaker.getState().name();
    }

    public static CircuitBreaker createCircuitBreaker() {
        return CircuitBreaker.of("paymentService",
                io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                        .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                        .slidingWindowSize(5)
                        .minimumNumberOfCalls(5)
                        .failureRateThreshold(50.0f)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(2)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .build());
    }
}
