package com.example.order_service.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerStatusService {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerStatusService(
            CircuitBreakerRegistry circuitBreakerRegistry) {

        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public CircuitBreaker getPaymentCircuitBreaker() {

        return circuitBreakerRegistry
                .circuitBreaker("paymentService");
    }

    public String getPaymentState() {

        CircuitBreaker circuitBreaker =
                getPaymentCircuitBreaker();

        return circuitBreaker.getState().name();
    }
}