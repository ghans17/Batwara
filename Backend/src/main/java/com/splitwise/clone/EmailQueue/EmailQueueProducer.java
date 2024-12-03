package com.splitwise.clone.EmailQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;

@Component
public class EmailQueueProducer {
     @Autowired
    private RabbitTemplate rabbitTemplate;
    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", true); // true for durable
    }
    public void sendEmailToQueue(String email) {
        rabbitTemplate.convertAndSend("emailQueue", email);
    }
}
