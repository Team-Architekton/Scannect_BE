package com.example.scannect.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
public class CardListDTO {
    private Long id;
    private String userId;
    private Long cardId;
    private Boolean favorite;
    private String memo;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
