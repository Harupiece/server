package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ChallengeMainResponseDto {

    private final List<ChallengeSourceResponseDto> slider = new ArrayList<>();
    private final List<ChallengeSourceResponseDto> popular = new ArrayList<>();
    private final List<ChallengeSourceResponseDto> exercise = new ArrayList<>();
    private final List<ChallengeSourceResponseDto> livinghabits = new ArrayList<>();
    private final List<ChallengeSourceResponseDto> nodrinknosmoke = new ArrayList<>();

    public static ChallengeMainResponseDto createChallengeMainResponseDto() {
        return new ChallengeMainResponseDto();
    }

    public void addExercise(ChallengeSourceResponseDto responseDto) {
        exercise.add(responseDto);
    }

    public void addLivingHabits(ChallengeSourceResponseDto responseDto) {
        livinghabits.add(responseDto);
    }

    public void addNoDrinkNoSmoke(ChallengeSourceResponseDto responseDto) {
        nodrinknosmoke.add(responseDto);
    }

    public void addPopular(List<ChallengeRecord> popularSource, List<ChallengeRecord> records) {
        this.popular.addAll(popularSource
                .stream()
                .map(record -> (ChallengeSourceResponseDto.createChallengeSourceResponseDto(record.getChallenge(), records)))
                .collect(Collectors.toList()));
    }

    public void addSlider(List<ChallengeSourceResponseDto> sliderSource) {
        this.slider.addAll(sliderSource);
    }
}
