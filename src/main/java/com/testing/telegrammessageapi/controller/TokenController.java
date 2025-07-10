package com.testing.telegrammessageapi.controller;

import com.testing.telegrammessageapi.model.User;
import com.testing.telegrammessageapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> generateTelegramToken(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        String token = userService.generateTelegramToken(user);
        return ResponseEntity.ok(token);
    }
}
