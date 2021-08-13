package com.example.onedaypiece.web.dto.query.posting;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SchedulerIdListDto {
    private Long challengeId;
    private Long memberId;

    @QueryProjection
    public SchedulerIdListDto(Long challengeId, Long memberId) {
        this.challengeId = challengeId;
        this.memberId = memberId;
    }
}
