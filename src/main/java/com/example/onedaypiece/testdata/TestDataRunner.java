//package com.example.onedaypiece.testdata;
//
//
//import com.example.onedaypiece.service.ChallengeService;
//import com.example.onedaypiece.service.MemberService;
//import com.example.onedaypiece.web.domain.challenge.CategoryName;
//import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
//import com.example.onedaypiece.web.domain.member.Member;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.domain.point.Point;
//import com.example.onedaypiece.web.domain.point.PointRepository;
//import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class TestDataRunner implements ApplicationRunner {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    PointRepository pointRepository;
//
//    @Autowired
//    ChallengeRepository challengeRepository;
//
//    @Autowired
//    ChallengeService challengeService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        Member testMember1 = new Member("test1@naver.com", passwordEncoder.encode("1234"),"닉네임1","프로필1" );
//        memberRepository.save(testMember1);
//
//        Member testMember2 = new Member("test2@naver.com", passwordEncoder.encode("1234"),"닉네임2","프로필2" );
//        memberRepository.save(testMember2);
//
//
//        Point pointTest1 = new Point(testMember1);
//        pointTest1.setAcquiredPoint(20L);
//        pointRepository.save(pointTest1);
//
//        Point pointTest2 = new Point(testMember1);
//        pointTest2.setAcquiredPoint(30L);
//        pointRepository.save(pointTest2);
////"2021-07-27T20:54:23.123"
//
//        //챌린지저장1
//        ChallengeRequestDto challengeRequestDtoTest1 = new ChallengeRequestDto("챌린지1","내용1","1234",
//                CategoryName.EXERCISE, LocalDateTime.now(),LocalDateTime.now().plusDays(2),
//                "챌린지이미지1","좋은사진1","나쁜사진1","" );
//
////        Challenge challengeTest1 = new Challenge(challengeRequestDtoTest1, testMember1);
////        challengeRepository.save(challengeTest1);
//        challengeService.createChallenge(challengeRequestDtoTest1, testMember1.getEmail());
//
//        //첼린지 저장2
//        ChallengeRequestDto challengeRequestDtoTest2 = new ChallengeRequestDto("챌린지2","내용2","1234",
//                CategoryName.STUDY, LocalDateTime.now(),LocalDateTime.now().plusDays(2),
//                "챌린지이미지2","좋은사진2","나쁜사진2","" );
//
////        Challenge challengeTest2 = new Challenge(challengeRequestDtoTest1, testMember2);
////        challengeRepository.save(challengeTest2);
//        challengeService.createChallenge(challengeRequestDtoTest2, testMember2.getEmail());
//
//
//    }
//}