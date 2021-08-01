package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ChallengeMainResponseDto {

    private final Set<ChallengeSourceResponseDto> slider = new HashSet<>();
    private final Set<ChallengeSourceResponseDto> popular = new HashSet<>();
    private final Set<ChallengeSourceResponseDto> exercise = new HashSet<>();
    private final Set<ChallengeSourceResponseDto> livinghabits = new HashSet<>();
    private final Set<ChallengeSourceResponseDto> nodrinknosmoke = new HashSet<>();

    public void addExercise(ChallengeSourceResponseDto responseDto) {
        exercise.add(responseDto);
    }

    public void addLivingHabits(ChallengeSourceResponseDto responseDto) {
        livinghabits.add(responseDto);
    }

    public void addNoDrinkNoSmoke(ChallengeSourceResponseDto responseDto) {
        nodrinknosmoke.add(responseDto);
    }

    public void addPopular(List<ChallengeRecord> popularSource) {
        this.popular.addAll(popularSource
                .stream()
                .map(record -> (new ChallengeSourceResponseDto(record.getChallenge(), popularSource)))
                .collect(Collectors.toList()));
    }

    public void addSlider(List<ChallengeSourceResponseDto> sliderSource) {
        this.slider.addAll(sliderSource);
    }

}
