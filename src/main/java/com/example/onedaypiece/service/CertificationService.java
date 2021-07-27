package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.certification.CertificationRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.point.PointRepository;
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
    private final PointRepository pointRepository;
    private final ChallengeRecordRepository challengeRecordRepository;


    @Transactional
    public Long createCertification(CertificationRequestDto certificationRequestDto, UserDetails userDetails) {
        Posting posting = getPosting(certificationRequestDto.getPostingId());
        Member member = getMemberByEmail(userDetails.getUsername());


        int count = getCount(posting); // <- 참여인원 있음

        // 인증 했는지 여부 확인
        duplicate(posting,member);

        Certification certification = Certification.createCertification(member,posting);

        //50% 이상
        checkMemberCountAndAddPoint(posting, member, count, certification);

        certificationRepository.save(certification);

        return certification.getCertificationId();
    }


    private void checkMemberCountAndAddPoint (Posting posting, Member member, int count, Certification certification) {
        if(count /2 < posting.getPostingCount()){
            Point point = member.updatePoint(member, certification);
//            pointRepository.save(point);
        }
    }

    private void duplicate(Posting posting,Member member){
        if(certificationRepository.existsByPostingAndMember(posting,member)){
            throw new ApiRequestException("이미 인증한 게시물입니다!");
        }
    }
    private int getCount(Posting posting) {
        return challengeRecordRepository
                .findAllByChallenge(posting.getChallenge()).size();
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
