package com.example.scannect.service;

import com.example.scannect.dto.CardDTO;
import com.example.scannect.dto.CardListDTO;
import com.example.scannect.mapper.CardListMapper;
import com.example.scannect.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getAll(String userId) {
        List<CardListDTO> cardListDTOs = cardListMapper.findAllByUserId(userId);
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 카드 ID 기준으로 중복 제거
        Map<Long, CardListDTO> cardListMap = new HashMap<>();
        for (CardListDTO cardListDTO : cardListDTOs) {
            cardListMap.put(cardListDTO.getCardId(), cardListDTO); // 중복 카드 ID 제거
        }

        for (Long cardId : cardListMap.keySet()) {
            CardDTO card = cardMapper.findById(cardId);
            if (card != null) {
                CardListDTO cardList = cardListMap.get(cardId);

                Map<String, Object> flatCardInfo = new HashMap<>();
                flatCardInfo.put("cardId", card.getId());
                flatCardInfo.put("cardName", card.getCardName());
                flatCardInfo.put("nickname", card.getNickname());
                flatCardInfo.put("email", card.getEmail());
                flatCardInfo.put("job", card.getJob());
                flatCardInfo.put("industry", card.getIndustry());
                flatCardInfo.put("belongTo", card.getBelongTo());
                flatCardInfo.put("department", card.getDepartment());
                flatCardInfo.put("position", card.getPosition());
                flatCardInfo.put("content", card.getContent());
                flatCardInfo.put("companyTel", card.getCompanyTel());
                flatCardInfo.put("phoneNum", card.getPhoneNum());
                flatCardInfo.put("imgUrl", card.getImgUrl());
                flatCardInfo.put("colour", card.getColour());
                flatCardInfo.put("urlList", card.getUrlList());
                // cardList에서 필요한 정보만 추가
                flatCardInfo.put("id", cardList.getId());
                flatCardInfo.put("userId", cardList.getUserId());
                flatCardInfo.put("memo", cardList.getMemo());
                flatCardInfo.put("favorite", cardList.getFavorite());
                flatCardInfo.put("isActive", cardList.getIsActive());

                resultList.add(flatCardInfo);
            } else {
                System.out.println("❗ 카드 정보 없음! cardId: " + cardId);
            }
        }

        return resultList;
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
        //Collections.sort(cardDTOList, (card1, card2) -> card2.getCreatedAt().compareTo(card1.getCreatedAt()));

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
        //Collections.sort(cardDTOList, (card1, card2) -> card2.getCreatedAt().compareTo(card1.getCreatedAt()));

        return cardDTOList;
    }

    public List<Map<String, Object>> search(String userId, String keyword) {
        List<CardListDTO> cardListDTOs = cardListMapper.searchByKeyword(userId, keyword);
        List<Map<String, Object>> resultList = new ArrayList<>();

        Map<Long, CardListDTO> cardListMap = new HashMap<>();
        for (CardListDTO cardListDTO : cardListDTOs) {
            cardListMap.put(cardListDTO.getCardId(), cardListDTO);
        }

        for (Long cardId : cardListMap.keySet()) {
            CardDTO card = cardMapper.findById(cardId);
            if (card != null) {
                CardListDTO cardList = cardListMap.get(cardId);

                Map<String, Object> flatCardInfo = new HashMap<>();
                flatCardInfo.put("cardId", card.getId());
                flatCardInfo.put("cardName", card.getCardName());
                flatCardInfo.put("nickname", card.getNickname());
                flatCardInfo.put("email", card.getEmail());
                flatCardInfo.put("job", card.getJob());
                flatCardInfo.put("industry", card.getIndustry());
                flatCardInfo.put("belongTo", card.getBelongTo());
                flatCardInfo.put("department", card.getDepartment());
                flatCardInfo.put("position", card.getPosition());
                flatCardInfo.put("content", card.getContent());
                flatCardInfo.put("companyTel", card.getCompanyTel());
                flatCardInfo.put("phoneNum", card.getPhoneNum());
                flatCardInfo.put("imgUrl", card.getImgUrl());
                flatCardInfo.put("colour", card.getColour());
                flatCardInfo.put("urlList", card.getUrlList());
                // cardList에서 필요한 정보만 추가
                flatCardInfo.put("id", cardList.getId());
                flatCardInfo.put("userId", cardList.getUserId());
                flatCardInfo.put("memo", cardList.getMemo());
                flatCardInfo.put("favorite", cardList.getFavorite());
                flatCardInfo.put("isActive", cardList.getIsActive());

                resultList.add(flatCardInfo);
            } else {
                System.out.println("❗ 카드 정보 없음! cardId: " + cardId);
            }
        }

        return resultList;
    }
}
