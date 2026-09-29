package com.example.circuite_breaker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

class CircuitBreakerDemoControllerTest {

    @Test
    void shouldReturnPaymentSuccess_whenPaymentServiceIsHealthy() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://example.com/payments/charge"))
                .andRespond(withSuccess("{\"status\":\"OK\"}", MediaType.APPLICATION_JSON));

        PaymentServiceClient client = new PaymentServiceClient(
                restTemplate,
                PaymentServiceClient.createCircuitBreaker(),
                "https://example.com/payments/charge");

        String response = client.callPaymentService();

        assertThat(response).contains("OK");
        assertThat(response).doesNotContain("fallback");
    }

    @Test
    void shouldReturnFallbackResponseFromCircuitBreaker_whenPaymentServiceFails() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://example.com/payments/charge"))
                .andRespond(withServerError());

        PaymentServiceClient client = new PaymentServiceClient(
                restTemplate,
                PaymentServiceClient.createCircuitBreaker(),
                "https://example.com/payments/charge");

        String response = client.callPaymentService();

        assertThat(response).contains("fallback");
        assertThat(response).contains("Order Service");
    }

    @Test
    void shouldOpenCircuit_afterRepeatedFailures() {
        CircuitBreaker circuitBreaker = PaymentServiceClient.createCircuitBreaker();
        RestTemplate failingRestTemplate = new RestTemplate() {
            @Override
            public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
                throw new RuntimeException("Payment failed");
            }
        };

        PaymentServiceClient client = new PaymentServiceClient(
                failingRestTemplate,
                circuitBreaker,
                "https://example.com/payments/charge");

        for (int i = 0; i < 5; i++) {
            String response = client.callPaymentService();
            assertThat(response).contains("fallback");
        }

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    @Test
    void shouldTransitionToHalfOpen_afterWaitDuration() throws InterruptedException {
        CircuitBreaker circuitBreaker = CircuitBreaker.of("half-open-demo",
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(2)
                        .minimumNumberOfCalls(2)
                        .failureRateThreshold(50.0f)
                        .waitDurationInOpenState(Duration.ofMillis(100))
                        .permittedNumberOfCallsInHalfOpenState(2)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .build());

        RestTemplate failingRestTemplate = new RestTemplate() {
            @Override
            public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
                throw new RuntimeException("Payment failed");
            }
        };

        PaymentServiceClient client = new PaymentServiceClient(
                failingRestTemplate,
                circuitBreaker,
                "https://example.com/payments/charge");

        for (int i = 0; i < 2; i++) {
            assertThat(client.callPaymentService()).contains("fallback");
        }

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        Thread.sleep(300L);

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
    }

    @Test
    void shouldReturnSuccessfulOrder_whenPaymentServiceIsHealthy() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://example.com/payments/charge"))
                .andRespond(withSuccess("{\"status\":\"OK\"}", MediaType.APPLICATION_JSON));

        PaymentServiceClient client = new PaymentServiceClient(
                restTemplate,
                PaymentServiceClient.createCircuitBreaker(),
                "https://example.com/payments/charge");

        OrderService orderService = new OrderService(client);

        String result = orderService.payOrder("1001").get("paymentStatus");

        assertThat(result).contains("OK");
    }

    @Test
    void shouldReturnFallbackOrder_whenPaymentServiceFails() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://example.com/payments/charge"))
                .andRespond(withServerError());

        PaymentServiceClient client = new PaymentServiceClient(
                restTemplate,
                PaymentServiceClient.createCircuitBreaker(),
                "https://example.com/payments/charge");

        OrderService orderService = new OrderService(client);

        String result = orderService.payOrder("1002").get("paymentStatus");

        assertThat(result).contains("fallback");
    }
}
