package com.example.scannect.mapper;

import com.example.scannect.dto.UserLocationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserLocationMapper {

    // 위치 저장 (UPSERT)
    void upsertUserLocation(UserLocationDTO dto);

    // 10m 반경 내 활성화된 유저 찾기
    List<UserLocationDTO> findNearbyUsers(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("myId") String myId
    );

    // 위치 비활성화 (토글 OFF)
    void setUserInactive(@Param("userId") String userId);

    UserLocationDTO findByUserId(@Param("userId") String userId);
}
