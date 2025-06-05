package com.example.scannect.dto;

import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Data
public class CardDTO {
    private Long id; //명함 Id
    private String userId; //명함 생성한 유저의 아이디 (FK)
    private String cardName; //생성 명함 이름
    private String nickname; //유저 닉네임
    private String email; //이메일
    private String job; //직업 or 직무
    private String industry; //업종
    private String belongTo; //회사 또는 학교 -> 소속
    private String department; //부서
    private String position; //직급
    private String content; //자기소개
    private String companyTel; //회사전화번호
    private String phoneNum; //폰 번호
    private String imgUrl; //이미지 URL
    private String colour; //명함 색상 ->HEX 코드
    private List<String> urlList;
    private Boolean is_active; //0:숨기기, 1:활성화
    private Boolean is_main;
    private Timestamp createdAt; //생성 일자
    private Timestamp updatedAt; //수정 일자
}
