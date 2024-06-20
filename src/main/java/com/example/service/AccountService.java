package com.example.service;

import java.util.Optional;

import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService 
{
    private final AccountRepository accountRepository;

    /***
     * Parameterized constructor setup for DI
     * @param accountRepository
     */
    @Autowired
    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    /***
     * Registering a new account to the database as long as it has valid credentials
     * @param account
     * @return
     */
    public int Register(Account account)
    {
        // Username and Password validation
        if(account.getUsername() != "" && account.getPassword().length() >= 4)
        {
            // Ensuring the username is unique
            if(accountRepository.findByUsername(account.getUsername()) == null)
            {
                accountRepository.save(account); // Inserts the record
                return 200; // Success!
            }
            else
                return 409; // Failure by existing username
        }

        return 400; // Failure because invalid credentials
    }

    /***
     * Attempting to log in the provided user
     * @param account
     * @return
     */
    public Account Login(Account account)
    {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword()); // Seeing if the account exists and returning the results
    }

    /***
     * Find a user by their id to see if they exist
     * @return
     */
    public Optional<Account> findUserById(Integer id) // Optional because there may no be account
    {
        return accountRepository.findById(id);
    }
}
