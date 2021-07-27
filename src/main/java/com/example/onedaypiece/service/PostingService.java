package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiException;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.PostingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    public Long createPosting(PostingRequestDto postingRequestDto) {
        Member member = getMember(postingRequestDto);
        Challenge challenge = getChallenge(postingRequestDto);

        Posting posting = Posting.createPosting(postingRequestDto,member,challenge);
        postingRepository.save(posting);

        return posting.getPostingId();
    }









    private Challenge getChallenge(PostingRequestDto postingRequestDto) {
        return challengeRepository.findById(postingRequestDto.getChallenge().getId()).orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }

    private Member getMember(PostingRequestDto postingRequestDto) {
        return memberRepository.findById(postingRequestDto.getMember().getId()).orElseThrow(() -> new ApiRequestException("등록된 멤버가 없습니다."));
    }
}
