package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.dto.response.challenge.ChallengeSourceResponseDto.createChallengeSourceResponseDto;

@NoArgsConstructor
@Getter
public class ChallengeMainResponseDto {

    private List<ChallengeSourceResponseDto> slider = new ArrayList<>();
    private List<ChallengeSourceResponseDto> popular = new ArrayList<>();
    private List<ChallengeSourceResponseDto> exercise = new ArrayList<>();
    private List<ChallengeSourceResponseDto> livinghabits = new ArrayList<>();
    private List<ChallengeSourceResponseDto> nodrinknosmoke = new ArrayList<>();

    @Builder
    public ChallengeMainResponseDto(List<ChallengeSourceResponseDto> slider,
                                    List<ChallengeSourceResponseDto> popular,
                                    List<ChallengeSourceResponseDto> exercise,
                                    List<ChallengeSourceResponseDto> livinghabits,
                                    List<ChallengeSourceResponseDto> nodrinknosmoke) {
        this.slider = slider;
        this.popular = popular;
        this.exercise = exercise;
        this.livinghabits = livinghabits;
        this.nodrinknosmoke = nodrinknosmoke;
    }

    public static ChallengeMainResponseDto createChallengeMainResponseDto() {
        return ChallengeMainResponseDto.builder()
                .build();
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
                .map(record -> (createChallengeSourceResponseDto(record.getChallenge(), records)))
                .collect(Collectors.toList()));
    }

    public void addSlider(List<ChallengeSourceResponseDto> sliderSource) {
        this.slider.addAll(sliderSource);
    }
}
