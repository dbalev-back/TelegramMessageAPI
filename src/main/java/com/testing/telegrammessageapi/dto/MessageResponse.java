package com.testing.telegrammessageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String content;
    private LocalDateTime createdAt;
}
