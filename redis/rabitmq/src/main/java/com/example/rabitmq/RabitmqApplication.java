package com.example.rabitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class RabitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabitmqApplication.class, args);
	}

}
