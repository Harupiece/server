package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true and c.challenge = :challenge")
    List<ChallengeRecord> findAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true")
    List<ChallengeRecord> findAll();

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true")
    Page<ChallengeRecord> findAll(Pageable pageable);

    void deleteAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true and c.member = :member")
    List<ChallengeRecord> findAllByMember(Member member);

    @Query("select CASE WHEN count(c)>0 then true else false end " +
            "from ChallengeRecord c " +
            "Where c.challengeRecordStatus = true and c.member = :member and c.challenge = :challenge")
    boolean existsByChallengeAndMember(Challenge challenge, Member member);

    @Query("select count(c) from ChallengeRecord c Where c.challengeRecordStatus = true and c.challenge = :challenge")
    int countByChallenge(Challenge challenge);
}
