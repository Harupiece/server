package com.example.onedaypiece.web.dto.response.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
public class ChallengeMemberMainResponseDto extends ChallengeGuestMainResponseDto{

    private final Set<ChallengeSliderSourceResponseDto> slider = new LinkedHashSet<>();
    private final List<ChallengeSliderSourceResponseDto> exercise = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> livingHabits = new ArrayList<>();
    private final List<ChallengeSliderSourceResponseDto> noDrinkNoSmoke = new ArrayList<>();

    public void addSlider(ChallengeSliderSourceResponseDto responseDto) {
        slider.add(responseDto);
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

    @Override
    public List<Long> sliderIdList() {
        List<Long> sliderIdList = new ArrayList<>();
        slider.forEach(content -> sliderIdList.add(content.getChallengeId()));
        return sliderIdList;
    }
}
