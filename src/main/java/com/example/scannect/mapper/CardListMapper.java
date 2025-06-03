package com.example.scannect.mapper;

import com.example.scannect.dto.CardListDTO;

import java.util.List;

public interface CardListMapper {

    // 저장
    void insert(CardListDTO dto);

    // 웹소켓 저장
    void insertByWebSocket(CardListDTO dto);

    // 메모 수정
    void updateMemo(Long id, String memo);

    // 숨기기 / 해제
    void updateActive(Long id, Boolean isActive);

    // 중요 표시 / 해제
    void updateFavorite(Long id, Boolean favorite);

    // 삭제
    void delete(Long id);

    // 전체 리스트
    List<CardListDTO> findAllByUserId(String userId);

    // 중요 리스트
    List<CardListDTO> findFavoritesByUserId(String userId);

    // 숨김 리스트
    List<CardListDTO> findHiddenByUserId(String userId);

    // 키워드 검색
    List<CardListDTO> searchByKeyword(String userId, String keyword);
}

