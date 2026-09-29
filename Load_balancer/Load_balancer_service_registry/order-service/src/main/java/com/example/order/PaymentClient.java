package com.example.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @GetMapping("/api/payment")
    PaymentResponse callPayment();
}
