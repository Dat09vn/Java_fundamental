package com.example.gateway;

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
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);
    private final RestClient restClient;
    private final Tracer tracer;

    public GatewayController(RestClient.Builder builder, Tracer tracer) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
        this.tracer = tracer;
    }

    @GetMapping("/gateway")
    public String gateway(@RequestParam String userId) {
        Span span = tracer.spanBuilder("gateway.createOrder").startSpan();
        try (Scope ignored = span.makeCurrent()) {
            MDC.put("traceId", span.getSpanContext().getTraceId());
            MDC.put("spanId", span.getSpanContext().getSpanId());
            log.info("Gateway received request userId={}", userId);
            String response = restClient.get()
                    .uri("/orders?userId={userId}", userId)
                    .header("traceparent", currentTraceParent())
                    .retrieve()
                    .body(String.class);
            log.info("Gateway completed response={}", response);
            return response;
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
