package com.example.scannect.external.naver;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CheckController {
    private final NaverOcrApi naverApi;
    @Value("${naver.service.secretKey}")
    private String secretKey;

    @GetMapping("/naverOcr")
    public ResponseEntity ocr() throws IOException {
        String imageFilePath = new ClassPathResource("static/ocr_image/test.jpg").getFile().getAbsolutePath();
        File file = new File(imageFilePath);

        List<String> result = naverApi.callApi("POST", file.getPath(), secretKey, "jpg");
        if (result != null) {
            for (String s : result) {
                log.info(s);
            }
        } else {
            log.info("null");
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
}