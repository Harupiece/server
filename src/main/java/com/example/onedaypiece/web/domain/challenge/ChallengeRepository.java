package com.example.onedaypiece.web.domain.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByChallengeStatusTrueAndChallengeId(Long challengeId);
}