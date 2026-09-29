package com.example.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpaApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldSaveAndFindUser() {
		User user = new User();
		user.setName("Alice");

		User saved = userRepository.save(user);
		User found = userRepository.findById(saved.getId()).orElseThrow();

		assertThat(found.getName()).isEqualTo("Alice");
	}

}
