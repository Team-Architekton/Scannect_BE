package com.example.scannect.controller;

import com.example.scannect.dto.QrDTO;
import com.example.scannect.service.QrService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrController {

    private final QrService qrService;
    public QrController(QrService qrService) {
        this.qrService = qrService;
    }

    @PostMapping
    public void create(@RequestBody QrDTO qr) {
        qrService.create(qr);
    }

    @GetMapping("/card/{cardId}")
    public QrDTO getByCardId(@PathVariable Long cardId) {
        return qrService.getByCardId(cardId);
    }

    @GetMapping("/token/{token}")
    public QrDTO getByToken(@PathVariable Long token) {
        return qrService.getByToken(token);
    }

    @PutMapping
    public void update(@RequestBody QrDTO qr) {
        qrService.update(qr);
    }
}
