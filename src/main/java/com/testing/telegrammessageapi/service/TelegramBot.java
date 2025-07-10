package com.testing.telegrammessageapi.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Getter
    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String token = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();

        userService.findByTelegramToken(token).ifPresentOrElse(user -> {
            user.setTelegramChatId(chatId);
            user.setTelegramToken(null); // удалить токен после привязки (безопасно)
            userService.saveUser(user);

            send(chatId, "Ваш Telegram успешно привязан к аккаунту " + user.getUsername());
        }, () -> {
            send(chatId, "Неверный токен. Попробуйте ещё раз.");
        });
    }

    private void send(Long chatId, String text) {
        try {
            this.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
