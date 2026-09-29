package com.example.basic_log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable("id") Long id) {

        log.info("Info: Get user id={}", id);
        log.debug("Debug: Get user id={}", id);

        return "User " + id;
    }
}