package com.example.user_service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    void shouldReturnUserInfo() {
        Map<String, Object> response = userController.getUser(1L);

        assertThat(response.get("id")).isEqualTo(1L);
        assertThat(response.get("name")).isEqualTo("Alice");
        assertThat(response.get("email")).isEqualTo("alice@example.com");
    }
}
