package com.example.jpa;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserCommandRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserCommandRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // userRepository.save(new User("Alice"));
        // userRepository.save(new User("Bob"));

        System.out.println("Users in database: " + userRepository.findAll().size());
    }
}
