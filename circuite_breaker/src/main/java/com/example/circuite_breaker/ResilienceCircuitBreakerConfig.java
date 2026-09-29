package com.example.circuite_breaker;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

@Configuration
public class ResilienceCircuitBreakerConfig {

    @Bean
    public CircuitBreaker paymentCircuitBreaker() {
        return CircuitBreaker.of("paymentService", io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50.0f)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(2)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
