package com.example.scannect.service;

import com.example.scannect.dto.UrlDTO;
import com.example.scannect.mapper.UrlMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    private final UrlMapper urlMapper;

    public UrlService(UrlMapper urlMapper) {
        this.urlMapper = urlMapper;
    }

    // 단건(URL) 저장
    public void insert(UrlDTO dto) {
        urlMapper.insert(dto);
    }

    // 여러 개 저장 (명함 생성 시 사용)
    public void insertAll(Long cardId, List<String> urlList) {
        if (urlList == null || urlList.isEmpty()) return;
        for (String url : urlList) {
            UrlDTO dto = new UrlDTO();
            dto.setCardId(cardId);
            dto.setUrl(url);
            urlMapper.insert(dto);
        }
    }

    // cardId 기준 전체 조회
    public List<UrlDTO> getByCardId(Long cardId) {
        return urlMapper.findByCardId(cardId);
    }

    // 단건 삭제
    public void delete(Long id) {
        urlMapper.deleteById(id);
    }

    // 명함 수정 시: 기존 URL 전부 삭제 후 재등록
    public void replaceUrls(Long cardId, List<String> urlList) {
        urlMapper.deleteByCardId(cardId);
        insertAll(cardId, urlList);
    }
}
