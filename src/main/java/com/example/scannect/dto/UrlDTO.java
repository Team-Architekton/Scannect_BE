package com.example.scannect.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UrlDTO implements Serializable {
    private Long id;
    private Long cardId;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
