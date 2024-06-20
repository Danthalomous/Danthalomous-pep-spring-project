package com.example.controller;

import java.io.Console;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController 
{
    private final AccountService accountService;
    private final MessageService messageService;

    /***
     * Parameterized Constructor setup for DI on the services
     * @param accountService
     */
    @Autowired
    SocialMediaController(AccountService accountService, MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /***
     * Register Endpoint that registers a new user to the database
     * @param account
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account account)
    {
        return ResponseEntity
                    .status(HttpStatus.valueOf(accountService.Register(account))) // Method in service returns the appropriate coede
                    .build();
    }

    /***
     * Login Endpoint that attempts to log in a user
     * @param account
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account)
    {
        Account foundAccount = accountService.Login(account);
        
        // Making sure the results aren't null
        if(foundAccount == null)
        {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401: No user with those credentials
                    .build();
        }
        else
        {
            return ResponseEntity
                    .status(HttpStatus.OK) // 200, There was a user
                    .body(foundAccount);
        }
    }

    /**
     * Create Message endpoint that attempts to write a new message for a user
     * @param message
     * @return
     */
    @PostMapping("/messages")
    public ResponseEntity createMessage(@RequestBody Message message)
    {
        // Checking to see if the user exists for who is posting the message (Since the return result is optional I am using the isPresent() method to ensure it is there)
        if(accountService.findUserById(message.getPostedBy()).isPresent())
        {
            Message insertedMessage = messageService.createMessage(message);
            if(insertedMessage != null)
            {
                return ResponseEntity
                        .status(HttpStatus.OK) // 200, Success!
                        .body(insertedMessage); // The ID should be attached
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400: The user did not exist therefore a bad request or the message was invalidated
                .build(); 
    }

    /**
     * Get all messages endpoint that returns all the messages currently stored in the repo
     * @return
     */
    @GetMapping("/messages")
    public ResponseEntity getAllMessages()
    {
        return ResponseEntity
                    .status(HttpStatus.OK) // 200, Success!
                    .body(messageService.getAllMessages()); // Attaching the list of messages to the body as JSON
    }

    /**
     * Gets message by id endpoint. Gets a message by a specific and provided id
     * @param id
     * @return
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable("messageId") Integer id)
    {
        Optional<Message> retreivedMessage = messageService.getMessageById(id);

        // Seeing if there were any results
        if(retreivedMessage.isPresent())
        {
            return ResponseEntity
                    .status(HttpStatus.OK) // 200, Success!
                    .body(retreivedMessage); // Attaching the retrieved message to the body as JSON
        }
        
        return ResponseEntity
            .status(HttpStatus.OK) // 200, Success! (The body is just empty)
            .build(); 
    }

    /**
     * Delete message endpoint that attempts to delete a message by a given id from the database
     * @param id
     * @return
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable("messageId") Integer id)
    {
        int rowsAffected = messageService.deleteMessageById(id);

        // Ensuring that a record was deleted
        if(rowsAffected > 0)
        {
            return ResponseEntity
                .status(HttpStatus.OK) // 200, Success!
                .body(rowsAffected); // Returning the rows affected
        }

        return ResponseEntity
                .status(HttpStatus.OK) // 200, Success! (Just no rows affected meaning that there was no deletion possible)
                .build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessage(@RequestBody Message message, @PathVariable("messageId") Integer id)
    {
        int rowsAffected = messageService.updateMessage(message.getMessageText(), id);

        // Ensuring that a record was updated
        if(rowsAffected > 0)
        {
            return ResponseEntity
                .status(HttpStatus.OK) // 200, Success!
                .body(rowsAffected); // Returning the rows affected
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // No update occurred, so it was an error
                .build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getMessagesByUserId(@PathVariable("accountId") Integer id)
    {
        return ResponseEntity
                .status(HttpStatus.OK) // 200, Success!
                .body(messageService.getMessagesByUserId(id)); // Even if it empty, the results will be attached to the response body as a JSON object
    }
}
