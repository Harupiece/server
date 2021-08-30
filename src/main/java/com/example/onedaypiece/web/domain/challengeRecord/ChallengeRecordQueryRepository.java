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

import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;

@Repository
@RequiredArgsConstructor
public class ChallengeRecordQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * @Query("select r from ChallengeRecord r " +
     * "inner join fetch r.member " +
     * "Where r.challengeRecordStatus = true and r.challenge = :challenge")
     * List<ChallengeRecord> findAllByChallenge(Challenge challenge);
     */
    public List<ChallengeRecord> findAllByChallenge(Challenge challenge) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.eq(challenge))
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

    /**
     * @Query("select c from ChallengeRecord c " +
     * "inner join fetch c.challenge " +
     * "inner join fetch c.member " +
     * "Where c.challengeRecordStatus = true " +
     * "and c.challenge.challengeId = :challengeId")
     * List<ChallengeRecord> findAllByChallengeId(Long challengeId);
     */
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

    /**
     * @Query("select c " +
     * "from ChallengeRecord c inner join fetch c.challenge " +
     * "where c.challenge.challengeStatus = true and c.challenge.challengeProgress = 1 " +
     * "and c.member.email not in :email group by c.challenge.challengeId " +
     * "order by count(c.challenge.challengeId) desc")
     * List<ChallengeRecord> findPopularOrderByDesc(String email, Pageable pageable);
     */
    public List<ChallengeRecord> findAllPopular(Pageable page) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(1L),
                        challengeRecord.challenge.categoryName.ne(CategoryName.OFFICIAL))
                .groupBy(challengeRecord.challenge.challengeId)
                .orderBy(challengeRecord.challenge.challengeId.count().desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();
    }

    /**
     * @Query("select c " +
     * "from ChallengeRecord c " +
     * "inner join fetch c.challenge " +
     * "inner join fetch c.member " +
     * "Where c.challengeRecordStatus = true and c.challenge.challengeProgress = 1 " +
     * "order by c.modifiedAt desc")
     * List<ChallengeRecord> findAllByStatusTrueOrderByModifiedAtDesc();
     */
    public List<ChallengeRecord> findAllByStatusTrue() {
        return queryFactory
                .selectFrom(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(1L))
                .orderBy(challengeRecord.modifiedAt.desc())
                .fetch();
    }

    /**
     * @Query("select count(c.challengeRecordId) " +
     * "from ChallengeRecord c " +
     * "Where c.challengeRecordStatus = true " +
     * "and c.challenge = :challenge")
     * int countByChallenge(Challenge challenge);
     */
    public Long countByChallenge(Challenge challenge) {
        return queryFactory
                .select(challengeRecord.challengeRecordId)
                .from(challengeRecord)
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.eq(challenge))
                .fetchCount();
    }

    /**
     * @Query("select c from ChallengeRecord c " +
     * "Where c.challengeRecordStatus = true " +
     * "and c.member = :member " +
     * "and c.challenge.challengeProgress = :progress")
     * List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress);
     */
    // 1번과 2번인 챌린지 레코드
    public List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress) {
        return queryFactory
                .selectFrom(challengeRecord)
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
