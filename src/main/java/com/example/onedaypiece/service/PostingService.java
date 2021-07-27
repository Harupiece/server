package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.posting.PostingRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    /**
     * 1.포스트 저장
     *
     */
    public Long createPosting(PostingRequestDto postingRequestDto) {
        Member member = getMemberById(postingRequestDto.getMember().getMemberId());
        Challenge challenge = getChallenge(postingRequestDto.getChallenge().getChallengeId());
        Posting posting = Posting.createPosting(postingRequestDto,member,challenge);
        postingRepository.save(posting);

        return posting.getPostingId();
    }

    /**
     * 2.포스트 리스트
     *
     */
    public List<PostingResponseDto> getPosting(int page, Long challengeId) {
        Challenge challenge = getChallenge(challengeId);
        Pageable pageable = PageRequest.of(page,6);
        List<Posting> postingList = postingRepository.findAllByChallengeAndPostingStatusTrueOrderByCreatedAtDesc(challenge,pageable);

        return postingList
                .stream()
                .map(PostingResponseDto::new)
                .collect(toList());
    }

    /**
     * 3.포스트 업데이트
     *
     */
    public Long updatePosting(Long postingId,String email,PostingRequestDto postingRequestDto) {
        Member member = getMemberByEmail(email);
        Posting posting = getPosting(postingId);

        // 작성자 검사
        validateMember(member,posting.getMember().getMemberId());

        posting.updatePosting(postingRequestDto);
        return posting.getPostingId();

    }
    /**
     * 4.포스트 삭제
     *
     */
    public Long deletePosting(Long postingId, String email) {
        Member member = getMemberByEmail(email);
        Posting posting =getPosting(postingId);

        // 작성자 검사
        validateMember(member,posting.getMember().getMemberId());

        posting.deletePosting();
        return posting.getPostingId();


    }




    private Posting getPosting(Long postingId) {
        return postingRepository.findById(postingId).orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }
    private Challenge getChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId).orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new ApiRequestException("등록된 멤버가 없습니다."));
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new ApiRequestException("등록된 멤버가 없습니다."));
    }

    private void validateMember(Member member, Long memberId) {
        if (!memberId.equals(member.getMemberId())) {
            throw new ApiRequestException("해당 게시물에 대한 수정 권한이 없습니다.");
        }
    }

}
