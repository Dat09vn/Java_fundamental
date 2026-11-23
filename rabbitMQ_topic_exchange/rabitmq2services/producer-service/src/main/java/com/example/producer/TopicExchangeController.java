package com.example.producer;

import com.example.producer.TopicMessageProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topic")
public class TopicExchangeController {

    private final TopicMessageProducer producer;

    public TopicExchangeController(
            TopicMessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String publish(
            @RequestParam String routingKey,
            @RequestParam String message) {

        producer.publish(routingKey, message);

        return "Published event: " + routingKey;
    }
}