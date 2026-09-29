package com.example.jpa.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserServiceTest {

    @Test
    void shouldRejectBlankNames() {
        UserService service = new UserService(null);

        assertThatThrownBy(() -> service.createUser("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Name is required");
    }

    @Test
    void shouldRejectBlankUpdatedNames() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService service = new UserService(userRepository);

        assertThatThrownBy(() -> service.updateUser(1L, "   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Name is required");
    }

    @Test
    void shouldRejectDeleteForMissingUser() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        UserService service = new UserService(userRepository);

        assertThatThrownBy(() -> service.deleteUser(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("User not found");
    }
}
