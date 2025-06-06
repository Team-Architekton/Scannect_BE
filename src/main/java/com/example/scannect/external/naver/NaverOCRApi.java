package com.example.scannect.external.naver;

import com.example.scannect.dto.CardDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class NaverOCRApi {

    @Value("${naver.service.url}")
    private String url;

    public CardDTO callApi(String type, String filePath, String naver_secretKey, String ext) {
        CardDTO cardDTO = new CardDTO();
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod(type);
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", naver_secretKey);

            // JSON body 생성
            String jsonBody = String.format("{\"version\":\"V2\",\"requestId\":\"%s\",\"timestamp\":%d,\"images\":[{\"format\":\"%s\",\"name\":\"demo\"}]}",
                    UUID.randomUUID(), System.currentTimeMillis(), ext);

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            File file = new File(filePath);
            writeMultiPart(wr, jsonBody, file, boundary);
            wr.close();

            int responseCode = con.getResponseCode();
            InputStream inputStream = (responseCode == 200) ? con.getInputStream() : con.getErrorStream();
            String response = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .reduce("", (acc, line) -> acc + line);

            cardDTO = parseResponseToCardDTO(response);

        } catch (Exception e) {
            log.error("OCR API 호출 중 오류 발생", e);
        }
        return cardDTO;
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage).append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder filePart = new StringBuilder();
            filePart.append("Content-Disposition:form-data; name=\"file\"; filename=\"")
                    .append(file.getName()).append("\"\r\n");
            filePart.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(filePart.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

    private CardDTO parseResponseToCardDTO(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OcrResponse response = mapper.readValue(json, OcrResponse.class);
        CardDTO dto = new CardDTO();

        response.getImages().stream().findFirst().ifPresent(image -> {
            for (OcrField field : image.getFields()) {
                switch (field.getName()) {
                    case "name": dto.setName(field.getInferText()); break;
                    case "company": dto.setCompany(field.getInferText()); break;
                    case "tel": dto.setPhone(field.getInferText()); break;
                    case "email": dto.setEmail(field.getInferText()); break;
                    case "position": dto.setPosition(field.getInferText()); break;
                    default: log.debug("Unmapped field: {}", field.getName());
                }
            }
        });

        return dto;
    }
}
