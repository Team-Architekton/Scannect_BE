package com.example.scannect.controller;

import com.example.scannect.common.ApiResponse;
import com.example.scannect.common.ResponseService;
import com.example.scannect.dto.CardExchangeDTO;
import com.example.scannect.service.CardExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange")
public class CardExchangeController {

    private final CardExchangeService cardExchangeService;
    private final ResponseService responseService;

    @PostMapping("/accept")
    public ResponseEntity<ApiResponse<?>> acceptExchange(@RequestBody CardExchangeDTO dto) {
        cardExchangeService.saveCardExchange(dto.getFromUserId(), dto.getToUserId(), dto.getCardId());
        return ResponseEntity.ok(responseService.success(dto, "명함 교환이 완료되었습니다."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getExchange(@PathVariable Long id) {
        CardExchangeDTO dto = cardExchangeService.getById(id);
        return ResponseEntity.ok(responseService.success(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserExchanges(@PathVariable String userId) {
        List<CardExchangeDTO> list = cardExchangeService.getUserExchanges(userId);
        return ResponseEntity.ok(responseService.success(list));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateStatus(@PathVariable Long id,
                                                       @RequestParam String status) {
        cardExchangeService.updateStatus(id, status);
        return ResponseEntity.ok(responseService.success(null, "상태가 업데이트되었습니다."));
    }
} 
