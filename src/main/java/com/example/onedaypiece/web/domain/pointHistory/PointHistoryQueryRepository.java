package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.challenge.QChallenge;
import com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord;
import com.example.onedaypiece.web.domain.member.QMember;
import com.example.onedaypiece.web.domain.point.QPoint;
import com.example.onedaypiece.web.domain.posting.QPosting;
import com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryDto;
import com.example.onedaypiece.web.dto.response.mypage.histroy.QMemberHistoryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.*;
import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.*;
import static com.example.onedaypiece.web.domain.member.QMember.*;
import static com.example.onedaypiece.web.domain.point.QPoint.*;
import static com.example.onedaypiece.web.domain.pointHistory.QPointHistory.*;
import static com.example.onedaypiece.web.domain.posting.QPosting.*;

@RequiredArgsConstructor
@Repository
public class PointHistoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<MemberHistoryDto> findHistoryPosting(String email){
        return queryFactory.select(new QMemberHistoryDto(
                pointHistory.pointHistoryId,
                pointHistory.createdAt,
                pointHistory.posting.challenge.challengeTitle,
                pointHistory.getPoint,
                pointHistory.posting.member.memberId,
                pointHistory.posting.member.nickname,
                pointHistory.posting.member.profileImg,
                pointHistory.posting.member.point.acquiredPoint,
                pointHistory.posting.member.point
        )).from(pointHistory)
                .leftJoin(pointHistory.posting, posting)
                .leftJoin(pointHistory.posting.member, member)
                .leftJoin(pointHistory.posting.member.point, point)
                .leftJoin(pointHistory.posting.challenge, challenge)
                .where(pointHistory.posting.member.email.eq(email),
                        pointHistory.status.isTrue())
                .fetch();
    }

    public List<MemberHistoryDto> findHistoryChallenge(String email){
        return queryFactory.select(new QMemberHistoryDto(
                pointHistory.pointHistoryId,
                pointHistory.createdAt,
                pointHistory.posting.challenge.challengeTitle,
                pointHistory.getPoint,
                pointHistory.posting.member.memberId,
                pointHistory.posting.member.nickname,
                pointHistory.posting.member.profileImg,
                pointHistory.posting.member.point.acquiredPoint,
                pointHistory.posting.member.point
        )).from(pointHistory)
                .leftJoin(pointHistory.challengeRecord, challengeRecord)
                .leftJoin(pointHistory.challengeRecord.member,member)
                .leftJoin(pointHistory.challengeRecord.member.point,point)
                .leftJoin(pointHistory.challengeRecord.challenge)
                .where(pointHistory.challengeRecord.member.email.eq(email),
                        pointHistory.status.isTrue())
                .fetch();
    }

}
