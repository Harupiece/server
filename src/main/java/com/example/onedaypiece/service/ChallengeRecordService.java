package com.example.onedaypiece.service;

import com.example.onedaypiece.chat.model.ChatMember;
import com.example.onedaypiece.chat.repository.ChatMemberRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeQueryRepository;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.dto.request.challengeRecord.ChallengeRecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.onedaypiece.web.domain.challenge.CategoryName.OFFICIAL;

@Service
@RequiredArgsConstructor
public class ChallengeRecordService {

    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    @Transactional
    public void requestChallenge(ChallengeRecordRequestDto requestDto, String email) {
        Challenge challenge = ChallengeChecker(requestDto.getChallengeId());
        Member member = MemberChecker(email);

        requestChallengeException(challenge, member);

        ChallengeRecord record = ChallengeRecord.createChallengeRecord(challenge, member);
        challengeRecordRepository.save(record);

        ChatMember chatMember = ChatMember.createChatMember(member.getMemberId(), String.valueOf(challenge.getChallengeId()));
        chatMemberRepository.save(chatMember);
    }

    @Transactional
    public void giveUpChallenge(Long challengeId, String email) {
        Challenge challenge = ChallengeChecker(challengeId);
        Member member = MemberChecker(email);

        ChallengeRecord record = challengeRecordRepository.
                findByChallengeAndMemberAndChallengeRecordStatusTrue(challenge, member);
        record.setStatusFalse();
    }

    private Challenge ChallengeChecker(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않은 챌린지입니다."));
    }

    private Member MemberChecker(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 유저입니다."));
    }

    private void requestChallengeException(Challenge challenge, Member member) {
        List<ChallengeRecord> recordList = challengeRecordQueryRepository.findAllByMember(member);
        if (recordList.size() >= 10) {
            throw new ApiRequestException("이미 10개의 챌린지에 참가하고 있는 유저입니다.");
        }
        if (!challenge.getCategoryName().equals(OFFICIAL) &&
                challengeRecordQueryRepository.countByChallenge(challenge) >= 10) {
            throw new ApiRequestException("챌린지는 10명까지만 참여 가능합니다.");
        }
        if (recordList.stream().anyMatch(r -> r.getChallenge().equals(challenge))) {
            throw new ApiRequestException("이미 해당 챌린지에 참가하고 있는 유저입니다.");
        }
    }
}
