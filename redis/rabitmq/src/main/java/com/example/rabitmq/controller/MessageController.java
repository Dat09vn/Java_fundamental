package com.example.rabitmq.controller;

import com.example.rabitmq.dto.MessageRequest;
import com.example.rabitmq.service.RabbitMQMessageTracker;
import com.example.rabitmq.service.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final RabbitMQProducer rabbitMQProducer;
    private final RabbitMQMessageTracker messageTracker;

    public MessageController(RabbitMQProducer rabbitMQProducer, RabbitMQMessageTracker messageTracker) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.messageTracker = messageTracker;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest request) {
        rabbitMQProducer.sendMessage(request.message());
        return ResponseEntity.ok("Đã gửi tin nhắn: " + request.message());
    }

    @GetMapping("/last-message")
    public ResponseEntity<String> getLastMessage() {
        return ResponseEntity.ok(messageTracker.getLastMessage());
    }
}
