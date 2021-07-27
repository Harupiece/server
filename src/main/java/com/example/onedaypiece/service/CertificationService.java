package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.certification.CertificationRepository;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.certification.CertificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CertificationService {

    private  final CertificationRepository certificationRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;


    public Long createCertification(CertificationRequestDto certificationRequestDto) {
        Posting posting = getPosting(certificationRequestDto.getPostingId());
        Member member = getMemberById(certificationRequestDto.getMemberId());

        Certification certification = Certification.createCertification(member,posting);
        certificationRepository.save(certification);

        return certification.getCertificationId();

    }



    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }
    private Posting getPosting(Long postingId) {
        return postingRepository.findById(postingId).orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }
}
