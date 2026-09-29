package com.example.order_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    @GetMapping("/orders/{id}")
    public Map<String, Object> getOrder(@PathVariable("id") Long id) {
        return Map.of(
                "id", id,
                "status", "CREATED",
                "total", 250.0
        );
    }
}
