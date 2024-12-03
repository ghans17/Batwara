package com.splitwise.clone.Services;

import jakarta.servlet.ServletOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import com.splitwise.clone.EmailQueue.EmailQueueProducer;
import com.splitwise.clone.Entities.Event;
import com.splitwise.clone.Entities.Transaction;
import com.splitwise.clone.Entities.User;
import com.splitwise.clone.Repositories.TransactionDao;
import com.splitwise.clone.Repositories.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class TransactionService {
    @Autowired
    TransactionDao transactionDao;
    @Autowired
    UserDao userDao;
    @Autowired
    EmailQueueProducer emailQueueProducer;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    // Existing code...

    public Transaction addTransaction(int giverId, int receiverId, int amount, LocalDateTime doneAt, int eventId) {
        // Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setGiverId(giverId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setDoneAt(doneAt);
        transaction.setEventId(eventId);

        // Save the transaction to the database
        Transaction savedTransaction = transactionDao.save(transaction);

        // Fetch users from UserDao
        Optional<User> giver = userDao.findById(giverId);
        Optional<User> receiver = userDao.findById(receiverId);

        // Declare email variables
        String giverEmail = null;
        String receiverEmail = null;

        // Check if both users are present
        if (giver.isPresent() && receiver.isPresent()) {
            User giverUser = giver.get();
            User receiverUser = receiver.get();

            giverEmail = giverUser.getEmail();
            receiverEmail = receiverUser.getEmail();

            if (giverEmail == null || receiverEmail == null) {
                logger.error("One of the users has a null email address.");
                return savedTransaction; // Exit early if emails are invalid
            }

            // Prepare email details
            String subject = "Transaction Completed";
            String body = String.format("A transaction of amount %d has been completed between you and %s", amount, receiverUser.getUserName());

            try {
                // Send email notifications to both giver and receiver using their actual emails
                emailQueueProducer.sendEmailToQueue(giverEmail + "," + subject + "," + body); // For giver
                emailQueueProducer.sendEmailToQueue(receiverEmail + "," + subject + "," + body); // For receiver
            } catch (Exception e) {
                logger.error("Failed to send email: {}", e.getMessage());
            }
        } else {
            if (!giver.isPresent()) {
                logger.warn("Giver not found for ID: {}", giverId);
            }
            if (!receiver.isPresent()) {
                logger.warn("Receiver not found for ID: {}", receiverId);
            }
        }

        return savedTransaction; // Return the saved transaction
    }

    public List<Transaction> allTransactionsByUser(int userId) {
        return new ArrayList<>(transactionDao.getAllTransactionsOfUser(userId));
    }

    public List<Transaction> allTransactionsByReceiver(int receiverId, int giverId) {
        return new ArrayList<>(transactionDao.getAllTransactionByReceiver(receiverId, giverId));
    }

    public List<Transaction> allUserTransactionsByEvent(int userId, int eventId) {
        return new ArrayList<>(transactionDao.getUserTransactionByEvent(userId, eventId));
    }

    public List<Transaction> getAllTransactionByEvent(int eventId) {
        return new ArrayList<>(transactionDao.findAllByEventId(eventId));
    }

}
