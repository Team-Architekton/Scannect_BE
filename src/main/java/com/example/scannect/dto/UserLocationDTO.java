package com.example.scannect.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLocationDTO {
    private Long id;
    private String userId;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}