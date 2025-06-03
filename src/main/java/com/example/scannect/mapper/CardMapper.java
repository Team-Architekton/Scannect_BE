package com.example.scannect.mapper;

import com.example.scannect.dto.CardDTO;

import java.util.List;

public interface CardMapper {
    //개별 명함 조회
    CardDTO findById(Long id);

    //명함 전체 리스트 조회
    List<CardDTO> findAllByUserId(String userId);

    //유저 별 명함 개수
    int countByUserId(String userId);

    //메인 명함 조회
    CardDTO findMainByUserId(String userId);

    //명함 생성
    void insert(CardDTO card);

    //명함 수정
    int update(CardDTO card);

    //명함 개별 삭제
    void deleteById(Long id);

    //명함 전체 삭제
    void deleteAllByUserId(String userId);

    //유저의 메인 명함을 모두 is_main = false로 바꿈
    void unsetMainCard(String userId);

    //특정 명함을 is_main = true로 설정
    void setMainCardById(Long id);
}
