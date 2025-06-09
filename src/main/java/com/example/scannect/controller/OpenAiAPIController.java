package com.example.scannect.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import com.example.scannect.dto.response.ChatGPTResponse;
import com.example.scannect.service.AiCallService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OpenAiAPIController {
    private final AiCallService aiCallService;

    @PostMapping("/image")
    public String imageAnalysis(@RequestParam MultipartFile image) throws IOException {
        String requestText = """
                다음 이미지는 한국어 명함입니다.  
                이 명함 이미지에서 텍스트 정보를 추출한 뒤, 아래 명세에 따라 JSON 객체로 반환해주세요.

                JSON의 키는 다음과 같고, 명함에 적힌 정보를 각 항목에 최대한 정확하게 매핑해주세요:

                - cardName: null (명함 자체의 이름은 존재하지 않으면 null)
                - nickname: null (유저의 닉네임이 기재된 경우)
                - email: 명함에 적힌 이메일 주소
                - job: 명함에 적힌 직업 또는 직무 (예: "Backend Developer", "마케팅 팀장")
                - industry: 업종 또는 회사 분야 (예: "정보통신업", "교육 서비스업")
                - belongTo: 소속된 회사 또는 학교 이름
                - department: 부서명
                - position: 직급 (예: "팀장", "매니저", "대표")
                - content: 자기소개 문구가 있다면 전체 문장
                - companyTel: 명함에 기재된 회사 전화번호
                - phoneNum: 개인 핸드폰 번호
                - imgUrl: null (이미지 저장 URL은 추출 불가하므로 null)
                - colour: null (이미지 색상은 분석하지 않으므로 null)
                - urlList: 명함에 기재된 웹사이트 주소들을 배열로 추출 (없으면 빈 배열 [])

                ⚠️ 주의사항:
                - JSON 형식만 반환해주세요. 불필요한 설명 없이 딱 JSON만 응답으로 주세요.
                - 명함에 없는 정보는 null 또는 빈 문자열로 처리해주세요.
                - 이메일/전화번호/회사명 등은 OCR로 읽을 수 있는 최대한 정확하게 추출해주세요.

                예상 출력 예시:

                {
                "cardName": null,
                "nickname": "우쨍",
                "email": "woo.jaeng@company.com",
                "job": "프론트엔드 개발자",
                "industry": "IT 서비스",
                "belongTo": "스캐넥트 주식회사",
                "department": "기획실",
                "position": "팀장",
                "content": "창의적인 기획으로 세상을 바꿉니다.",
                "companyTel": "02-123-4567",
                "phoneNum": "010-9876-5432",
                "imgUrl": null,
                "colour": null,
                "urlList": ["https://scannect.kr", "https://woojaeng.dev"]
                }

                딱 이 Json 형식으로만 response해줘.
                """;
        ChatGPTResponse response = aiCallService.requestImageAnalysis(image, requestText);
        return response.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/text")
    public String textAnalysis(@RequestParam String requestText) {
        ChatGPTResponse response = aiCallService.requestTextAnalysis(requestText);
        return response.getChoices().get(0).getMessage().getContent();
    }
}