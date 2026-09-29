package com.example.order_service.dto;

import java.math.BigDecimal;

public class OrderResponse {

    private Long orderId;

    private Long customerId;

    private BigDecimal amount;

    private String orderStatus;

    private PaymentResponse payment;

    public OrderResponse(
            Long orderId,
            Long customerId,
            BigDecimal amount,
            String orderStatus,
            PaymentResponse payment) {

        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.orderStatus = orderStatus;
        this.payment = payment;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public PaymentResponse getPayment() {
        return payment;
    }
}