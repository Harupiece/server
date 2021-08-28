package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.certification.CertificationQueryRepository;
import com.example.onedaypiece.web.domain.certification.CertificationRepository;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingQueryRepository;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.query.certification.CertificationQueryDto;
import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingUpdateRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingListDto;
import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final CertificationQueryRepository certificationQueryRepository;
    private  final PointHistoryRepository pointHistoryRepository;

    /**
     * 1.포스트 저장
     *
     */
    public Long createPosting(PostingCreateRequestDto postingCreateRequestDto, String email) {
        Member member = getMemberByEmail(email);
        Challenge challenge = getChallenge(postingCreateRequestDto.getChallengeId());
        Posting posting = Posting.createPosting(postingCreateRequestDto,member,challenge);

        // 포스팅 검사
        duplicatePosting(member,challenge);

        Certification certification = Certification.createCertification(member, posting);

        postingRepository.save(posting);
        certificationRepository.save(certification);

        Long memberCount = postingCreateRequestDto.getTotalNumber(); // <- 참여인원 있음

        checkMemberCountAndAddPoint(posting, memberCount);

        return posting.getPostingId();
    }


    /**
     * 2.포스트 리스트
     */
    @Transactional(readOnly = true)
    public PostingListDto getPosting(int page, Long challengeId) {

        Pageable pageable = PageRequest.of(page-1,6);

        Slice<PostingListQueryDto> postingList =postingQueryRepository.findPostingList(challengeId,pageable);

        List<CertificationQueryDto> certificationList = certificationQueryRepository.findAllByPosting(challengeId);

        List<PostingResponseDto> postingResponseDtoList = postingList
                .stream()
                .map(posting -> PostingResponseDto.of(posting, certificationList))
                .collect(Collectors.toList());

        return PostingListDto.createPostingListDto(postingResponseDtoList,postingList.hasNext());
    }

    /**
     * 3.포스트 업데이트
     */
    public Long updatePosting(Long postingId, String email, PostingUpdateRequestDto postingUpdateRequestDto) {

        Member member = getMemberByEmail(email);
        Posting posting = getPosting(postingId);

        // 작성자 검사
        validateMember(member, posting.getMember().getMemberId());

        // 포스팅 검사
        validateUpdatePosting(posting);

        posting.updatePosting(postingUpdateRequestDto);
        return posting.getPostingId();
    }

    /**
     * 4.포스트 삭제
     */
    public Long deletePosting(Long postingId, String email) {
        Member member = getMemberByEmail(email);
        Posting posting =getPosting(postingId);

        // 작성자 검사
        validateMember(member, posting.getMember().getMemberId());

        // 인증 검사.
        isApprovalTrue(posting);

        posting.deletePosting();
        return posting.getPostingId();

    }

    private Posting getPosting(Long postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }

    private Challenge getChallenge(Long challengeId) {
        return challengeRepository.findByChallengeStatusTrueAndChallengeId(challengeId)
                .orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("등록된 멤버가 없습니다."));
    }

    private void isApprovalTrue(Posting posting) {
        if(posting.isPostingApproval()){
            throw new ApiRequestException("이미 인증된 게시글은 삭제할 수 없습니다.");
        }
    }

    private void validateMember(Member member, Long memberId) {
        if (!memberId.equals(member.getMemberId())) {
            throw new ApiRequestException("해당 게시물에 대한 수정 권한이 없습니다.");
        }
    }

    private void duplicatePosting(Member member, Challenge challenge) {

        LocalDateTime now = LocalDate.now().atStartOfDay();

        boolean sat = 6 == now.getDayOfWeek().getValue();
        boolean sun = 7 == now.getDayOfWeek().getValue();

        if ( sat || sun){
            if (challenge.getChallengeHoliday().equals("0,6") ){
                throw new ApiRequestException("주말에 작성 불가능한 챌린지 입니다!");
            }
        }

        boolean posting = postingQueryRepository.existsTodayPosting(now,member.getMemberId(),challenge.getChallengeId());

        if(posting){
            throw new ApiRequestException("동일한 챌린지에는 한번의 인증글만 작성할 수 있습니다.");
        }

    }

    private void validateUpdatePosting(Posting posting) {
        LocalDateTime now = LocalDate.now().atStartOfDay();

        if(posting.getCreatedAt().isBefore(now)){
            throw new ApiRequestException("작성 후 하루가 지나면 수정 할 수 없습니다.");
        }

        if(posting.isPostingApproval()){
            throw new ApiRequestException("인증된 게시글은 수정할 수 없습니다.");
        }
    }

    private void checkMemberCountAndAddPoint(Posting posting, Long memberCount) {
        if(memberCount /2 <= posting.getPostingCount()){
            if(!posting.isPostingApproval()){
                PointHistory pointHistory = new PointHistory(1L, posting); // 몇점받는지 첫번쨰 파라미터로 들어가야함
                pointHistoryRepository.save(pointHistory);
                posting.getMember().updatePoint(1L);
                posting.updateApproval();
                posting.updatePoint();
            }
        }
    }

}
