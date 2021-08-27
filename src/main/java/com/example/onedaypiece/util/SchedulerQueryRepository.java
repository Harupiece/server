package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.QMember;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.posting.QPosting;
import com.example.onedaypiece.web.dto.query.posting.QSchedulerIdListDto;
import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.challenge;
import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;
import static com.example.onedaypiece.web.domain.member.QMember.member;
import static com.example.onedaypiece.web.domain.point.QPoint.point;
import static com.example.onedaypiece.web.domain.posting.QPosting.posting;
import static com.querydsl.jpa.JPAExpressions.*;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerQueryRepository {

    @Autowired
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    /**
     *진행중인 챌린지
     *
     */
    public List<ChallengeRecord> findAllByChallenge(int week) {
        return queryFactory
                .selectFrom(challengeRecord)
                .innerJoin(challengeRecord.challenge,challenge)
                .where(getEmpty(week),
                        challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(2L))
                .fetch();
    }

    private BooleanExpression getEmpty(int week) {
        BooleanExpression notEmpty = challengeRecord.challenge.challengeHoliday.isNotEmpty();
        BooleanExpression empty = challengeRecord.challenge.challengeHoliday.eq("");
        return week == 6 || week ==7 ? notEmpty:empty ;
    }

    /**
     *글 쓰지 않은 자
     */
    public List<SchedulerIdListDto> findNotWrittenList(List<Long> challengeId){

        return queryFactory.select(new QSchedulerIdListDto(
                challengeRecord.challenge.challengeId,
                challengeRecord.member.memberId)).distinct()
                .from(challengeRecord)
                .leftJoin(posting)
                .on(challengeRecord.challenge.challengeId.eq(posting.challenge.challengeId),
                        (challengeRecord.member.memberId.eq(posting.member.memberId)))
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeId.in(challengeId),
                        posting.isNull())
                .fetch();
    }

    public List<RemainingMember> findChallengeMember(LocalDateTime today) {
        QChallengeRecord challengeRecordSub = new QChallengeRecord("challengeRecordSub");

        return queryFactory.select(new QRemainingMember(
                posting,
                challengeRecord.challengeRecordId,
                challengeRecord.challengeRecordStatus))
                .from(challengeRecord)
                .leftJoin(challengeRecord.challenge,challenge)
                .leftJoin(challengeRecord.member, member)
                .join(posting).on(posting.challenge.challengeId.eq(challenge.challengeId),
                        posting.member.eq(member))
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        posting.postingCount.goe(
                                JPAExpressions.select(challengeRecordSub.challengeRecordId.count().divide(2))
                                        .from(challengeRecordSub)
                                        .where(challengeRecordSub.challenge.challengeId.eq(challengeRecord.challenge.challengeId))
                        ))
                .fetch();
    }

    /**
     * 수정 가능 여부 (당일만 가능)
     */
    public List<Long> findSchedulerUpdatePosting(LocalDateTime today) {
        return queryFactory
                .select(posting.postingId)
                .from(posting)
                .where(posting.postingStatus.isTrue(),
                        posting.postingModifyOk.isTrue(),
                        posting.createdAt.lt(today))
                .fetch();
    }

    /**
     * 공식 챌린지 존재 여부
     */
    public Boolean findAllByOfficialAndChallengeTitle(String title) {
        return queryFactory
                .select(challenge.challengeId)
                .from(challenge)
                .where(challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1L),
                        challenge.challengeTitle.eq(title))
                .fetchFirst() == null;
    }

    /**
     * 챌린지 진행 상태 업데이트
     */
    @Modifying
    public Long updateChallengeProgress(Long progress, List<Challenge> challengeList) {
        return queryFactory
                .update(challenge)
                .set(challenge.challengeProgress, progress)
                .where(challenge.in(challengeList))
                .execute();
    }

    /**
     * 포인트 업데이트
     */
    @Modifying
    public Long updateChallengePoint(List<Challenge> challengeList) {
        return queryFactory
                .update(challengeRecord)
                .set(challengeRecord.challengePoint, true)
                .where(challengeRecord.challenge.in(challengeList))
                .execute();
    }

    /**
     * 챌린지 완료 포인트 지급
     */
    public List<ChallengeRecord> findAllByChallengeOnScheduler(Challenge challenge) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.eq(challenge))
                .fetch();
    }

    /**
     * 포스팅 갯수 구하기
     */
    public Long findAllByChallengeAndFirstMember(Challenge challenge, Member member) {
        return queryFactory
                .select(posting.challenge.challengeId)
                .from(posting)
                .where(posting.challenge.challengeStatus.isTrue(),
                        posting.challenge.eq(challenge),
                        posting.member.eq(member))
                .fetchCount();

    }

    /**
     * 챌린지 완수 포인트 벌크 업데이트
     */
    @Modifying
    public void updatePointAll(List<Point> pointList, Long getPoint) {
        queryFactory
                .update(point)
                .set(point.acquiredPoint, point.acquiredPoint.add(getPoint))
                .where(point.in(pointList))
                .execute();
    }

    /**
     * 진행 상태 업데이트할 챌린지 목록 찾기
     */
    public List<ChallengeRecord> findAllByChallengeProgressLessThan(Long progress) {
        return queryFactory
                .selectFrom(challengeRecord)
                .innerJoin(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.lt(progress))
                .fetch();
    }




//    select challenge_record_id,count(c.challenge_id)
//    from challenge_record cr
//    left outer join challenge c on cr.challenge_id = c.challenge_id
//    where challenge_record_status = true
//    and c.challenge_status = true
//    and c.challenge_progress = 2
//    group by c.challenge_id

//    ;
}

