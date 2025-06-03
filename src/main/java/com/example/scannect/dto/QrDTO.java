package com.example.scannect.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QrDTO {
    private Long id;
    private Long cardId;
    private Long token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;

}
