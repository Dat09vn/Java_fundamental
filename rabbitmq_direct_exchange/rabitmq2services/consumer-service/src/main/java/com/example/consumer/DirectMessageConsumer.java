package com.example.consumer;

// import com.example.consumer.DirectExchangeConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageConsumer {

    @RabbitListener(
        queues = DirectExchangeConfig.CREATED_QUEUE
    )
    public void handleOrderCreated(String message) {
        System.out.println(
                "[OrderCreatedConsumer] " + message
        );
    }

    @RabbitListener(
        queues = DirectExchangeConfig.CANCELLED_QUEUE
    )
    public void handleOrderCancelled(String message) {
        System.out.println(
                "[OrderCancelledConsumer] " + message
        );
    }
}