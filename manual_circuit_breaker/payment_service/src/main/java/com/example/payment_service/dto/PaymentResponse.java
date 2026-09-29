package com.example.payment_service.dto;

import java.math.BigDecimal;

public class PaymentResponse {

    private Long paymentId;

    private Long orderId;

    private BigDecimal amount;

    private String status;

    public PaymentResponse(
            Long paymentId,
            Long orderId,
            BigDecimal amount,
            String status) {

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}