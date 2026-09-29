package com.example.order;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentClient {

    private final RestTemplate restTemplate;

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentResponse callPayment() {
        return restTemplate.getForObject("http://payment-service/api/payment", PaymentResponse.class);
    }
}
