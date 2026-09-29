package com.example.circuite_breaker;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CircuitBreakerDemoController {

    private final OrderService orderService;

    public CircuitBreakerDemoController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{orderId}/pay")
    public Map<String, String> createOrder(@PathVariable("orderId") String orderId) {
        return orderService.payOrder(orderId);
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of(
                "state", orderService.getPaymentCircuitState(),
                "message", "CLOSED = bình thường, OPEN = ngắt gọi Payment, HALF_OPEN = thử lại một phần"
        );
    }
}
