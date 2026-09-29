package com.example.order_service.service;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PaymentResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final PaymentCircuitBreakerService
            paymentCircuitBreakerService;

    public OrderService(
            OrderRepository orderRepository,
            PaymentCircuitBreakerService
                    paymentCircuitBreakerService) {

        this.orderRepository = orderRepository;

        this.paymentCircuitBreakerService =
                paymentCircuitBreakerService;
    }

    public OrderResponse createOrder(
            CreateOrderRequest request) {

        // 1. Create order
        Order order = new Order(
                request.getCustomerId(),
                request.getAmount(),
                "PENDING"
        );

        Order savedOrder =
                orderRepository.save(order);

        // 2. Call Payment Service
        PaymentResponse payment =
                paymentCircuitBreakerService.callPayment(
                        savedOrder.getId(),
                        savedOrder.getAmount()
                );

        // 3. Update order status
        if ("SUCCESS".equals(payment.getStatus())) {

            savedOrder.setStatus("PAID");

        } else {

            savedOrder.setStatus("PAYMENT_FAILED");
        }

        orderRepository.save(savedOrder);

        // 4. Return response
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getAmount(),
                savedOrder.getStatus(),
                payment
        );
    }
}