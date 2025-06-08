package com.example.scannect.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load(); // .env 로딩
    }

    @Bean
    public RestTemplate template(Dotenv dotenv) {
        String apiKey = dotenv.get("OPENAI_API_KEY"); // .env에서 직접 꺼냄
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().set("Authorization", "Bearer " + apiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}