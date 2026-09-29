package com.example.rabitmq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public RabbitMQProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.name}") String exchangeName,
            @Value("${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        log.info("[Producer] Đã gửi tin nhắn tới RabbitMQ: {}", message);
    }
}
