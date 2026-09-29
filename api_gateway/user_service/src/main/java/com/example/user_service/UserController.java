package com.example.user_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable("id") Long id) {
        return Map.of(
                "id", id,
                "name", "Alice",
                "email", "alice@example.com"
        );
    }
}
