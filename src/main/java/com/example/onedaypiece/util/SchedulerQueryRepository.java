package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord;
import com.example.onedaypiece.web.domain.posting.QPosting;
import com.example.onedaypiece.web.dto.query.posting.QSchedulerIdListDto;
import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.*;
import static com.example.onedaypiece.web.domain.posting.QPosting.*;
import static com.querydsl.jpa.JPAExpressions.*;

@Repository
@RequiredArgsConstructor
public class SchedulerQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<SchedulerIdListDto> findUncertifiedList(List<Long> challengeId, List<Long> memberId, LocalDateTime today){

        return queryFactory.select(new QSchedulerIdListDto(
                posting.challenge.challengeId,
                posting.member.memberId))
                .from(posting)
                .where(posting.postingStatus.isTrue(),
                        posting.challenge.challengeId.in(challengeId),
                        posting.member.memberId.in(memberId),
                        posting.createdAt.lt(today),
                        posting.postingCount.eq(1L))
                .fetch();
    }
    public List<SchedulerIdListDto> findNotWrittenList(List<Long> challengeId,List<Long> memberId, LocalDateTime today){

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


}
