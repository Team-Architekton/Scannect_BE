package com.example.scannect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.scannect.mapper")
public class ScannectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScannectApplication.class, args);
    }
}
