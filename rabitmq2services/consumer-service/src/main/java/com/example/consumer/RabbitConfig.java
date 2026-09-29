package com.example.consumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "learning.events";
    public static final String QUEUE = "learning.messages";
    public static final String ROUTING_KEY = "message.created";

    @Bean
    TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue messagesQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding messagesBinding(Queue messagesQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(messagesQueue).to(eventsExchange).with(ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
