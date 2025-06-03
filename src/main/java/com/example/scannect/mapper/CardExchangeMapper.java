package com.example.scannect.mapper;

import com.example.scannect.dto.CardExchangeDTO;

import java.util.List;

public interface CardExchangeMapper {

    void insertExchange(CardExchangeDTO dto);

    CardExchangeDTO findById(Long id);

    List<CardExchangeDTO> findAllByUserId(String userId);

    void updateStatus(Long id, String status);

    Boolean existsExchange(String toUserId, String fromUserId, Long cardId);
}
