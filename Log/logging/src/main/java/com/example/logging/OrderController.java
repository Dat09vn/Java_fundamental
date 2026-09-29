package com.example.logging;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final Tracer tracer;
    private final RestClient paymentClient;

    public OrderController(OrderService orderService, Tracer tracer, RestClient.Builder restClientBuilder) {
        this.orderService = orderService;
        this.tracer = tracer;
        this.paymentClient = restClientBuilder
                .baseUrl("http://localhost:8082")
                .build();
    }

    @GetMapping("/orders")
    public String createOrder(@RequestParam String userId) {
        Span span = tracer.spanBuilder("order.controller.createOrder").startSpan();
        try (Scope ignored = span.makeCurrent()) {
            MDC.put("traceId", span.getSpanContext().getTraceId());
            MDC.put("spanId", span.getSpanContext().getSpanId());
            log.info("Received request to create order userId={}", userId);
            span.setAttribute("user.id", userId);

            String orderId = orderService.createOrder(userId);
            span.setAttribute("order.id", orderId);

            String paymentResult = paymentClient.get()
                    .uri("/payments?orderId={orderId}", orderId)
                    .header("traceparent", currentTraceParent())
                    .retrieve()
                    .body(String.class);

            log.info("Payment result {} for orderId={}", paymentResult, orderId);
            log.info("Request completed orderId={}", orderId);
            return "Created order " + orderId + " | " + paymentResult;
        } finally {
            MDC.remove("traceId");
            MDC.remove("spanId");
            span.end();
        }
    }

    private String currentTraceParent() {
        Span current = Span.current();
        if (!current.getSpanContext().isValid()) {
            return "00-00000000000000000000000000000000-0000000000000000-01";
        }
        return "00-" + current.getSpanContext().getTraceId() + "-" + current.getSpanContext().getSpanId() + "-01";
    }
}
