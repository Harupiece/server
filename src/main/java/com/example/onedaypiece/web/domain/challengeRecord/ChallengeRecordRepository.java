package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    @Query("select c from ChallengeRecord c Where c.challenge = :challenge and c.challengeRecordStatus = true")
    List<ChallengeRecord> findAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true")
    List<ChallengeRecord> findAll();

    @Query("select c from ChallengeRecord c Where c.challengeRecordStatus = true")
    Page<ChallengeRecord> findAll(Pageable pageable);

    void deleteAllByChallenge(Challenge challenge);

    @Query("select c from ChallengeRecord c Where c.member = :member and c.challengeRecordStatus = true")
    List<ChallengeRecord> findAllByMember(Member member);

//    @Query("select c from ChallengeRecord c Where c.challenge = :challenge and c.member = :member and c.challengeRecordStatus = true")
    Boolean existsByChallengeAndMember(Challenge challenge, Member member);

//    @Query("select c from ChallengeRecord c Where c.challenge = :challenge and c.challengeRecordStatus = true")
    Long countByChallenge(Challenge challenge);
}
