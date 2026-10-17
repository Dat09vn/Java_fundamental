package com.example.producer;

// import com.example.producer.DirectExchangeConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public DirectMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreated(String message) {
        rabbitTemplate.convertAndSend(
                DirectExchangeConfig.EXCHANGE,
                "order.created",
                message
        );
    }

    public void sendOrderCancelled(String message) {
        rabbitTemplate.convertAndSend(
                DirectExchangeConfig.EXCHANGE,
                "order.cancelled",
                message
        );
    }
}