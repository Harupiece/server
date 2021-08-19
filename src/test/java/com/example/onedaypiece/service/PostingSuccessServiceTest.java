package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.certification.CertificationRepository;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.member.MemberRole;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostingSuccessServiceTest {

    @Autowired
    PostingRepository postingRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    PostingService postingService;


    @BeforeEach
    void setUp() {

        List<Posting> postingList = new ArrayList<>();

        for (Long i = 1L; i <10 ; i++) {
            PostingCreateRequestDto dto = PostingCreateRequestDto.builder()
                    .postingImg("테스트이미지")
                    .postingContent("테스트콘텐츠")
                    .challengeId(i)
                    .build();
            Point point = Point.builder()
                    .pointId(i)
                    .acquiredPoint(0L)
                    .build();
            Member member = Member.builder()
                    .memberId(i)
                    .email("qwer@qwer.com")
                    .profileImg("test.jpg")
                    .nickname("김진태")
                    .password("1234")
                    .memberStatus(1L)
                    .point(point)
                    .role(MemberRole.MEMBER)
                    .build();
            Challenge challenge = Challenge.builder()
                    .challengeId(i)
                    .challengeTitle("테스트 챌린지 타이틀")
                    .challengeContent("테스트 챌린지 콘텐츠")
                    .categoryName(CategoryName.EXERCISE)
                    .challengeImgUrl("challengeTest.jpg")
                    .challengeGood("good.jpg")
                    .challengeBad("bad.jpg")
                    .challengeHoliday("")
                    .build();
            Posting posting = Posting.createPosting(dto, member, challenge);
            postingList.add(posting);
        }
            postingRepository.saveAll(postingList);
    }

    @DisplayName("포스팅 작성 성공")
    @Test
    @Transactional
    void createPosting (){

        PostingCreateRequestDto dto = PostingCreateRequestDto.builder()
                .postingImg("테스트이미지")
                .postingContent("테스트콘텐츠")
                .challengeId(1L)
                .build();

        Member member = memberRepository.findById(1L).get();
        Challenge challenge = challengeRepository.findById(dto.getChallengeId()).get();

        Posting posting = Posting.createPosting(dto, member, challenge);
        Certification certification = Certification.createCertification(member,posting);

        postingRepository.save(posting);
        certificationRepository.save(certification);

        Certification certificationResult = certificationRepository.findById(certification.getCertificationId()).orElseThrow(()->new ApiRequestException("no certification"));
        Posting postingResult = postingRepository.findById(posting.getPostingId()).get();

        Assertions.assertEquals(posting.getChallenge(),challenge);
        Assertions.assertEquals(posting,postingResult);
        Assertions.assertEquals(certificationResult.getPosting(),posting);

    }

}