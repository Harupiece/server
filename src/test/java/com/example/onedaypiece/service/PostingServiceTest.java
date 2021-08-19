//package com.example.onedaypiece.service;
//
//import com.example.onedaypiece.web.domain.certification.CertificationRepository;
//import com.example.onedaypiece.web.domain.challenge.CategoryName;
//import com.example.onedaypiece.web.domain.challenge.Challenge;
//import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
//import com.example.onedaypiece.web.domain.member.Member;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.domain.member.MemberRole;
//import com.example.onedaypiece.web.domain.point.Point;
//import com.example.onedaypiece.web.domain.point.PointRepository;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//public class PostingServiceTest {
//
//    @Spy
//    @InjectMocks
//    PostingService postingService;
//
//    @Mock
//    PostingRepository postingRepository;
//
//    @Mock
//    MemberRepository memberRepository;
//
//    @Mock
//    CertificationRepository certificationRepository;
//
//    @Mock
//    ChallengeRepository challengeRepository;
//
//    @Mock
//    PointRepository pointRepository;
//
//
//    @DisplayName("포스팅 작성 성공")
//    @Test
//    void createPosting (){
//        //given
//
//        // dto
//        PostingCreateRequestDto dto = PostingCreateRequestDto.builder()
//                .postingImg("테스트이미지")
//                .postingContent("테스트콘텐츠")
//                .challengeId(1L)
//                .build();
//        // 포인트
//        Point point = Point.builder()
////                .pointId(200L)
//                .acquiredPoint(0L)
//                .build();
//
//        pointRepository.save(point);
//        // 멤버
//        Member member = Member.builder()
////                .memberId(200L)
//                .email("qwer@qwer.com")
//                .profileImg("test.jpg")
//                .nickname("김진태")
//                .password("1234")
//                .memberStatus(1L)
//                .point(point)
//                .role(MemberRole.MEMBER)
//                .build();
//
//        memberRepository.save(member);
//        //챌린지
//        Challenge challenge = Challenge.builder()
////                .challengeId(200L)
//                .challengeTitle("테스트 챌린지 타이틀")
//                .challengeContent("테스트 챌린지 콘텐츠")
//                .categoryName(CategoryName.EXERCISE)
//                .challengeImgUrl("challengeTest.jpg")
//                .challengeGood("good.jpg")
//                .challengeBad("bad.jpg")
//                .challengeHoliday("")
//                .build();
//
//        challengeRepository.save(challenge);
//
//
//
//
//        //mocking
//        given(pointRepository.findById(200L)).willReturn(Optional.of(point));
//        given(memberRepository.findById(200L)).willReturn(Optional.of(member));
//        given(challengeRepository.save(challenge)).willReturn(challenge);
//
//        System.out.println("challenge.getChallengeId() = " + challenge.getChallengeId());
//
//        Long posting = postingService.createPosting(dto, "qwer@qwer.com");
//
//        System.out.println("posting = " + posting);
//
//
//    }
//
//}
