package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>
{
    @Query("SELECT account FROM Account account WHERE account.username = :uname") // JPQL for the custom query (instead of SQL)
    Account findByUsername(@Param("uname") String username); // Method that can be called in the service

    @Query("SELECT account FROM Account account WHERE account.username = :uname AND account.password = :pswd") // JPQL for the custom query
    Account findByUsernameAndPassword(@Param("uname") String username, @Param("pswd") String password); // Method that can be called in the service
}
