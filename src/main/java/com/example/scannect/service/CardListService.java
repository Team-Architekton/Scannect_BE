package com.example.scannect.service;

import com.example.scannect.dto.CardDTO;
import com.example.scannect.dto.CardListDTO;
import com.example.scannect.mapper.CardListMapper;
import com.example.scannect.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardListService {

    private final CardListMapper cardListMapper;
    private final CardMapper cardMapper; // cardMapper 추가

    // 명함 저장
    public void save(CardListDTO cardList) {
        cardListMapper.insert(cardList);
    }

    public void insertByWebSocket(String userId, Long cardId) {
        CardListDTO dto = new CardListDTO();
        dto.setUserId(userId);
        dto.setCardId(cardId);
        dto.setIsActive(true); // 필요 시 추가

        cardListMapper.insertByWebSocket(dto); // ✅ DTO 객체로 전달
    }

    // 서비스 로직 (CardListService.java)
    public void updateMemo(Long id, String memo) {
        cardListMapper.updateMemo(id, memo);
    }


    // 서비스에서 직접 id와 boolean 값만 받도록 수정
    public void updateActive(Long id, boolean isActive) {
        cardListMapper.updateActive(id, isActive);
    }

    public void updateFavorite(Long id, boolean favorite) {
        cardListMapper.updateFavorite(id, favorite);
    }

    // 삭제
    public void delete(Long id) {
        cardListMapper.delete(id);
    }

    // 전체 목록 (cardList에 저장된 cardId를 기준으로 card 정보를 조회)
    public List<CardDTO> getAll(String userId) {
        List<CardListDTO> cardListDTOs = cardListMapper.findAllByUserId(userId);
        List<CardDTO> cardDTOList = new ArrayList<>();

        for (CardListDTO cardListDTO : cardListDTOs) {
            CardDTO card = cardMapper.findById(cardListDTO.getCardId());
            if (card != null) {
                cardDTOList.add(card);
            } else {
                System.out.println("❗ 카드 정보 없음! cardId: " + cardListDTO.getCardId());
            }
        }

        // 명함 리스트 최신순으로 정렬
        Collections.sort(cardDTOList, (card1, card2) -> card2.getCreatedAt().compareTo(card1.getCreatedAt()));

        return cardDTOList;
    }


    // 중요 목록
    public List<CardDTO> getFavorites(String userId) {
        List<CardListDTO> cardListDTOs = cardListMapper.findFavoritesByUserId(userId);
        List<CardDTO> cardDTOList = new ArrayList<>();

        for (CardListDTO cardListDTO : cardListDTOs) {
            CardDTO card = cardMapper.findById(cardListDTO.getCardId());
            cardDTOList.add(card);
        }
        // 명함 리스트 최신순으로 정렬
        Collections.sort(cardDTOList, (card1, card2) -> card2.getCreatedAt().compareTo(card1.getCreatedAt()));

        return cardDTOList;
    }

    // 숨김 목록
    public List<CardDTO> getHidden(String userId) {
        List<CardListDTO> cardListDTOs = cardListMapper.findHiddenByUserId(userId);
        List<CardDTO> cardDTOList = new ArrayList<>();

        for (CardListDTO cardListDTO : cardListDTOs) {
            CardDTO card = cardMapper.findById(cardListDTO.getCardId());
            cardDTOList.add(card);
        }
        // 명함 리스트 최신순으로 정렬
        Collections.sort(cardDTOList, (card1, card2) -> card2.getCreatedAt().compareTo(card1.getCreatedAt()));

        return cardDTOList;
    }

    // 키워드 검색 (JOIN 포함)
    public List<CardDTO> search(String userId, String keyword) {
        List<CardListDTO> cardListDTOs = cardListMapper.searchByKeyword(userId, keyword);
        List<CardDTO> cardDTOList = new ArrayList<>();

        for (CardListDTO cardListDTO : cardListDTOs) {
            CardDTO card = cardMapper.findById(cardListDTO.getCardId());
            cardDTOList.add(card);
        }
        // 명함 리스트 최신순으로 정렬
        Collections.sort(cardDTOList, (card1, card2) -> card2.getCreatedAt().compareTo(card1.getCreatedAt()));

        return cardDTOList;
    }

}
