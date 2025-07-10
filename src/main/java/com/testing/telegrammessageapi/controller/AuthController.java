package com.testing.telegrammessageapi.controller;

import com.testing.telegrammessageapi.dto.AuthRequest;
import com.testing.telegrammessageapi.dto.AuthResponse;
import com.testing.telegrammessageapi.dto.RegisterRequest;
import com.testing.telegrammessageapi.model.User;
import com.testing.telegrammessageapi.security.JwtTokenProvider;
import com.testing.telegrammessageapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request.getUsername(), request.getPassword(), request.getName());
        String token = userService.generateTelegramToken(user);
        return ResponseEntity.ok(Map.of("message", "Регистрация прошла успешно", "telegram_token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtTokenProvider.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/telegram-token")
    public ResponseEntity<?> getTelegramToken(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        String token = userService.generateTelegramToken(user); // пересоздаём токен
        return ResponseEntity.ok(Map.of("telegram_token", token));
    }


}
