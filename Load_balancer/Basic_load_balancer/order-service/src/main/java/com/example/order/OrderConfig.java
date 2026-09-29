package com.example.order;

import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import reactor.core.publisher.Flux;

@Configuration
public class OrderConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ServiceInstanceListSupplier paymentServiceInstanceListSupplier() {
        return new ServiceInstanceListSupplier() {
            @Override
            public String getServiceId() {
                return "payment-service";
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                return Flux.just(List.of(
                    new DefaultServiceInstance("payment-instance-1", "payment-service", "localhost", 8081, false),
                    new DefaultServiceInstance("payment-instance-2", "payment-service", "localhost", 8082, false),
                    new DefaultServiceInstance("payment-instance-3", "payment-service", "localhost", 8083, false)
                ));
            }
        };
    }
}
