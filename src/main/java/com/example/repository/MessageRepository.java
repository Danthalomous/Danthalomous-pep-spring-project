package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>
{
    // Modifying and transactional are needed to ensure Database integrity
    @Modifying
    @Transactional
    @Query("DELETE FROM Message message WHERE message.id = :id") // Custom query to execute on the repo using JPQL
    int deleteMessageById(@Param("id") Integer id); // Custom delete by id method that returns the amount of rows affected

     // Modifying and transactional are needed to ensure Database integrity
     @Modifying
     @Transactional
     @Query("UPDATE Message message SET message.messageText = :newText WHERE message.messageId = :id") // Custom query to execute on the repo using JPQL
     int updateMessage(@Param("newText") String newMessage, @Param("id") Integer id); // Custom update by id method that returns the amount of rows affected

     
    @Query("SELECT message FROM Message message WHERE message.postedBy = :userId") // Custom query to return all objects that were posted by a user
    List<Message> findMessagesByUserId(@Param("userId") Integer id); // Returns a list of messages
}
