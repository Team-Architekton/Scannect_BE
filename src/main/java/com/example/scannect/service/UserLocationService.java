package com.example.scannect.service;

import com.example.scannect.dto.UserLocationDTO;
import com.example.scannect.mapper.UserLocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLocationService {

    private final UserLocationMapper userLocationMapper;
    private final CardExchangeService cardExchangeService;

    /**
     * 위치 공유 ON: 위치 저장 + 주변 유저 탐색 + 명함 교환 위임
     */
    @Transactional
    public void activateLocation(String userId, UserLocationDTO dto) {
        // 1. 위치 저장
        dto.setUserId(userId);
        userLocationMapper.upsertUserLocation(dto);

        // ✅ 저장한 위치 정보 다시 조회해서 DTO에 채움
        UserLocationDTO updated = userLocationMapper.findByUserId(userId);
        dto.setId(updated.getId());
        dto.setIsActive(updated.getIsActive());
        dto.setCreatedAt(updated.getCreatedAt());
        dto.setUpdatedAt(updated.getUpdatedAt());

        // 2. 반경 10m 이내 유저 탐색
        List<UserLocationDTO> nearbyUsers =
                userLocationMapper.findNearbyUsers(dto.getLatitude(), dto.getLongitude(), userId);

        // 3. 명함 교환 위임
        List<String> toUserIds = nearbyUsers.stream()
                .map(UserLocationDTO::getUserId)
                .toList();

        cardExchangeService.exchangeViaWebSocket(userId, toUserIds);
    }


    /**
     * 위치 공유 OFF: is_active = false로 변경
     */
    @Transactional
    public void deactivateLocation(String userId) {
        userLocationMapper.setUserInactive(userId);
    }

    

}
