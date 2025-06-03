package com.example.scannect.controller;

import com.example.scannect.dto.UrlDTO;
import com.example.scannect.service.UrlService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public void insert(@RequestBody UrlDTO dto) {
        urlService.insert(dto);
    }

    @GetMapping("/card/{cardId}")
    public List<UrlDTO> getByCardId(@PathVariable Long cardId) {
        return urlService.getByCardId(cardId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        urlService.delete(id);
    }
}
