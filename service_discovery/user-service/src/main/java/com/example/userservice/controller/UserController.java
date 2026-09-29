package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Value("${server.port}")
    private String port;

    private final List<String> users = List.of("Alice", "Bob", "Charlie");

    @GetMapping
    public List<String> getUsers() {
        return users;
    }

    @GetMapping("/profile/{name}")
    public String getUserProfile(@PathVariable("name") String name) {
        return "User service instance on port " + port + " => profile for " + name;
    }
}
