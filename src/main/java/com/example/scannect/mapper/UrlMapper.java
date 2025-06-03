package com.example.scannect.mapper;

import com.example.scannect.dto.UrlDTO;

import java.util.List;

public interface UrlMapper {

    void insert(UrlDTO dto);

    // 여러 개의 URL을 한 번에 삽입하는 메서드
    void insertAll(List<UrlDTO> urls); // List<UrlDTO>로 수정


    List<UrlDTO> findByCardId(Long cardId);

    void deleteById(Long id);

    void deleteByCardId(Long cardId);  // card 단위 전체 삭제

}
