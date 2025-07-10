package com.testing.telegrammessageapi.service;

import com.testing.telegrammessageapi.dto.MessageResponse;
import com.testing.telegrammessageapi.model.Message;
import com.testing.telegrammessageapi.model.User;
import com.testing.telegrammessageapi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final TelegramBotService telegramBotService;

    public void saveAndSend(User user, String content) {
        Message message = Message.builder()
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        messageRepository.save(message);

        if(user.getTelegramChatId() != null) {
            telegramBotService.sendMessage(user, content);
        }
    }

    public List<MessageResponse> getUserMessages(User user) {
        return  messageRepository.findAllByUser(user).stream()
                .map(m -> new MessageResponse(m.getContent(), m.getCreatedAt()))
                .toList();
    }
}
