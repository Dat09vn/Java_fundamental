package com.example.producer;

import com.example.producer.TopicExchangeConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TopicMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public TopicMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, String message) {
        rabbitTemplate.convertAndSend(
                TopicExchangeConfig.EXCHANGE,
                routingKey,
                message
        );

        System.out.println(
                "Published [" + routingKey + "]: " + message
        );
    }
}
