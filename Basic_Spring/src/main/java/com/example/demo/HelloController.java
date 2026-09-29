package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final GreetingService greetingService;

    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/")
    public Greeting hello() {
        return greetingService.getGreeting();
    }

    @GetMapping("/greet/{name}")
    public Greeting greet(@PathVariable String name) {
        return greetingService.getGreetingByName(name);
    }
}
