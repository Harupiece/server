package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.certification.CertificationRepository;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingQueryRepository;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingUpdateRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;

    private final PostingQueryRepository postingQueryRepository;

    /**
     * 1.포스트 저장
     *
     */
    public Long createPosting(PostingCreateRequestDto postingCreateRequestDto, String email) {
        Member member = getMemberByEmail(email);
        Challenge challenge = getChallenge(postingCreateRequestDto.getChallengeId());
        Posting posting = Posting.createPosting(postingCreateRequestDto,member,challenge);


        // 포스팅 검사
        validatePosting(challenge);

        postingRepository.save(posting);

        return posting.getPostingId();
    }

    /**
     * 2.포스트 리스트
     *
     */
    @Transactional(readOnly = true)
    public List<PostingResponseDto> getPosting(int page, Long challengeId) {

        Pageable pageable = PageRequest.of(page-1,6);

//        List<Posting> postingList =postingRepository.findPostingList(challengeId,pageable);

        // QueryRepository 적용
        List<Posting> postingList =postingQueryRepository.findPostingList(challengeId,pageable);

        List<Certification> certifications = certificationRepository.findAllByPosting(challengeId);

        return postingList
                .stream()
                .map(posting -> new PostingResponseDto(posting, certifications))
                .collect(Collectors.toList());
    }

    /**
     * 3.포스트 업데이트
     *
     */
    public Long updatePosting(Long postingId, String email, PostingUpdateRequestDto postingUpdateRequestDto) {

        Member member = getMemberByEmail(email);
        Posting posting = getPosting(postingId);

        // 작성자 검사
        validateMember(member,posting.getMember().getMemberId());

        // 포스팅 검사
        validatePosting(posting.getChallenge());

        posting.updatePosting(postingUpdateRequestDto);
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

        // 인증 검사.
        isApprovalIsTrue(posting);

        posting.deletePosting();
        return posting.getPostingId();


    }

    private void isApprovalIsTrue(Posting posting) {
        if(posting.isPostingApproval()){
             throw new ApiRequestException("이미 인증된 게시글은 삭제할 수 없습니다.");
        }
    }

    private Posting getPosting(Long postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }

    private Challenge getChallenge(Long challengeId) {
        log.info("getChallenge : {} ",challengeId);
        return challengeRepository.findChallengeStatusTrue(challengeId)
                .orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("등록된 멤버가 없습니다."));
    }

    private void validateMember(Member member, Long memberId) {
        if (!memberId.equals(member.getMemberId())) {
            throw new ApiRequestException("해당 게시물에 대한 수정 권한이 없습니다.");
        }
    }

    private void validatePosting(Challenge challenge){

        LocalDateTime now = LocalDateTime.now();

        if(challenge.getChallengeStartDate().isAfter(now)){
            throw new ApiRequestException("챌린지 시작 후에 게시글 등록 가능합니다.");
        }
    }

}
