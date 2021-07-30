package com.example.onedaypiece.web.dto.response.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeGuestMainResponseDto {

    private final List<ChallengeSliderSourceResponseDto> popular = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> exercise = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> livingHabits = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> noDrinkNoSmoke = new ArrayList<>();

    public void addPopular(ChallengeSliderSourceResponseDto responseDto) {
        popular.add(responseDto);
    }
    public void addExercise(ChallengeSliderSourceResponseDto responseDto) {
        exercise.add(responseDto);
    }
    public void addLivingHabits(ChallengeSliderSourceResponseDto responseDto) {
        livingHabits.add(responseDto);
    }
    public void addNoDrinkNoSmoke(ChallengeSliderSourceResponseDto responseDto) {
        noDrinkNoSmoke.add(responseDto);
    }
}
