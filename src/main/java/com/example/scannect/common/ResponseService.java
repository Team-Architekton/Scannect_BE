package com.example.scannect.common;

import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    public <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    public <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    public ApiResponse<?> successMessage(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public ApiResponse<?> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
