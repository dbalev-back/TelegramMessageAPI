package com.testing.telegrammessageapi.service;

import com.testing.telegrammessageapi.model.User;
import com.testing.telegrammessageapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(String username, String password, String name) {
        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .name(name)
                .build();
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String generateTelegramToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setTelegramToken(token);
        userRepository.save(user);
        return token;
    }

    public Optional<User> findByTelegramToken(String token) {
        return userRepository.findByTelegramToken(token);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
