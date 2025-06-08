package com.example.scannect.common;

import com.example.scannect.dto.CardDTO;
import com.example.scannect.dto.CardExchangeDTO;
import com.example.scannect.mapper.CardExchangeMapper;
import com.example.scannect.service.CardListService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final CardExchangeMapper cardExchangeMapper;
    private final CardListService cardListService;

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessionMap.put(userId, session);
            System.out.println("WebSocket 연결됨: " + userId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessionMap.remove(userId);
        }
    }

    public void sendCardListToUser(String toUserId, List<CardDTO> cardList) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "type", "cardList",
                    "cards", cardList
            ));
            sendMessageToUser(toUserId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRemoveNotice(String toUserId, String removedUserId) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "type", "remove",
                    "userId", removedUserId
            ));
            sendMessageToUser(toUserId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToUser(String userId, String payload) {
        WebSocketSession session = sessionMap.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                System.out.println("메시지 전송 실패: " + userId);
            }
        }
    }

    public void sendNotify(String toUserId, String message) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "type", "notify",
                    "message", message
            ));
            sendMessageToUser(toUserId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendError(String toUserId, String errorMessage) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "type", "error",
                    "message", errorMessage
            ));
            sendMessageToUser(toUserId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        try {
            JsonNode node = objectMapper.readTree(textMessage.getPayload());
            String type = node.get("type").asText();

            if ("request".equals(type)) {
                String fromUserId = node.get("fromUserId").asText();
                String toUserId = node.get("toUserId").asText();
                sendMessageToUser(toUserId, textMessage.getPayload());
            } else if ("response".equals(type)) {
                String status = node.get("status").asText();
                String fromUserId = node.get("fromUserId").asText();
                String toUserId = node.get("toUserId").asText();

                if ("accept".equalsIgnoreCase(status)) {
                    Long fromCardId = node.get("fromCardId").asLong();
                    Long toCardId = node.get("toCardId").asLong();

                    // 교환 요청자 입장에서: 수락자의 명함 저장
                    CardExchangeDTO e1 = new CardExchangeDTO(null, toUserId, fromUserId, fromCardId, "websocket", LocalDateTime.now(), "accepted");
                    cardExchangeMapper.insertExchange(e1);
                    cardListService.insertByWebSocket(toUserId, fromCardId);
                    sendNotify(toUserId, fromUserId + "님의 명함이 저장되었습니다.");

                    // 수락자 입장에서: 요청자의 명함 저장
                    CardExchangeDTO e2 = new CardExchangeDTO(null, fromUserId, toUserId, toCardId, "websocket", LocalDateTime.now(), "accepted");
                    cardExchangeMapper.insertExchange(e2);
                    cardListService.insertByWebSocket(fromUserId, toCardId);

                    sendNotify(fromUserId, toUserId + "님의 명함이 저장되었습니다.");

                } else if ("reject".equalsIgnoreCase(status)) {
                    sendNotify(toUserId, fromUserId + "님이 명함 교환을 거절했습니다.");
                }

                //sendMessageToUser(toUserId, textMessage.getPayload());
            }

        } catch (Exception e) {
            e.printStackTrace();
            String userId = (String) session.getAttributes().get("userId");
            sendError(userId, "메시지 처리 중 오류가 발생했습니다.");
        }
    }
}
