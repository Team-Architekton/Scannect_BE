package com.example.scannect.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import com.example.scannect.dto.response.ChatGPTResponse;
import com.example.scannect.service.AiCallService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OpenAiAPIController {
    private final AiCallService aiCallService;

    @PostMapping("/image")
    public String imageAnalysis(@RequestParam MultipartFile image, @RequestParam String requestText)
            throws IOException {
        ChatGPTResponse response = aiCallService.requestImageAnalysis(image, requestText);
        return response.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/text")
    public String textAnalysis(@RequestParam String requestText) {
        ChatGPTResponse response = aiCallService.requestTextAnalysis(requestText);
        return response.getChoices().get(0).getMessage().getContent();
    }
}