package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.common.Timestamped;
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

    @OneToOne
    @JoinColumn(name="certification_id")
    private Certification certification;

    @OneToOne
    @JoinColumn(name="challenge_record_id")
    private ChallengeRecord challengeRecord;

    // 상태 아직안쓴거면 true / 사용했으면 false
    @Column
    private boolean status;

    public PointHistory(Long getPoint, Certification certification){
        this.getPoint = getPoint;
        this.certification = certification;
        this.status = true;
    }

    public PointHistory(Long getPoint, ChallengeRecord challengeRecord) {
        this.getPoint = getPoint;
        this.challengeRecord = challengeRecord;
        this.status = true;
    }


}
