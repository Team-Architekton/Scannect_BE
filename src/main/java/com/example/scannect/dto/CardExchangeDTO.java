package com.example.scannect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardExchangeDTO {
    private Long exchangeId;
    private String fromUserId;
    private String toUserId;
    private Long cardId;
    private String method; // 예: "websocket", "qr", "ocr"
    private LocalDateTime exchangedAt;
    private String status;
}

