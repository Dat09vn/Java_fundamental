package com.example.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate;

    @Autowired
    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/users")
    public String getUsers() {
        String url = "http://user-service/users";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/user-profile")
    public String getUserProfile() {
        String url = "http://user-service/users/profile/Alice";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping
    public Map<String, String> orderSummary() {
        String userList = restTemplate.getForObject("http://user-service/users", String.class);
        return Map.of("message", "Order service called user-service via Eureka + LoadBalancer", "users", userList);
    }
}
