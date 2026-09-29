package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public Greeting getGreeting() {
        return new Greeting(1L, "Hello from Spring Boot!");
    }

    public Greeting getGreetingByName(String name) {
        return new Greeting(2L, "Welcome, " + name + "!");
    }
}
