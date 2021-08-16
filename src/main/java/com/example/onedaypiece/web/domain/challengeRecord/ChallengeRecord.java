package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@ToString(exclude = {"member","challenge"})
@Table(indexes = {@Index(name = "idx_record_status", columnList = "challenge_record_status")})
public class ChallengeRecord extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeRecordId;

    // 챌린지 참여율이 80% 이상이어서 포인트를 받았는지 여부
    @Column
    private boolean challengePoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // ChallengeProgress가 1 or 2이면 true, 3이면 false
    @Column(name = "challenge_record_status", nullable = false)
    private boolean challengeRecordStatus;

    public ChallengeRecord(Challenge challenge, Member member) {
        this.challengePoint = false;
        this.challenge = challenge;
        this.member = member;
        this.challengeRecordStatus = true;
    }

    public void setStatusFalse() {
        this.challengeRecordStatus = false;
    }
}
