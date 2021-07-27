package com.example.onedaypiece.web.domain.challengeRecord;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    List<ChallengeRecord> findAllByChallenge(Challenge challenge);
}
