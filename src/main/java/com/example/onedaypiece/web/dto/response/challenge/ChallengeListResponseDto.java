package com.example.onedaypiece.web.dto.response.challenge;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeListResponseDto {

    private List<ChallengeResponseDto> challengeList = new ArrayList<>();
    private boolean hasNext;

    @Builder
    public ChallengeListResponseDto(List<ChallengeResponseDto> challengeList, boolean hasNext) {
        this.challengeList = challengeList;
        this.hasNext = hasNext;
    }

    public static ChallengeListResponseDto createChallengeListDto(List<ChallengeResponseDto> dto, boolean hasNext) {
        return ChallengeListResponseDto.builder()
                .challengeList(dto)
                .hasNext(hasNext)
                .build();
    }
}
