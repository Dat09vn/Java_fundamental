package com.example.jpa.service;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name) {
        validateName(name);

        User user = new User(name.trim());
        return userRepository.save(user);
    }

    public User updateUser(Long id, String name) {
        validateName(name);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setName(name.trim());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.delete(user);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }
}
