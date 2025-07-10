package com.testing.telegrammessageapi.service;

import com.testing.telegrammessageapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramBotService {

    private final TelegramBot telegramBot;

    @Value("${telegram.bot.username}")
    private String botUsername;

    public void sendMessage(User user, String content) {
        if(user.getTelegramChatId() == null) return;

        String text = String.format("%s, я получил от тебя сообщение:\n%s", user.getName(), content);

        SendMessage message = SendMessage.builder()
                .chatId(user.getTelegramChatId().toString())
                .text(text)
                .build();
        try{
            telegramBot.execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
