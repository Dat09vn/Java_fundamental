package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.UserService;
import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
//http://localhost:8080
@RequestMapping()
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public Map<String, String> users() {
        return Map.of(
                "message", "Favorite Movie API is running",
                "endpoint", "/users/{userId}/favorites or /allMovies"
        );
    }

    @GetMapping("/users")
    public Set<User> getAllUsers() {
        System.out.println("UserController:getAllUsers: Received request to get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/users/")
    public Map<String, String> user() {
        return Map.of(
                "message", "Favorite Movie API is running",
                "endpoint", "/users/{userId}/favorites"
        );
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        System.out.println("UserController:getUserById: Received request to get user with ID: " + userId);
        return userService.getUserById(userId);
    }

    @GetMapping("/users/{userId}/")
    public Map<String, String> favorites() {
        return Map.of(
                "message", "Favorite Movie API is running",
                "endpoint", "/users/{userId}/favorites"
        );
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        System.out.println("UserController:addUser: Received request to add user with ID: " + user.getId());
        return ResponseEntity.ok(userService.addUser(user));
    }

}
