package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;

    @Transactional
    public void deleteChallengeByAdmin(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다."));
        challengeRepository.deleteById(challengeId);
        challengeRecordRepository.deleteAllByChallenge(challenge);
    }
}
