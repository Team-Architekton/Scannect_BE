package com.example.scannect.mapper;

import com.example.scannect.dto.QrDTO;

public interface QrMapper {

    void insert(QrDTO qr);

    QrDTO findByCardId(Long cardId);

    QrDTO findByToken(Long token);

    void update(QrDTO qr);
}
