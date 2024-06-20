package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService
{
    public final MessageRepository messageRepository;

    /**
     * Parameterized constructor Autowired for DI
     * @param messageRepository
     */
    @Autowired
    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    /***
     * Method that attempts to create a new message in the repository
     * @param message the message the user wrote
     * @param userID the user posting the message
     */
    public Message createMessage(Message message)
    {
        // Validating the message
        if(message.getMessageText() != "" && message.getMessageText().length() < 255)
        {
            return messageRepository.save(message); // Inserting the record and returning ther results (including the key)
        }

        return null; // Return a null object since it was invalid
    }

    /**
     * Method that gets all Messages from the repository
     * @return
     */
    public List<Message> getAllMessages()
    {
        return messageRepository.findAll(); // Using the findAll() method to return all instances of Message in the repo
    }

    /**
     * Method that gets a message by its id from the repo
     * @param id
     * @return
     */
    public Optional<Message> getMessageById(Integer id)
    {
        return messageRepository.findById(id); // Returns whatever is found as an optional
    }

    /**
     * Method that deletes a message by a given id
     * @param id
     * @return
     */
    public int deleteMessageById(Integer id)
    {
        return messageRepository.deleteMessageById(id); // Returns the number of rows affected
    }

    /**
     * Method that updates the message text of a message by its id
     * @param newMessage
     * @param id
     * @return
     */
    public int updateMessage(String newMessage, Integer id)
    {
        System.out.println("NEW MESSAGE: " + newMessage);
        // checking if the new message is valid
        if(newMessage.length() <= 255 && !newMessage.isEmpty())
        {
            System.out.println("New Message is blank: " + newMessage.isEmpty());
            // Checking if the id exists
            if(messageRepository.findById(id) != null)
                return messageRepository.updateMessage(newMessage, id); // Returns the number of rows affected (if any)
        }
        
        return -1; // Validation failed or id didn't exist
    }

    /**
     * Method that gets all messages for a given user
     * @param id
     * @return
     */
    public List<Message> getMessagesByUserId(Integer id)
    {
        return messageRepository.findMessagesByUserId(id); // Returns a list of Messages by a given user
    }
}
