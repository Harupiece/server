package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.dto.request.challengeRecord.ChallengeRecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeRecordService {

    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void requestChallenge(ChallengeRecordRequestDto requestDto, String email) {
        requestChallengeException(requestDto, email);
    }

    @Transactional
    public void giveUpChallenge(Long challengeId) {
        challengeRecordRepository.deleteById(challengeId);
    }

    private void requestChallengeException(ChallengeRecordRequestDto requestDto, String email) {
        Challenge challenge = challengeRepository.findById(requestDto.getChallengeId())
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다."));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 유저입니다."));
        if (challengeRecordRepository.existsByChallengeAndMember(challenge, member)) {
            throw new ApiRequestException("이미 해당 챌린지에 신청한 유저입니다.");
        }
        if (challengeRecordRepository.countByChallenge(challenge) >= 10) {
            throw new ApiRequestException("챌린지는 10명까지만 참여 가능합니다.");
        }
    }
}
