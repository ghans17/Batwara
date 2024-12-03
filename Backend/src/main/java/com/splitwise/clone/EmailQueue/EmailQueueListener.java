package com.splitwise.clone.EmailQueue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class EmailQueueListener {
    
    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "emailQueue")
    public void receiveEmail(String emailDetails) {
        // Parse emailDetails to extract recipient, subject, and body if needed.
        // Here is a simple example assuming a comma-separated string.
        String[] details = emailDetails.split(",");
        String recipient = details[0];
        String subject = details[1];
        String body = details[2];

        emailService.sendEmail(recipient, subject, body);
    }
}
