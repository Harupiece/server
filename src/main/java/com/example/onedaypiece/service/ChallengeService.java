package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final MemberRepository memberRepository;

    public ChallengeResponseDto getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("존재하지 않은 챌린지id입니다"));

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeProgress(1L);
        } else if (currentLocalDateTime.isBefore(challenge.getChallengeEndDate())) {
            challenge.setChallengeProgress(2L);
        } else {
            challenge.setChallengeProgress(3L);
        }

        List<Long> challengeMember = new ArrayList<>();
        challengeRecordRepository.findAllByChallenge(challenge).forEach(
                value -> challengeMember.add(value.getMember().getMemberId()));

        return new ChallengeResponseDto(challenge, challengeMember);
    }

    @Transactional
    public Map<String, String> deleteChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("존재하지 않은 챌린지id입니다"));

        Map<String, String> deleteResultMap = new HashMap<>();

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeStatus(false);
            challenge.setChallengeProgress(3L);

            deleteResultMap.put("msg", "챌린지가 비활성화되었습니다.");
        } else {
            deleteResultMap.put("msg", "이미 시작된 챌린지는 삭제할 수 없습니다.");
        }

        return deleteResultMap;
    }

    public void createChallenge(ChallengeRequestDto requestDto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));
        Challenge challenge = new Challenge(requestDto, member);
        challengeRepository.save(challenge);
    }
}
