package com.example.scannect.service;

import com.example.scannect.dto.OCRScanDTO;
import com.example.scannect.mapper.OCRScanMapper;
import org.springframework.stereotype.Service;

@Service
public class OCRScanService {

    private final OCRScanMapper ocrScanMapper;
    public OCRScanService(OCRScanMapper ocrScanMapper) {
        this.ocrScanMapper = ocrScanMapper;
    }

    public void create(OCRScanDTO dto) {
        ocrScanMapper.insert(dto);
    }

    public OCRScanDTO getById(Long id) {
        return ocrScanMapper.findById(id);
    }

    public void update(OCRScanDTO dto) {
        ocrScanMapper.update(dto);
    }
}
