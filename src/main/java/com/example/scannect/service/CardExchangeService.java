// CardExchangeService.java

package com.example.scannect.service;

import com.example.scannect.common.WebSocketHandler;
import com.example.scannect.dto.CardDTO;
import com.example.scannect.dto.CardExchangeDTO;
import com.example.scannect.dto.CardListDTO;
import com.example.scannect.mapper.CardExchangeMapper;
import com.example.scannect.mapper.CardListMapper;
import com.example.scannect.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CardExchangeService {

    private final CardMapper cardMapper;
    private final CardExchangeMapper cardExchangeMapper;
    private final CardListMapper cardListMapper;
    private final WebSocketHandler webSocketHandler;

    /**
     * 위치 기반으로 양방향 명함 리스트를 WebSocket으로 전송
     */
    public void exchangeViaWebSocket(String fromUserId, List<String> toUserIds) {
        // 1. 내 명함 → 상대방들에게 전송
        CardDTO myCard = cardMapper.findMainByUserId(fromUserId);
        if (myCard != null) {
            for (String toUserId : toUserIds) {
                webSocketHandler.sendCardListToUser(toUserId, List.of(myCard));
            }
        }

        // 2. 상대방 명함 → 나에게 전송
        List<CardDTO> theirCards = toUserIds.stream()
                .map(cardMapper::findMainByUserId)
                .filter(Objects::nonNull)
                .toList();

        if (!theirCards.isEmpty()) {
            webSocketHandler.sendCardListToUser(fromUserId, theirCards);
        }
    }

    /**
     * 명함 교환 수락 시 양방향 저장 처리 (중복 방지 포함)
     */
    public void saveCardExchange(String fromUserId, String toUserId, Long acceptedCardId) {
    // 요청자 입장에서: 상대방 명함 저장 (중복 방지)
        if (!cardExchangeMapper.existsExchange(toUserId, fromUserId, acceptedCardId)) {
            CardExchangeDTO e1 = new CardExchangeDTO(null, toUserId, fromUserId, acceptedCardId, "websocket", LocalDateTime.now(), "accepted");
            cardExchangeMapper.insertExchange(e1);

            CardListDTO cardListDTO1 = new CardListDTO();
            cardListDTO1.setUserId(toUserId);
            cardListDTO1.setCardId(acceptedCardId);
            cardListMapper.insertByWebSocket(cardListDTO1);  // ✅ DTO 전달
        }

        // 수락자 입장에서: 요청자의 명함 저장 (중복 방지)
        CardDTO requesterCard = cardMapper.findMainByUserId(toUserId);
        if (requesterCard != null && !cardExchangeMapper.existsExchange(fromUserId, toUserId, requesterCard.getId())) {
            CardExchangeDTO e2 = new CardExchangeDTO(null, fromUserId, toUserId, requesterCard.getId(), "websocket", LocalDateTime.now(), "accepted");
            cardExchangeMapper.insertExchange(e2);

            CardListDTO cardListDTO2 = new CardListDTO();
            cardListDTO2.setUserId(fromUserId);
            cardListDTO2.setCardId(requesterCard.getId());
            cardListMapper.insertByWebSocket(cardListDTO2);  // ✅ DTO 전달
        }
    }

    /**
     * 특정 유저에게 명함 제거 알림 전송
     */
    public void notifyUserRemoval(String userId, List<String> nearbyUserIds) {
        for (String target : nearbyUserIds) {
            webSocketHandler.sendRemoveNotice(target, userId);
        }
    }

    /**
     * ID 기반 교환 정보 조회
     */
    public CardExchangeDTO getById(Long id) {
        return cardExchangeMapper.findById(id);
    }

    /**
     * 유저의 모든 교환 내역 조회
     */
    public List<CardExchangeDTO> getUserExchanges(String userId) {
        return cardExchangeMapper.findAllByUserId(userId);
    }

    /**
     * 상태 업데이트(accepted)
     */
    public void updateStatus(Long id, String status) {
        cardExchangeMapper.updateStatus(id, status);
    }
} 
