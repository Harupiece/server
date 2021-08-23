package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.domain.challengeRecord.QChallengeRecord.challengeRecord;

@Repository
@RequiredArgsConstructor
public class ChallengeRecordQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * @Query("select c from ChallengeRecord c inner join fetch c.challenge " +
     * "where c.challengeRecordStatus = true " +
     * "and c.challenge.challengeStatus = true " +
     * "and c.challenge.challengeProgress = 1 " +
     * "order by c.challenge.challengeStartDate asc")
     * List<ChallengeRecord> findAllByChallengeStatusTrueAndProgressNotStart();
     */
    public List<ChallengeRecord> findAllByChallengeStatusTrueAndProgressNotStart() {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .distinct()
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(1L))
                .orderBy(challengeRecord.challenge.challengeStartDate.asc())
                .fetch();
    }

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
                        challengeRecord.challenge.eq(challenge))
                .fetch();
    }

    /**
     * @Query("select c from ChallengeRecord c " +
     * "inner join fetch c.challenge " +
     * "inner join fetch c.member " +
     * "Where c.challengeRecordStatus = true " +
     * "and c.challenge.challengeId in :challengeIdList")
     * List<ChallengeRecord> findAllByChallengeIdList(List<Long> challengeIdList);
     */
    public List<ChallengeRecord> findAllByChallengeIdList(List<Long> challengeIdList) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeId.in(challengeIdList))
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
    public List<ChallengeRecord> findAllByChallengeId(Long challengeId) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.challenge).fetchJoin()
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
    public List<ChallengeRecord> findAllPopular(String email, Pageable page) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challenge.challengeStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(1L),
                        challengeRecord.member.email.ne(email),
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
                        challengeRecord.member.eq(member),
                        challengeRecord.challenge.challengeProgress.eq(progress))
                .fetch();
    }
// 여기

    /**
     * @Query("select c " +
     * "from ChallengeRecord c " +
     * "Where c.challengeRecordStatus = true " +
     * "and c.member = :member " +
     * "and c.challenge.challengeProgress in (:progress, :expected) ")
     * List<ChallengeRecord> findAllByMemberAndProgressAndExpected(@Param("member") Member member, @Param("progress") Long progress, @Param("expected") Long expected);
     */
//    public List<ChallengeRecord> findAllByMemberAndProgressAndExpected(Member member, Long progress, Long expected) {
//        return queryFactory
//                .selectFrom(challengeRecord)
//                .where(challengeRecord.member.eq(member),
//                        challengeRecord.challenge.challengeProgress.in(progress, expected))
//                .fetch();
//    }
    public List<ChallengeRecord> findAllByMemberAndStatus(Member member, Long challengeStatus) {
        return queryFactory
                .selectFrom(challengeRecord)
                .where(challengeRecord.member.eq(member),
                        challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.challenge.challengeProgress.eq(challengeStatus))
                .fetch();
    }


// 여기

    /**
     * @Query("select c from ChallengeRecord c " +
     * "inner join c.challenge " +
     * "where c.challengeRecordStatus= true " +
     * "and c.challenge.challengeProgress = 2 ")
     * List<ChallengeRecord> findAllByChallenge();
     */
    public List<ChallengeRecord> findAllByChallenge() {
        return queryFactory
                .selectFrom(challengeRecord)
                .innerJoin(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.isTrue(),
                        challengeRecord.challenge.challengeProgress.eq(2L))
                .fetch();
    }

    public List<ChallengeRecord> findAllByMember(Member member) {
        return queryFactory
                .selectFrom(challengeRecord)
                .where(challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.challenge.challengeStatus.eq(true),
                        challengeRecord.challenge.challengeProgress.lt(3L),
                        challengeRecord.member.eq(member))
                .fetch();
    }

//    /**
//     * @Query("select distinct c from ChallengeRecord c " +
//     * "left join fetch Posting p on c.challenge.challengeId = p.challenge.challengeId " +
//     * "where c.challenge.challengeId in :challengeId " +
//     * "and p.postingId  in ( select p.postingId" +
//     * "                    from Posting p " +
//     * "                    where p.challenge.challengeId in :challengeId " +
//     * "                      and p.createdAt < :today " +
//     * "                      and p.member.memberId not in (c.member.memberId))")
//     * List<ChallengeRecord> findPostingListTest2(List<Long> challengeId, LocalDateTime today);
//     */
//    public List<ChallengeRecord> findPostingListTest2(List<Long> challengeId, LocalDateTime today) {
//        return queryFactory
//                .select(challengeRecord).distinct()
//                .from(challengeRecord)
//                .leftJoin(posting).on(challengeRecord.challenge.challengeId.eq(posting.challenge.challengeId)).fetchJoin()
//                .where(challengeRecord.challenge.challengeId.in(challengeId),
//                        posting.postingId.in(JPAExpressions
//                                .select(posting.postingId)
//                                .from(posting)
//                                .where(
//                                        posting.challenge.challengeId.in(challengeId),
//                                        posting.createdAt.before(today),
//                                        posting.member.memberId.ne(challengeRecord.member.memberId)))) // notIn을 ne로 했는데 동작 확인 바람
//                .fetch();
//    }
}
