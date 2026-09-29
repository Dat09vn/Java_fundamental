package com.example.logging;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final Tracer tracer;

    public OrderService(Tracer tracer) {
        this.tracer = tracer;
    }

    public String createOrder(String userId) {
        Span span = tracer.spanBuilder("order.service.createOrder").startSpan();
        try (Scope ignored = span.makeCurrent()) {
            MDC.put("traceId", span.getSpanContext().getTraceId());
            MDC.put("spanId", span.getSpanContext().getSpanId());
            log.info("Starting order creation for userId={}", userId);
            span.setAttribute("user.id", userId);

            String orderId = "ORD-" + userId + "-" + System.currentTimeMillis();

            log.info("Order created successfully orderId={} userId={}", orderId, userId);
            span.setAttribute("order.id", orderId);
            return orderId;
        } finally {
            MDC.remove("traceId");
            MDC.remove("spanId");
            span.end();
        }
    }
}
