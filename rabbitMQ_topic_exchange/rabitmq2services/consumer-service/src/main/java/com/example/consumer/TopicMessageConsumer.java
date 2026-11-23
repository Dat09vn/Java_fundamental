package com.example.consumer;

import com.example.consumer.TopicExchangeConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageConsumer {

    @RabbitListener(
        queues = TopicExchangeConfig.ORDER_QUEUE
    )
    public void handleOrderEvent(String message) {
        System.out.println(
                "[OrderConsumer] " + message
        );
    }

    @RabbitListener(
        queues = TopicExchangeConfig.AUDIT_QUEUE
    )
    public void handleAuditEvent(String message) {
        System.out.println(
                "[AuditConsumer] " + message
        );
    }
}