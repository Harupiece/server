package com.example.onedaypiece.web.dto.response.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Getter
public class ChallengeMemberMainResponseDto extends ChallengeGuestMainResponseDto {

    private final List<ChallengeSliderSourceResponseDto> slider = new ArrayList<>();
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

    @Override
    public void addSlider(List<ChallengeSliderSourceResponseDto> sliderSource) {
        this.slider.addAll(sliderSource);
    }

}
