package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ChallengeMainResponseDto {

    private List<ChallengeSliderSourceResponseDto> slider;
    private final List<ChallengeSliderSourceResponseDto> popular = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> exercise = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> livinghabits = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> nodrinknosmoke = new ArrayList<>();

    public void addExercise(ChallengeSliderSourceResponseDto responseDto) {
        exercise.add(responseDto);
    }

    public void addLivingHabits(ChallengeSliderSourceResponseDto responseDto) {
        livinghabits.add(responseDto);
    }

    public void addNoDrinkNoSmoke(ChallengeSliderSourceResponseDto responseDto) {
        nodrinknosmoke.add(responseDto);
    }

    public void addPopular(List<ChallengeRecord> popularSource) {
        this.popular.addAll(popularSource
                .stream()
                .map(record -> (new ChallengeSliderSourceResponseDto(record.getChallenge(), popularSource)))
                .collect(Collectors.toList()));
    }

    public void addSlider(List<ChallengeSliderSourceResponseDto> sliderSource) {
        this.slider = new ArrayList<>();
        this.slider.addAll(sliderSource);
    }

}
