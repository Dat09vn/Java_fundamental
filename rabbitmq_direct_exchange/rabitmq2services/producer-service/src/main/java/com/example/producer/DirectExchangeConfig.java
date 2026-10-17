package com.example.producer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {

    public static final String EXCHANGE = "orders.direct";

    public static final String CREATED_QUEUE =
            "order.created.queue";

    public static final String CANCELLED_QUEUE =
            "order.cancelled.queue";

    @Bean
    public DirectExchange ordersDirectExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(CREATED_QUEUE, true);
    }

    @Bean
    public Queue orderCancelledQueue() {
        return new Queue(CANCELLED_QUEUE, true);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(ordersDirectExchange())
                .with("order.created");
    }

    @Bean
    public Binding cancelledBinding() {
        return BindingBuilder
                .bind(orderCancelledQueue())
                .to(ordersDirectExchange())
                .with("order.cancelled");
    }
}