package com.example.order;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final PaymentClient paymentClient;

    public OrderController(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @GetMapping("/orders/checkout")
    public Map<String, Object> checkout() {
        PaymentResponse payment = paymentClient.callPayment();

        return Map.of(
            "orderId", UUID.randomUUID().toString(),
            "status", "PAID",
            "payment", payment
        );
    }
}
