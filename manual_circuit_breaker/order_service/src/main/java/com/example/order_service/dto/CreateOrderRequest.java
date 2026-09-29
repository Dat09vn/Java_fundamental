package com.example.order_service.dto;

import java.math.BigDecimal;

public class CreateOrderRequest {

    private Long customerId;

    private BigDecimal amount;

    public CreateOrderRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}