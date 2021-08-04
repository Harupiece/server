package com.example.onedaypiece.web.dto.query;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MainPageQueryDto {

    private Challenge challenge;
    private LocalDateTime modifiedAt;
    private ChallengeRecord challengeRecord;
    private Long memberId;
}
