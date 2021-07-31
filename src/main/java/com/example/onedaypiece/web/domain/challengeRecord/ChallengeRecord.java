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
@ToString
@Table(indexes = {@Index(name = "idx_record_status", columnList = "challenge_record_status")})
public class ChallengeRecord extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long challengeRecordId;

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
        this.challenge = challenge;
        this.member = member;
        this.challengeRecordStatus = true;
    }

    public void setChallengeRecordStatusToFalse() {
        this.challengeRecordStatus = false;
    }
}
