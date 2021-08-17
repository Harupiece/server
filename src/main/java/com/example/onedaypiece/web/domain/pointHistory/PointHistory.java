package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.posting.Posting;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="posting_id")
    private Posting posting;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_record_id")
    private ChallengeRecord challengeRecord;

    // 상태 아직안쓴거면 true / 사용했으면 false
    @Column
    private boolean status;

    public PointHistory(Long getPoint, Posting posting){
        this.getPoint = getPoint;
        this.posting = posting;
        this.status = true;
    }

    public PointHistory(Long getPoint, ChallengeRecord challengeRecord) {
        this.getPoint = getPoint;
        this.challengeRecord = challengeRecord;
        this.status = true;
    }
}
