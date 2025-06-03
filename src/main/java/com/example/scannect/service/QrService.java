package com.example.scannect.service;

import com.example.scannect.dto.QrDTO;
import com.example.scannect.mapper.QrMapper;
import org.springframework.stereotype.Service;

@Service
public class QrService {

    private final QrMapper qrMapper;
    public QrService(QrMapper qrMapper) {
        this.qrMapper = qrMapper;
    }

    public void create(QrDTO qr) {
        qrMapper.insert(qr);
    }

    public QrDTO getByCardId(Long cardId) {
        return qrMapper.findByCardId(cardId);
    }

    public QrDTO getByToken(Long token) {
        return qrMapper.findByToken(token);
    }

    public void update(QrDTO qr) {
        qrMapper.update(qr);
    }
}
