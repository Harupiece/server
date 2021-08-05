//package com.example.onedaypiece.integration;
//
//import com.example.onedaypiece.service.MemberService;
//import com.example.onedaypiece.web.domain.member.Member;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.dto.request.mypage.ProfileUpdateRequestDto;
//import com.example.onedaypiece.web.dto.request.mypage.PwUpdateRequestDto;
//import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class MemberIntegrationTest {
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    Long memberId = 100L;
//    Member member = null;
//
//    @Test
//    @Order(1)
//    @DisplayName("회원 가입")
//    void test1() {
//        // given
//        String email = "integ@naver.com";
//        String nickname = "통합테스트닉네임";
//        String password = "1234";
//        String passwordConfirm ="1234";
//        String profileImg = "https://onedaypiece-shot-image.s3.ap-northeast-2.amazonaws.com/%5Bobject+File%5DMon+Aug+02+2021+15%3A44%3A07+GMT%2B0900+(%ED%95%9C%EA%B5%AD+%ED%91%9C%EC%A4%80%EC%8B%9C).jpg";
//
//        SignupRequestDto requestDto = new SignupRequestDto(email, password, nickname, passwordConfirm, profileImg);
//        // when
//        memberService.registMember(requestDto);
//        member = memberRepository.findByEmail(email).orElse(null);
//        // then
//        assertNotNull(member.getMemberId());
//        assertEquals(email,member.getEmail());
//        assertEquals(nickname,member.getNickname());
//        assertEquals(true,passwordEncoder.matches(password,member.getPassword()));
//        assertEquals(profileImg,member.getProfileImg());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("프로필 이미지와 닉네임 변경")
//    void test2() {
//        // given
//        String nickname = "변경닉네임짱짱맨"; // "프로필변경" 이미존재하는 닉네임
//        String profileImg = "변경짱짱맨";
//        String email = "integ@naver.com";
//        // when
//        ProfileUpdateRequestDto requestDto = new ProfileUpdateRequestDto(nickname, profileImg);
//        memberService.updateProfile(requestDto, email); // 이미 존재하는 닉네임입니다.
//        member = memberRepository.findByEmail(email).orElse(null);
//        // then
//        assertEquals(nickname, member.getNickname());
//        assertEquals(profileImg, member.getProfileImg());
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("비밀번호 변경")
//    void test3() {
//        // given
//        String currentPw = "12345";
//        String newPw = "1234";
//        String newPwCheck = "1234";
//        String email = "integ@naver.com";
//        // when
//        PwUpdateRequestDto requestDto = new PwUpdateRequestDto(currentPw, newPw, newPwCheck);
//        memberService.updatePassword(requestDto,email);
//        member = memberRepository.findByEmail(email).orElse(null);
//        // then
//        assertEquals(false, passwordEncoder.matches(currentPw, member.getPassword()));
//    }
//}
