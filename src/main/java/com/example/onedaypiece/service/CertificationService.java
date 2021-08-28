package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.certification.CertificationRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.certification.CertificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;


    @Transactional
    public Long createCertification(CertificationRequestDto certificationRequestDto, UserDetails userDetails) {
        Posting posting = getPosting(certificationRequestDto.getPostingId());
        Member member = getMemberByEmail(userDetails.getUsername());

        Long memberCount = certificationRequestDto.getTotalNumber(); // <- 참여인원 있음

        // 인증 했는지 여부 확인
        duplicateCertification(posting,member);

        Certification certification = Certification.createCertification(member, posting);

        certificationRepository.save(certification);

        //50% 이상 여기에 pointHistory추가되는거임
        checkMemberCountAndAddPoint(posting, memberCount);

        return posting.getPostingId();
    }


    // 인증 인원 50% 넘으면 승인
    private void checkMemberCountAndAddPoint(Posting posting, Long memberCount) {

        if(memberCount /2 <= posting.getPostingCount()){
            if(!posting.isPostingApproval()){
                PointHistory pointHistory = new PointHistory(1L, posting); // 몇점받는지 첫번쨰 파라미터로 들어가야함
                pointHistoryRepository.save(pointHistory);
                posting.getMember().updatePoint(1L);
                posting.updateApproval(true);
                posting.updatePoint();
            }
        }
    }

    private void duplicateCertification(Posting posting, Member member){
        if(certificationRepository.existsByPostingAndMember(posting,member)){
            throw new ApiRequestException("이미 인증한 게시물입니다!");
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("등록된 유저가 없습니다."));
    }

    private Posting getPosting(Long postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }
}
