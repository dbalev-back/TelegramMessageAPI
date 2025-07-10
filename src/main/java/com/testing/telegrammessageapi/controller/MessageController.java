package com.testing.telegrammessageapi.controller;

import com.testing.telegrammessageapi.dto.MessageRequest;
import com.testing.telegrammessageapi.dto.MessageResponse;
import com.testing.telegrammessageapi.model.User;
import com.testing.telegrammessageapi.service.MessageService;
import com.testing.telegrammessageapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody MessageRequest request){
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        messageService.saveAndSend(user, request.getContent());
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(messageService.getUserMessages(user));
    }
}
