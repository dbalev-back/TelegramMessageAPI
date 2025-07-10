package com.testing.telegrammessageapi.repository;

import com.testing.telegrammessageapi.model.Message;
import com.testing.telegrammessageapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByUser(User user);
}
