package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private static Set<Long> setId = new HashSet<>();
    private final Set<User> users = new HashSet<>();

    public UserService() {
        users.add(new User(1L, "Alice"));
        users.add(new User(2L, "Bob"));
        setId.add(1L);
        setId.add(2L);
    }

    public Set<User> getAllUsers() {
        System.out.println("UserService:getAllUsers: Returning all users");
        return users;
    }

    public User getUserById(Long userId) {
        System.out.println("UserService:getUserById: Searching for user with ID: " + userId);
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public String addUser(User user) {
        System.out.println("UserService:addUser: Attempting to add user with ID: " + user.getId());
        if (setId.contains(user.getId())) {
            System.out.println("UserService:addUser: User with ID " + user.getId() + " already exists.");
            return "User with ID " + user.getId() + " already exists.";
        }
        users.add(user);
        setId.add(user.getId());
        System.out.println("UserService:addUser: User " + user.getName() + " added successfully.");
        return "User " + user.getName() + " added successfully.";
    }

    public void deleteUser(Long userId) {
        users.removeIf(user -> user.getId().equals(userId));
    }

    public void updateUser(User updatedUser) {
        users.removeIf(user -> user.getId().equals(updatedUser.getId()));
        users.add(updatedUser);
    }

    public boolean userExists(Long userId) {
        return users.stream().anyMatch(user -> user.getId().equals(userId));
    }

    public void clearUsers() {
        users.clear();
    }

}
