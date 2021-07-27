package com.example.onedaypiece.web.dto.response.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeMemberMainResponseDto extends ChallengeGuestMainResponseDto{

    private List<ChallengeSliderSourceResponseDto> popular;
    private List<ChallengeSliderSourceResponseDto> myList;
    private List<ChallengeSliderSourceResponseDto> exercise;
    private List<ChallengeSliderSourceResponseDto> livingHabits;
    private List<ChallengeSliderSourceResponseDto> study;
    private List<ChallengeSliderSourceResponseDto> money;

    public void addPopular(ChallengeSliderSourceResponseDto responseDto) {
        popular.add(responseDto);
    }
    public void addMyList(ChallengeSliderSourceResponseDto responseDto) {
        myList.add(responseDto);
    }
    public void addExercise(ChallengeSliderSourceResponseDto responseDto) {
        exercise.add(responseDto);
    }
    public void addLivingHabits(ChallengeSliderSourceResponseDto responseDto) {
        livingHabits.add(responseDto);
    }
    public void addStudy(ChallengeSliderSourceResponseDto responseDto) {
        study.add(responseDto);
    }
    public void addMoney(ChallengeSliderSourceResponseDto responseDto) {
        money.add(responseDto);
    }
}
