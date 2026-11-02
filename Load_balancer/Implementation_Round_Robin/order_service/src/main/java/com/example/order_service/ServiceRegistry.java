package com.example.order_service;

import java.util.List;

import org.springframework.stereotype.Component;



@Component
public class ServiceRegistry {

    public record ServiceInstance(
        String host,
        int port
    ) {}

    private final List<ServiceInstance> instances = List.of(
            new ServiceInstance("localhost", 8081),
            new ServiceInstance("localhost", 8082),
            new ServiceInstance("localhost", 8083)
    );

    public List<ServiceInstance> getInstances() {
        return instances;
    }
}