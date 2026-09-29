package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepository;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {

        Payment payment = new Payment(
                request.getOrderId(),
                request.getAmount(),
                "SUCCESS"
        );

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponse(
                saved.getId(),
                saved.getOrderId(),
                saved.getAmount(),
                saved.getStatus()
        );
    }
}