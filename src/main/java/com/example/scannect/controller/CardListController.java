package com.example.scannect.controller;

import com.example.scannect.common.ApiResponse;
import com.example.scannect.common.ResponseService;
import com.example.scannect.controller.CardListController.MemoUpdateRequest;
import com.example.scannect.dto.CardDTO;
import com.example.scannect.dto.CardListDTO;
import com.example.scannect.service.CardListService;
import com.example.scannect.service.CardService;

import lombok.Data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/card-list")
public class CardListController {

    private final CardListService cardListService;
    private final CardService cardService;
    private final ResponseService responseService;

    public CardListController(CardListService cardListService, ResponseService responseService, CardService cardService) {
        this.cardListService = cardListService;
        this.responseService = responseService;
        this.cardService = cardService;
    }

    // 1. 명함 저장
    @PostMapping()
    public ResponseEntity<ApiResponse<?>> save(@RequestBody CardListDTO dto) {
        System.out.println("🔥 userId: " + dto.getUserId()); // null인지 확인!ㅎ
        cardListService.save(dto);
        return ResponseEntity.ok(responseService.success(dto, "명함이 저장되었습니다."));
    }
    
    // 1. 명함 저장(웹소켓)
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> insertByWebSocket(@RequestBody CardListDTO dto) {
        cardListService.insertByWebSocket(dto.getUserId(), dto.getCardId());
        return ResponseEntity.ok(responseService.success(dto, "명함이 저장되었습니다."));
    }

    @PostMapping("/save/ocr/{userId}")
    public ResponseEntity<ApiResponse<?>> insertByOCR(@PathVariable String userId, @RequestBody CardDTO dto) {
        cardService.createCard(dto);

        Long cardId = dto.getId();
        CardListDTO cardListDTO = new CardListDTO();
        cardListDTO.setCardId(cardId);
        cardListDTO.setUserId(userId);
        cardListService.save(cardListDTO);

        return ResponseEntity.ok(responseService.success(cardListDTO, "명함이 저장되었습니다."));
    }

    @Data
    public static class MemoUpdateRequest {
        private String memo;
        // getter, setter
    }
    // 2. 메모 수정
    @PatchMapping("/{id}/memo")
    public ResponseEntity<ApiResponse<?>> updateMemo(@PathVariable Long id, @RequestBody MemoUpdateRequest request) {
        cardListService.updateMemo(id, request.getMemo());
        return ResponseEntity.ok(responseService.successMessage("메모가 수정되었습니다."));
    }


    // 3. 숨기기 / 숨기기 해제
    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<?>> updateActive(@PathVariable Long id, @RequestParam boolean isActive) {
        cardListService.updateActive(id, isActive);
        return ResponseEntity.ok(responseService.successMessage("숨김 상태가 변경되었습니다."));
    }

    // 4. 중요 표시 / 해제
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<?>> updateFavorite(@PathVariable Long id, @RequestParam boolean favorite) {
        cardListService.updateFavorite(id, favorite);
        return ResponseEntity.ok(responseService.successMessage("중요 표시가 변경되었습니다."));
    }


    // 5. 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        cardListService.delete(id);
        return ResponseEntity.ok(responseService.successMessage("명함이 삭제되었습니다."));
    }

    // 6. 전체 리스트 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAll(@PathVariable String userId) {
        List<Map<String, Object>> list = cardListService.getAll(userId);
        return ResponseEntity.ok(responseService.success(list, "전체 명함 목록 조회"));
    }

    // 7. 중요 명함 리스트
    @GetMapping("/user/{userId}/favorites")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getFavorites(@PathVariable String userId) {
        List<CardDTO> list = cardListService.getFavorites(userId);
        return ResponseEntity.ok(responseService.success(list, "중요 명함 목록 조회"));
    }

    // 8. 숨김 명함 리스트
    @GetMapping("/user/{userId}/hidden")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getHidden(@PathVariable String userId) {
        List<CardDTO> list = cardListService.getHidden(userId);
        return ResponseEntity.ok(responseService.success(list, "숨김 명함 목록 조회"));
    }

    // 9. 키워드 검색
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> search(
            @PathVariable String userId,
            @RequestParam String keyword
    ) {
        List<Map<String, Object>> list = cardListService.search(userId, keyword);
        return ResponseEntity.ok(responseService.success(list, "명함 검색 결과"));
    }
}
