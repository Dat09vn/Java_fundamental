package com.example.order_service;

import com.example.order_service.ServiceRegistry.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;


@Component
public class RoundRobinLoadBalancer {

    private final ServiceRegistry serviceRegistry;

    private final AtomicInteger counter = new AtomicInteger(0);

    public RoundRobinLoadBalancer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public ServiceInstance choose() {

        List<ServiceInstance> instances =
                serviceRegistry.getInstances();

        if (instances.isEmpty()) {
            throw new IllegalStateException("No available instances");
        }

        int index = Math.floorMod(
                counter.getAndIncrement(),
                instances.size()
        );

        return instances.get(index);
    }
}