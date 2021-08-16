package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
     **/
    public List<ChallengeRecord> findAllByChallengeStatusTrueAndProgressNotStart() {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.challenge.challengeStatus.eq(true),
                        challengeRecord.challenge.challengeProgress.eq(1L))
                .orderBy(challengeRecord.challenge.challengeStartDate.asc())
                .fetch();
    }

    /**
     * @Query("select r from ChallengeRecord r " +
     * "inner join fetch r.member " +
     * "Where r.challengeRecordStatus = true and r.challenge = :challenge")
     * List<ChallengeRecord> findAllByChallenge(Challenge challenge);
     **/
    public List<ChallengeRecord> findAllByChallenge(Challenge challenge) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.challenge.eq(challenge))
                .fetch();
    }

    /**
     * @Query("select r from ChallengeRecord r " +
     * "inner join fetch r.member " +
     * "Where r.challengeRecordStatus = true and r.challenge = :challenge")
     * Optional<List<ChallengeRecord>> optionalFindAllByChallenge(Challenge challenge);
     **/
    public Optional<List<ChallengeRecord>> optionalFindAllByChallenge(Challenge challenge) {
        return Optional.ofNullable(queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.challenge.eq(challenge))
                .fetch());
    }

    /**
     * @Query("select c from ChallengeRecord c " +
     * "inner join fetch c.challenge " +
     * "inner join fetch c.member " +
     * "Where c.challengeRecordStatus = true " +
     * "and c.challenge.challengeId in :challengeIdList")
     * List<ChallengeRecord> findAllByChallengeIdList(List<Long> challengeIdList);
     **/
    public List<ChallengeRecord> findAllByChallengeIdList(List<Long> challengeIdList) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.eq(true),
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
     **/
    public List<ChallengeRecord> findAllByChallengeId(Long challengeId) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.eq(true),
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
     **/
    public List<ChallengeRecord> findPopular(String email, Pageable page) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .where(challengeRecord.challenge.challengeStatus.eq(true),
                        challengeRecord.challenge.challengeProgress.eq(1L),
                        challengeRecord.member.email.notIn(email))
                .groupBy(challengeRecord.challenge.challengeId)
                .orderBy(challengeRecord.challenge.challengeId.count().desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();
    }

    /**
    @Query("select c " +
            "from ChallengeRecord c " +
            "inner join fetch c.challenge " +
            "inner join fetch c.member " +
            "Where c.challengeRecordStatus = true and c.challenge.challengeProgress = 1 " +
            "order by c.modifiedAt desc")
    List<ChallengeRecord> findAllByStatusTrueOrderByModifiedAtDesc();
    **/
    public List<ChallengeRecord> findAllByStatusTrueOrderByModifiedAtDesc() {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .join(challengeRecord.challenge).fetchJoin()
                .join(challengeRecord.member).fetchJoin()
                .where(challengeRecord.challengeRecordStatus.eq(true),
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
     **/
    public Long countByChallenge(Challenge challenge) {
        return queryFactory
                .select(challengeRecord.challengeRecordId)
                .from(challengeRecord)
                .where(challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.challenge.eq(challenge))
                .fetchCount();
    }

    /**
    @Query("select c from ChallengeRecord c " +
            "Where c.challengeRecordStatus = true " +
            "and c.member = :member " +
            "and c.challenge.challengeProgress = :progress")
    List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress);
    **/
    public List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress) {
        return queryFactory
                .select(challengeRecord)
                .from(challengeRecord)
                .where(challengeRecord.challengeRecordStatus.eq(true),
                        challengeRecord.member.eq(member),
                        challengeRecord.challenge.challengeProgress.eq(progress))
                .fetch();
    }
}
