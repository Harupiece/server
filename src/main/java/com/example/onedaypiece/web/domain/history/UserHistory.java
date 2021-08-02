package com.example.onedaypiece.web.domain.history;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class UserHistory extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long HistoryId;

    @Column
    private String content;

    // 유저가 히스토리를 확인했는지 여부
    @Column
    private boolean checkStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public UserHistory(Member member) {
        this.member = member;
        this.checkStatus = false;
        this.content = "챌린지 타이틀을 생성해주세요.";
    }

    public void setCheckStatusTrue() {
        this.checkStatus = true;
    }

    public void setContentWhenChallengeStart(Challenge challenge) {
        this.content = challenge.getChallengeTitle() + " 챌린지가 시작되었어요!";
    }

    public void setContentWhenChallengeEnd(Challenge challenge) {
        this.content = challenge.getChallengeTitle() + " 챌린지가 끝났어요!";
    }

    public void setContentEarnPointWhenChallengeEnd(Challenge challenge) {
        this.content = challenge.getChallengeTitle() + " 챌린지의 완료 보상으로 " + "xxxx" + "의 조각을 획득했어요!";
    }
}
