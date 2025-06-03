package com.example.scannect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OCRScanDTO {
    private Long id;
    private String userId;
    private String img;
    private String extractedText;
    private String processedData;
    private String status;
    private LocalDateTime createdAt;

}
