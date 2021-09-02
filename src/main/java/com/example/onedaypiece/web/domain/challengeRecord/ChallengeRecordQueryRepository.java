package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeDetailResponseDtoMemberDto;
import com.example.onedaypiece.web.dto.response.challenge.QChallengeDetailResponseDtoMemberDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.QChallenge.challenge;
import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;

@Repository
@RequiredArgsConstructor
public class ChallengeRecordQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChallengeRecord> findAllByChallenge(List<Challenge> challengeList) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge,challenge)
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.in(challengeList))
                .fetch();
    }


    public List<ChallengeRecord> findAllByChallengeList(Slice<Challenge> challengeList) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.member).fetchJoin()
                .distinct()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.in(challengeList.getContent()))
                .fetch();
    }

    public List<ChallengeDetailResponseDtoMemberDto> findAllByChallengeId(Long challengeId) {
        return queryFactory
                .select(new QChallengeDetailResponseDtoMemberDto(
                        challengeRecord.member.memberId,
                        challengeRecord.member.nickname,
                        challengeRecord.member.profileImg
                ))
                .distinct()
                .from(challengeRecord)
                .join(challengeRecord.member)
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeId.eq(challengeId))
                .fetch();
    }

    public List<ChallengeRecord> findAllPopular(Pageable page) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(1L),
                        challengeRecord.challenge.categoryName.ne(CategoryName.OFFICIAL),
                        challengeRecord.challenge.challengePassword.eq(""))
                .groupBy(challengeRecord.challenge.challengeId)
                .orderBy(challengeRecord.challenge.challengeId.count().desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();
    }

    public List<ChallengeRecord> findAllByStatusTrue() {
        return queryFactory
                .selectFrom(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(1L),
                        challengeRecord.challenge.challengePassword.eq(""))
                .orderBy(challengeRecord.modifiedAt.desc())
                .fetch();
    }

    public Long countByChallenge(Challenge challenge) {
        return queryFactory
                .select(challengeRecord.challengeRecordId)
                .from(challengeRecord)
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.eq(challenge))
                .fetchCount();
    }

    public List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress) {
        return queryFactory
                .selectFrom(challengeRecord)
                .join(challengeRecord.challenge, challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.member.eq(member),
                        challengeRecord.challenge.challengeProgress.eq(progress))
                .fetch();
    }

    public List<ChallengeRecord> findAllByMemberAndStatus(Member member, Long challengeStatus) {
        return queryFactory
                .selectFrom(challengeRecord)
                .where(challengeRecord.member.eq(member),
                        challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(challengeStatus))
                .fetch();
    }

    public List<ChallengeRecord> findAllByMember(Member member) {
        return queryFactory
                .selectFrom(challengeRecord)
                .distinct()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.lt(3L),
                        challengeRecord.member.eq(member))
                .fetch();
    }
}
