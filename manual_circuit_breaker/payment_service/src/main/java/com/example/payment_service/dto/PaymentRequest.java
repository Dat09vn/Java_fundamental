package com.example.payment_service.dto;

import java.math.BigDecimal;

public class PaymentRequest {

    private Long orderId;

    private BigDecimal amount;

    public PaymentRequest() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}