package com.example.producer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {

    public static final String EXCHANGE = "orders.topic";

    public static final String ORDER_QUEUE =
            "order.events.queue";

    public static final String AUDIT_QUEUE =
            "audit.events.queue";

    @Bean
    public TopicExchange ordersTopicExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue orderEventsQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Queue auditEventsQueue() {
        return new Queue(AUDIT_QUEUE, true);
    }

    @Bean
    public Binding orderEventsBinding() {
        return BindingBuilder
                .bind(orderEventsQueue())
                .to(ordersTopicExchange())
                .with("order.#");
    }

    @Bean
    public Binding auditEventsBinding() {
        return BindingBuilder
                .bind(auditEventsQueue())
                .to(ordersTopicExchange())
                .with("#");
    }
}