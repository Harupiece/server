package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.dto.request.challengeRecord.ChallengeRecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChallengeRecordService {

    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Map<String, String> requestChallenge(ChallengeRecordRequestDto requestDto, UserDetails userDetails) {
        Map<String, String> requestChallengeResultMap = new HashMap<>();

        Challenge challenge = challengeRepository.findById(requestDto.getChallengeId())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 챌린지입니다."));

        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));

        if (challengeRecordRepository.existsByChallengeAndMember(challenge, member)) {
            throw new IllegalArgumentException("이미 해당 챌린지에 신청한 유저입니다.");
        }
        if (challengeRecordRepository.countByChallenge(challenge) > 10) {
            throw new IllegalArgumentException("챌린지는 10명까지만 참여 가능합니다.");
        }

        if (challenge.getChallengePassword().equals(requestDto.getChallengePassword())) {
            challengeRecordRepository.save(new ChallengeRecord(challenge, member));
            requestChallengeResultMap.put("ok", "true");
            requestChallengeResultMap.put("message", "성공");
        } else {
            requestChallengeResultMap.put("ok", "false");
            requestChallengeResultMap.put("message", "실패");
        }
        requestChallengeResultMap.put("challengeId", String.valueOf(requestDto.getChallengeId()));

        return requestChallengeResultMap;
    }

    @Transactional
    public void giveUpChallenge(Long challengeId) {
        challengeRecordRepository.deleteById(challengeId);
    }
}
