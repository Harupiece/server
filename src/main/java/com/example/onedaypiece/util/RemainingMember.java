package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.posting.Posting;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class RemainingMember {

    private Posting posting;
    private Long challengeRecordId;
    private boolean challengeRecordStatus;


    @QueryProjection
    public RemainingMember(Posting posting, Long challengeRecordId, boolean challengeRecordStatus) {
        this.posting = posting;
        this.challengeRecordId = challengeRecordId;
        this.challengeRecordStatus = challengeRecordStatus;
    }
}
