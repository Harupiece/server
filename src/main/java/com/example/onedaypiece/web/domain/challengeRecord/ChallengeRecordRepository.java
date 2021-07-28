package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    List<ChallengeRecord> findAllByChallenge(Challenge challenge);
    List<ChallengeRecord> findAll();
    Page<ChallengeRecord> findAll(Pageable pageable);
    void deleteAllByChallenge(Challenge challenge);
    List<ChallengeRecord> findAllByMember(Member member);
    Boolean existsByChallengeAndMember(Challenge challenge, Member member);
}
