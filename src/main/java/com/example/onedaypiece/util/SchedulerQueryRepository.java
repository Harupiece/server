package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.dto.query.posting.QSchedulerIdListDto;
import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.challenge;
import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;
import static com.example.onedaypiece.web.domain.point.QPoint.point;
import static com.example.onedaypiece.web.domain.posting.QPosting.posting;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerQueryRepository {


    private final JPAQueryFactory queryFactory;

    /**
     *진행중인 챌린지
     */
    public List<Long> findAllByChallenge(int week) {
        return queryFactory
                .select(challengeRecord.challenge.challengeId)
                .distinct()
                .from(challengeRecord)
                .join(challengeRecord.challenge,challenge)
                .where(isHoliday(week),
                        challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(2L))
                .fetch();
    }

    // 주말인지 아닌지 여부를 체크하기 위해서

    private BooleanExpression isHoliday(int week) {
        // 0,6 자바스크립트 date의 주말
        BooleanExpression notEmpty = challengeRecord.challenge.challengeHoliday.isNotEmpty();
        // null 값이 들어올 일이 없기 때문에 3항연산자만  사용.
       return week == 6 || week ==7 ? notEmpty:null ;
    }

    /**
     *글 쓰지 않은 자
     * left outer join으로 포스팅이 null인것까지 가져올 수 있게 함.
     * on절에 and로 challengeRecord의 member와 posting의 member를 조인.
     * posting.isNull()을 통해 posting이 null인것만 가져옴.
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
    /**
     * 포스팅을 등록한 인원만 가져오기 위해 포스팅에서 가져옴.
     * posting 의 challenge과 challengeRecord 의 challenge , posting의 member와 challengeRecord의 member를 조인(포스팅 작성자)
     * 서브쿼리를 이용해서 postingCount가  challengeRecord의 멤버 count의 50%이상인 포스팅만 가져옴.
     */
    public List<Posting> findChallengeMember() {
        QChallengeRecord challengeRecordSub = new QChallengeRecord("challengeRecordSub");

        return queryFactory.select(posting)
                .from(posting)
                .join(challengeRecord).on(challengeRecord.challenge.eq(posting.challenge),
                        posting.member.eq(challengeRecord.member))
                .join(posting.challenge,challenge)
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        posting.postingApproval.isFalse(),
                        posting.postingCount.goe(
                                JPAExpressions.select(challengeRecordSub.challengeRecordId.count().divide(2))
                                        .from(challengeRecordSub)
                                        .where(challengeRecordSub.challenge.challengeId.eq(challengeRecord.challenge.challengeId),
                                                challengeRecordSub.challengeRecordStatus.isTrue())
                        ))
                .fetch();
    }

    /**
     * 수정 가능 여부 (당일만 가능)
     */
    public List<Long> findSchedulerUpdatePostingModifyOk(LocalDateTime today) {
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

}