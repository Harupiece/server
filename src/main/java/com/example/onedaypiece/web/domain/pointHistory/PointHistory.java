package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PointHistory extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="pointhistory_id")
    private Long pointHistoryId;

    @Column
    private Long getPoint;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="posting_id")
    private Posting posting;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_record_id")
    private ChallengeRecord challengeRecord;

    // 상태 아직안쓴거면 true / 사용했으면 false
    @Column
    private boolean status;


    @Builder
    public PointHistory(Long pointHistoryId, Long getPoint, Posting posting, ChallengeRecord challengeRecord, boolean status) {
        this.pointHistoryId = pointHistoryId;
        this.getPoint = getPoint;
        this.posting = posting;
        this.challengeRecord = challengeRecord;
        this.status = status;
    }

    public static PointHistory createPostingPointHistory(Long getPoint, Posting posting) {
        return PointHistory.builder()
                .getPoint(getPoint)
                .posting(posting)
                .build();
    }

    public static PointHistory createChallengePointHistory(Long resultPoint, ChallengeRecord r) {
        return PointHistory.builder()
                .getPoint(resultPoint)
                .challengeRecord(r)
                .build();
    }

    public void updateStatus() {
        this.status = false;
    }
}
