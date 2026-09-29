package com.example.rabitmq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final RabbitMQMessageTracker messageTracker;

    public RabbitMQConsumer(RabbitMQMessageTracker messageTracker) {
        this.messageTracker = messageTracker;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) {
        log.info("[Consumer] Nhận tin nhắn từ queue: {}", message);
        messageTracker.setLastMessage(message);
    }
}
