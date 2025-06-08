package com.example.scannect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.scannect.dto.TextMessage;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    private int index;
    private TextMessage message;
}