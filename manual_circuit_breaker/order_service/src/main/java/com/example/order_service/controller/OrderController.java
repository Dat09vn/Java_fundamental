package com.example.order_service.controller;

import com.example.order_service.client.PaymentClient;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PaymentResponse;
import com.example.order_service.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentClient paymentClient;

    public OrderController(OrderService orderService, PaymentClient paymentClient) {
        this.orderService = orderService;
        this.paymentClient = paymentClient;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request) {

        OrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/orderService")
    public ResponseEntity<PaymentResponse> createNewOrder(
            @RequestBody CreateOrderRequest request) {
                
        PaymentResponse response =
                paymentClient.createPayment(
                        request.getCustomerId(),
                        request.getAmount()
                );

        return ResponseEntity.ok(response);
    }

}