package com.example.scannect.controller;

import com.example.scannect.common.ApiResponse;
import com.example.scannect.common.ResponseService;
import com.example.scannect.dto.CardDTO;
import com.example.scannect.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final ResponseService responseService;

    public CardController(CardService cardService, ResponseService responseService) {
        this.cardService = cardService;
        this.responseService = responseService;
    }

    // ✅ 개별 명함 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CardDTO>> getCard(@PathVariable Long id) {
        CardDTO card = cardService.getCardById(id);
        return ResponseEntity.ok(responseService.success(card, "명함 조회 완료"));
    }

    // ✅ 유저의 전체 명함 목록 (최신순 정렬)
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getUserCards(@PathVariable String userId) {
        List<CardDTO> cards = cardService.getCardsByUserId(userId);
        return ResponseEntity.ok(responseService.success(cards, "명함 목록 조회"));
    }

    // ✅ 대표 명함 설정
    @PatchMapping("/{id}/main")
    public ResponseEntity<ApiResponse<?>> setMainCard(@PathVariable Long id, @RequestParam String userId) {
        cardService.setMainCard(userId, id);
        return ResponseEntity.ok(responseService.successMessage("대표 명함 설정 완료"));
    }

    // ✅ 명함 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CardDTO>> createCard(@RequestBody CardDTO card) {
        cardService.createCard(card);  // 카드 생성
        return ResponseEntity.ok(responseService.success(card, "명함이 저장되었습니다."));  // 생성된 카드 데이터 반환
    }

    // ✅ 명함 수정
    @PutMapping
    public ResponseEntity<ApiResponse<CardDTO>> updateCard(@RequestBody CardDTO card) {
        cardService.updateCard(card);  // 명함 수정
        return ResponseEntity.ok(responseService.success(card, "명함이 수정되었습니다."));  // 수정된 명함 데이터 반환
    }

    // ✅ 개별 명함 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        cardService.deleteById(id);  // 명함 삭제
        return ResponseEntity.ok(responseService.successMessage("명함 삭제 완료"));
    }

    // ✅ 전체 명함 삭제
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteByUserId(@PathVariable String userId) {
        cardService.deleteByUserId(userId);  // 사용자의 모든 명함 삭제
        return ResponseEntity.ok(responseService.successMessage("전체 명함이 삭제되었습니다."));
    }
}
