package com.example.onedaypiece.web.dto.response.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeListResponseDto {
    private final List<ChallengeResponseDto> result = new ArrayList<>();

    public void addResult(ChallengeResponseDto responseDto) {
        this.result.add(responseDto);
    }
}
