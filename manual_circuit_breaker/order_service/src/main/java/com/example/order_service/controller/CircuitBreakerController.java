package com.example.order_service.controller;

import com.example.order_service.service.CircuitBreakerStatusService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/circuit-breaker")
public class CircuitBreakerController {

    private final CircuitBreakerStatusService statusService;

    public CircuitBreakerController(
            CircuitBreakerStatusService statusService) {

        this.statusService = statusService;
    }

    @GetMapping("/payment/state")
    public Map<String, String> getPaymentState() {

        CircuitBreaker circuitBreaker =
                statusService.getPaymentCircuitBreaker();

        return Map.of(
                "name", circuitBreaker.getName(),
                "state", circuitBreaker.getState().name()
        );
    }
}