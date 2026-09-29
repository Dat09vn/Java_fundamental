package com.example.payment;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final Tracer tracer;

    public PaymentController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/payments")
    public String pay(@RequestParam String orderId) {
        Span span = tracer.spanBuilder("payment.process").startSpan();
        try (Scope ignored = span.makeCurrent()) {
            MDC.put("traceId", span.getSpanContext().getTraceId());
            MDC.put("spanId", span.getSpanContext().getSpanId());
            log.info("Payment processing orderId={}", orderId);
            span.setAttribute("order.id", orderId);
            return "Payment successful for order " + orderId;
        } finally {
            MDC.remove("traceId");
            MDC.remove("spanId");
            span.end();
        }
    }
}
