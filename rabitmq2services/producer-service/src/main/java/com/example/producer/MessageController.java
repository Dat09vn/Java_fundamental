package com.example.producer;

import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public Map<String, String> publish(@RequestBody MessageEvent event) {
        if (event.text() == null || event.text().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text must not be blank");
        }

        String messageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        return Map.of("messageId", messageId, "status", "published");
    }
}
