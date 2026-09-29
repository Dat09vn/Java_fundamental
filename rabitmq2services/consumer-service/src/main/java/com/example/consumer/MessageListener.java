package com.example.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(MessageEvent event) {
        log.info("Received message from RabbitMQ: {}", event.text());
    }
}
