package com.example.scannect.external.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class TestController {
    private final NaverOcrApi naverApi;
    @Value("${naver.service.secretKey}")
    private String secretKey;
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    public TestController(NaverOcrApi naverApi) {
        this.naverApi = naverApi;
    }
    @GetMapping("/naverOcr/real")
    public ResponseEntity ocr() throws IOException {
        String fileName = "test.jpg"; // 파일 이름
        File file = ResourceUtils.getFile("classpath:static/ocr_image/" + fileName);

        List<String> result = naverApi.callApi("POST", file.getPath(), secretKey, "jpg");
        if (!result.equals(null)) {
            for (String s : result) {
                log.info(s);
            }
        } else {
            log.info("null");
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
}