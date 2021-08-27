package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {

    ChallengeRecord findByChallengeAndMemberAndChallengeRecordStatusTrue(Challenge challenge, Member member);

    List<ChallengeRecord> findAllByChallengeAndChallengeRecordStatusTrue(Challenge challenge);

    @Modifying(clearAutomatically = true)
    @Query("update ChallengeRecord c " +
            "set c.challengeRecordStatus = false " +
            "where c.member.memberId in :kickMember " +
            "and c.challenge.challengeId in :kickChallenge")
    int kickMemberOnChallenge(List<Long> kickMember, List<Long> kickChallenge);

    @Query("select CASE WHEN count(c.challengeRecordId) > 0 then true else false end " +
            "from ChallengeRecord c " +
            "Where c.challengeRecordStatus = true " +
            "and c.challenge.challengeId = :challengeId " +
            "and c.member = :member " +
            "and c.challenge.challengeProgress in (:progress, :expected) ")
    boolean existsByChallengeIdAndAndMember(Long challengeId, Member member, Long progress, Long expected);

    void deleteByMember(Member member);
}