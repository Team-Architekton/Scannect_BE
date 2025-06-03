package com.example.scannect.service;

import com.example.scannect.dto.CardDTO;
import com.example.scannect.dto.UrlDTO;
import com.example.scannect.mapper.CardMapper;
import com.example.scannect.mapper.UrlMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    private final CardMapper cardMapper;
    private final UrlMapper urlMapper;

    public CardService(CardMapper cardMapper, UrlMapper urlMapper) {
        this.cardMapper = cardMapper;
        this.urlMapper = urlMapper;
    }

    //개별 명함 조회
    public CardDTO getCardById(Long id) {
        CardDTO card = cardMapper.findById(id);
        if (card != null) {
            List<UrlDTO> urlDTOList = urlMapper.findByCardId(id);
            List<String> urlList = new ArrayList<>();
            for (UrlDTO urlDTO : urlDTOList) {
                urlList.add(urlDTO.getUrl());
            }
            card.setUrlList(urlList);
        }
        return card;
    }


    // 전체 명함 리스트 (각 명함에 URL 리스트 포함)
    public List<CardDTO> getCardsByUserId(String userId) {
        List<CardDTO> cardList = cardMapper.findAllByUserId(userId);
        for (CardDTO card : cardList) {
            List<UrlDTO> urlDTOList = urlMapper.findByCardId(card.getId());
            List<String> urlList = new ArrayList<>();
            for (UrlDTO urlDTO : urlDTOList) {
                urlList.add(urlDTO.getUrl());
            }
            card.setUrlList(urlList);
        }
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
        if (card != null) {
            List<UrlDTO> urlDTOList = urlMapper.findByCardId(card.getId());
            List<String> urlList = new ArrayList<>();
            for (UrlDTO urlDTO : urlDTOList) {
                urlList.add(urlDTO.getUrl());
            }
            card.setUrlList(urlList);
        }
        return card;
    }



    //명함 생성
    public void createCard(CardDTO card) {
        cardMapper.insert(card);
        Long cardId = card.getId();

        // URL 리스트가 있을 경우
        if (card.getUrlList() != null && !card.getUrlList().isEmpty()) {
            // URL 리스트를 담을 DTO 생성
            List<UrlDTO> urlDTOList = new ArrayList<>();
            for (String url : card.getUrlList()) {
                // URLDTO에 cardId와 url 값을 세팅
                UrlDTO urlDTO = new UrlDTO();
                urlDTO.setCardId(cardId);
                urlDTO.setUrl(url);
                urlDTOList.add(urlDTO);
            }
            // insertAll을 통해 한 번에 여러 URL 삽입
            urlMapper.insertAll(urlDTOList);
        }

        // 사용자 첫 명함이면 대표 명함으로 설정
        int count = cardMapper.countByUserId(card.getUserId());
        if (count == 1) { // insert 이후가 첫 명함이면 → 대표로 설정
            cardMapper.setMainCardById(card.getId());
        }
    }

    public void updateCard(CardDTO card) {
        int result = cardMapper.update(card);

        // URL 업데이트: 기존 URL 삭제 후 재삽입
        urlMapper.deleteByCardId(card.getId());

        if (card.getUrlList() != null && !card.getUrlList().isEmpty()) {
            List<UrlDTO> urlDTOList = new ArrayList<>();
            for (String url : card.getUrlList()) {
                UrlDTO urlDTO = new UrlDTO();
                urlDTO.setCardId(card.getId());
                urlDTO.setUrl(url);
                urlDTOList.add(urlDTO);
            }
            urlMapper.insertAll(urlDTOList);
        }
    }

    //명함 개별 삭제
    public void deleteById(Long id) {
        urlMapper.deleteByCardId(id);
        cardMapper.deleteById(id);
    }

    // 명함 전체 삭제 (URL도 함께 삭제)
    public void deleteByUserId(String userId) {
        // 1. 해당 유저가 가진 모든 카드 리스트 조회
        List<CardDTO> cardList = cardMapper.findAllByUserId(userId);

        // 2. 각 카드별로 연결된 URL 삭제
        for (CardDTO card : cardList) {
            urlMapper.deleteByCardId(card.getId());
        }

        // 3. 카드 전체 삭제
        cardMapper.deleteAllByUserId(userId);
    }


}
