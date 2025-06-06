package com.example.scannect.common;

import lombok.Data;
import java.util.List;

@Data
public class OcrResponse {
    private List<OcrImage> images;

    @Data
    public static class OcrImage {
        private List<OcrField> fields;
    }

    @Data
    public static class OcrField {
        private String name;
        private String inferText;
    }
}
