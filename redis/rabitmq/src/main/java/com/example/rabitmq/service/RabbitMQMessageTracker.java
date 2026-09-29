package com.example.rabitmq.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class RabbitMQMessageTracker {

    private final AtomicReference<String> lastMessage = new AtomicReference<>("Chưa có tin nhắn nào được xử lý.");

    public void setLastMessage(String message) {
        lastMessage.set(message);
    }

    public String getLastMessage() {
        return lastMessage.get();
    }
}
