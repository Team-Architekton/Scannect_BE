package com.example.scannect.service;

import com.example.scannect.dto.CardDTO;
import com.example.scannect.mapper.CardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardMapper cardMapper;

    public CardService(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    //개별 명함 조회
    public CardDTO getCardById(Long id) {
        CardDTO card = cardMapper.findById(id);
        return card;
    }


    // 전체 명함 리스트 (각 명함에 URL 리스트 포함)
    public List<CardDTO> getCardsByUserId(String userId) {
        List<CardDTO> cardList = cardMapper.findAllByUserId(userId);
        return cardList;
    }


    //기본 명함 설정
    public void setMainCard(String userId, Long cardId) {
        cardMapper.unsetMainCard(userId);
        cardMapper.setMainCardById(cardId);
    }

    // 기본 명함 조회 (URL 리스트 포함)
    public CardDTO findMainByUserId(String userId) {
        CardDTO card = cardMapper.findMainByUserId(userId);
        return card;
    }



    //명함 생성
    public void createCard(CardDTO card) {
        cardMapper.insert(card);
        // 사용자 첫 명함이면 대표 명함으로 설정
        int count = cardMapper.countByUserId(card.getUserId());
        if (count == 1) { // insert 이후가 첫 명함이면 → 대표로 설정
            cardMapper.setMainCardById(card.getId());
        }
    }

    public void updateCard(CardDTO card) {
        cardMapper.update(card);
    }


    //명함 개별 삭제
    public void deleteById(Long id) {
        cardMapper.deleteById(id);
    }

    // 명함 전체 삭제 (URL도 함께 삭제)
    public void deleteByUserId(String userId) {
        cardMapper.deleteAllByUserId(userId);
    }


}
