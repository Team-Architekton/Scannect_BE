package com.example.scannect.controller;

import com.example.scannect.dto.OCRScanDTO;
import com.example.scannect.service.OCRScanService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
public class OCRScanController {

    private final OCRScanService ocrScanService;
    public OCRScanController(OCRScanService ocrScanService) {
        this.ocrScanService = ocrScanService;
    }

    @PostMapping
    public void create(@RequestBody OCRScanDTO dto) {
        ocrScanService.create(dto);
    }

    @GetMapping("/{id}")
    public OCRScanDTO getById(@PathVariable Long id) {
        return ocrScanService.getById(id);
    }

    @PutMapping
    public void update(@RequestBody OCRScanDTO dto) {
        ocrScanService.update(dto);
    }
}
