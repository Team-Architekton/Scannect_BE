package com.example.scannect.mapper;

import com.example.scannect.dto.OCRScanDTO;

public interface OCRScanMapper {

    void insert(OCRScanDTO scan);

    OCRScanDTO findById(Long id);

    void update(OCRScanDTO scan);
}
