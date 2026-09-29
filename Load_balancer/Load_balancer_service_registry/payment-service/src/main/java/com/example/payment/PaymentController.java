package com.example.payment;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${server.port}")
    private int port;

    @Value("${instance.id:payment-instance-default}")
    private String instanceId;

    @GetMapping("/payment")
    public Map<String, Object> pay() {
        return Map.of(
            "message", "Payment processed",
            "serviceName", "payment-service",
            "instanceId", instanceId,
            "port", port,
            "status", "SUCCESS"
        );
    }
}
