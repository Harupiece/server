package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {

    @Query("select c from ChallengeRecord c inner join fetch c.challenge " +
            "where c.challengeRecordStatus = true " +
            "and c.challenge.challengeStatus = true " +
            "and c.challenge.challengeProgress < 3 " +
            "order by c.modifiedAt desc")
    List<ChallengeRecord> findAllByChallengeStatusTrue();

    @Query("select c from ChallengeRecord c inner join fetch c.challenge " +
            "where c.challengeRecordStatus = true " +
            "and c.challenge.challengeStatus = true " +
            "and c.challenge.challengeProgress = 1 " +
            "order by c.challenge.challengeStartDate asc")
    List<ChallengeRecord> findAllByChallengeStatusTrueAndProgressNotStart();

    @Query("select c from ChallengeRecord c inner join fetch c.challenge " +
            "where c.challengeRecordStatus = true " +
            "and c.challenge.challengeStatus = true " +
            "and c.challenge.challengeProgress < 3 " +
            "order by c.modifiedAt desc")
    List<ChallengeRecord> findAllByChallengeStatusTrueByPaged(Pageable pageable);

    void deleteByChallengeAndMember(Challenge challenge, Member member);

    @Query("select r from ChallengeRecord r " +
            "inner join fetch r.member " +
            "Where r.challengeRecordStatus = true and r.challenge = :challenge")
    List<ChallengeRecord> findAllByChallenge(Challenge challenge);

    @Query("select r from ChallengeRecord r " +
            "inner join fetch r.member " +
            "Where r.challengeRecordStatus = true and r.challenge = :challenge")
    Optional<List<ChallengeRecord>> optionalFindAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c " +
            "inner join fetch c.challenge " +
            "inner join fetch c.member " +
            "Where c.challengeRecordStatus = true " +
            "and c.challenge.challengeId = :challengeId")
    List<ChallengeRecord> findAllByChallengeId(Long challengeId);

    @Query("select c " +
            "from ChallengeRecord c inner join fetch c.challenge " +
            "where c.challenge.challengeStatus = true and c.challenge.challengeProgress = 1 " +
            "and c.member.email not in :email group by c.challenge.challengeId " +
            "order by count(c.challenge.challengeId) desc")
    List<ChallengeRecord> findPopularOrderByDesc(String email, Pageable pageable);

    void deleteAllByChallenge(Challenge challenge);

    @Query("select c " +
            "from ChallengeRecord c " +
            "inner join fetch c.challenge " +
            "inner join fetch c.member " +
            "Where c.challengeRecordStatus = true and c.challenge.challengeProgress = 1 " +
            "order by c.modifiedAt desc")
    List<ChallengeRecord> findAllByStatusTrueOrderByModifiedAtDesc();

    @Query("select count(c.challengeRecordId) from ChallengeRecord c Where c.challengeRecordStatus = true and c.challenge = :challenge")
    int countByChallenge(Challenge challenge);

    // 본인이 참여한 챌린지중 진행중인첼린지
    @Query("select c from ChallengeRecord c " +
            "Where c.challengeRecordStatus = true " +
            "and c.member = :member " +
            "and c.challenge.challengeProgress = :progress")
    List<ChallengeRecord> findAllByMemberAndProgress(Member member, Long progress);

    // 진행중과 진행예정 챌린지 in을 사용하기
    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true and c.member = :member and c.challenge.challengeProgress in (:progress, :expected) ")
    List<ChallengeRecord> findAllByMemberAndProgressAndExpected(@Param("member") Member member,@Param("progress") Long progress, @Param("expected") Long expected);

    // 채팅방 입장할 때 사용
    @Query("select CASE WHEN count(c)>0 then true else false end from ChallengeRecord c Where c.challengeRecordStatus = true and c.challenge.challengeId = :challengeId and c.member = :member and c.challenge.challengeProgress in (:progress, :expected) ")
    boolean existsByChallengeIdAndAndMember (Long challengeId, Member member, Long progress, Long expected);

    @Query("select c from ChallengeRecord c " +
            "inner join c.challenge " +
            "where c.challengeRecordStatus= true " +
            "and c.challenge.challengeProgress = 2 ")
    List<ChallengeRecord> findAllByChallenge();

    @Query("select distinct c from ChallengeRecord c " +
            "left join fetch Posting p on c.challenge.challengeId = p.challenge.challengeId " +
            "where c.challenge.challengeId in :challengeId " +
            "and p.postingId  in ( select p.postingId" +
            "                    from Posting p " +
            "                    where p.challenge.challengeId in :challengeId " +
            "                      and p.createdAt < :today " +
            "                      and p.member.memberId not in (c.member.memberId))")
    List<ChallengeRecord> findPostingListTest2(List<Long> challengeId, LocalDateTime today);

    @Modifying(clearAutomatically = true)
    @Query("update ChallengeRecord c " +
            "set c.challengeRecordStatus = false " +
            "where c.member.memberId in :kickMember " +
            "and c.challenge.challengeId in :kickChallenge")
    int kickMemberOnChallenge(List<Long> kickMember, List<Long> kickChallenge);

    @Modifying(clearAutomatically = true)
    @Query("update ChallengeRecord c " +
            "set c.challengePoint = true " +
            "where c.challenge in :challengeList")
    void updateChallengePoint(List<Challenge> challengeList);

    @Query("select r " +
            "from ChallengeRecord r " +
            "where r.challenge = :challenge " +
            "and r.member = :member")
    ChallengeRecord findByChallengeAndMember(Challenge challenge, Member member);

    @Query("select r " +
            "from ChallengeRecord r " +
            "where r.member = :member")
    List<ChallengeRecord> findAllByMember(Member member);
}