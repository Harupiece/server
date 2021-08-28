package com.example.onedaypiece.web.dto.request.challengeRecord;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeRecordRequestDto {
    private Long challengeId;
    private String challengePassword;
}
