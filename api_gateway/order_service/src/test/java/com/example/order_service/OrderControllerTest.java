package com.example.order_service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OrderControllerTest {

    private final OrderController orderController = new OrderController();

    @Test
    void shouldReturnOrderInfo() {
        Map<String, Object> response = orderController.getOrder(1L);

        assertThat(response.get("id")).isEqualTo(1L);
        assertThat(response.get("status")).isEqualTo("CREATED");
        assertThat(response.get("total")).isEqualTo(250.0);
    }
}
