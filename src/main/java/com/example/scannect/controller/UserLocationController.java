package com.example.scannect.controller;

import com.example.scannect.common.ApiResponse;
import com.example.scannect.common.ResponseService;
import com.example.scannect.dto.UserLocationDTO;
import com.example.scannect.service.UserLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class UserLocationController {

    private final UserLocationService userLocationService;
    private final ResponseService responseService;

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse<?>> activateLocation(@RequestBody UserLocationDTO dto) {
        String userId = dto.getUserId();
        userLocationService.activateLocation(userId, dto);
        dto.setUserId(userId);
        return ResponseEntity.ok(responseService.success(dto, "위치 공유가 활성화되었습니다."));
    }



    @PostMapping("/deactivate")
    public ResponseEntity<ApiResponse<?>> deactivateLocation(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        userLocationService.deactivateLocation(userId);

        Map<String, Object> responseBody = Map.of("userId", userId);
        return ResponseEntity.ok(responseService.success(responseBody, "위치 공유가 비활성화되었습니다."));
    }

}

