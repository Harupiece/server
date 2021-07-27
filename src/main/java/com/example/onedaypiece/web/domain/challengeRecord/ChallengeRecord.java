package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ChallengeRecord extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long challengeRecordId;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
