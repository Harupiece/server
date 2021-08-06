//
//package com.example.onedaypiece.web.domain.challenge;
//
//import com.example.onedaypiece.exception.ApiRequestException;
//import com.example.onedaypiece.web.domain.member.Member;
//import com.example.onedaypiece.web.domain.member.MemberRole;
//import com.example.onedaypiece.web.domain.point.Point;
//import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
//import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ChallengeTest {
//    @Nested
//    @DisplayName("챌린지 생성")
//    class CreateMember{
//
//        private Long challengeId;
//        private String challengeTitle;
//        private String challengeContent;
//        private CategoryName categoryName;
//        private String challengePassword;
//        private LocalDateTime challengeStartDate;
//        private LocalDateTime challengeEndDate;
//        private boolean challengeStatus;
//        private Long challengeProgress;
//        private String challengeImgUrl;
//        private String challengeGood;
//        private String challengeBad;
//        private String challengeHoliday;
//        private Member member;
//
//        @BeforeEach
//        void setUp(){
//            challengeId = 100L;
//            challengeTitle = "챌린지제목";
//            challengeContent = "챌린지내용";
//            categoryName = CategoryName.EXERCISE;
//            challengePassword = "1234";
//            challengeStartDate = LocalDateTime.now().plusDays(2);
//            challengeEndDate = LocalDateTime.now().plusDays(12);
//            challengeImgUrl = "이미지";
//            challengeGood = "좋은이미지";
//            challengeBad = "안좋은 이미지";
//            challengeHoliday = "";
//            member = new Member("test@naver.com", "1234", "닉네임1", "프로필이미지1");
//        }
//
//        @Test
//        @DisplayName("정상 케이스")
//        void createChallenge_Normal(){
//            // given
//            ChallengeRequestDto requestDto = new ChallengeRequestDto(
//                    challengeTitle,
//                    challengeContent,
//                    challengePassword,
//                    categoryName,
//                    challengeStartDate,
//                    challengeEndDate,
//                    challengeImgUrl,
//                    challengeGood,
//                    challengeBad,
//                    challengeHoliday
//            );
//            // when
//            // starteDateTime
//            // Expected :2021-08-05T16:15:01.909
//            // Actual :2021-08-05T00:00:00.909 ?? 이유를 모르겠음
//            Challenge challenge = new Challenge(requestDto, member);
//            // then
//            assertNull(challenge.getChallengeId());
//            assertEquals(challengeTitle, challenge.getChallengeTitle());
//            assertEquals(challengePassword, challenge.getChallengePassword());
//            assertEquals(challengeContent, challenge.getChallengeContent());
//            assertEquals(categoryName, challenge.getCategoryName());
//            assertEquals(challengeImgUrl, challenge.getChallengeImgUrl());
//            assertEquals(challengeGood, challenge.getChallengeGood());
//            assertEquals(challengeBad, challenge.getChallengeBad());
//            assertEquals(challengeHoliday, challenge.getChallengeHoliday());
//            assertEquals(1L, challenge.getChallengeProgress());
//            assertEquals(true, challenge.isChallengeStatus());
//            assertEquals(member, challenge.getMember()); // 여기가 걱정
//        }
//
//        @Nested
//        @DisplayName("실패 케이스")
//        class FailCases{
//            @Nested
//            @DisplayName("제목")
//            class cTitle{
//                @Test
//                @DisplayName("null")
//                void fail1(){
//                    // given
//                    challengeTitle = null;
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("제목이 비었습니다.", exception.getMessage());
//                }
//
//                @Test
//                @DisplayName("empty")
//                void fail2(){
//                    // given
//                    challengeTitle = "";
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("제목이 비었습니다.", exception.getMessage());
//                }
//            }
//
//            @Nested
//            @DisplayName("내용")
//            class cContent{
//                @Test
//                @DisplayName("null")
//                void fail1(){
//                    // given
//                    challengeContent = null;
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("내용이 비었습니다.", exception.getMessage());
//                }
//
//                @Test
//                @DisplayName("empty")
//                void fail2(){
//                    // given
//                    challengeContent = "";
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("내용이 비었습니다.", exception.getMessage());
//                }
//            }
//
//            @Nested
//            @DisplayName("이미지")
//            class cImgUrl{
//                @Test
//                @DisplayName("null")
//                void fail1(){
//                    // given
//                    challengeImgUrl = null;
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("챌린지 이미지가 비었습니다.", exception.getMessage());
//                }
//
//                @Test
//                @DisplayName("empty")
//                void fail2(){
//                    // given
//                    challengeImgUrl = "";
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("챌린지 이미지가 비었습니다.", exception.getMessage());
//                }
//            }
//
//            @Nested
//            @DisplayName("좋은예시")
//            class cGood{
//                @Test
//                @DisplayName("null")
//                void fail1(){
//                    // given
//                    challengeGood = null;
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("좋은예시가 비었습니다.", exception.getMessage());
//                }
//
//                @Test
//                @DisplayName("empty")
//                void fail2(){
//                    // given
//                    challengeGood = "";
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("좋은예시가 비었습니다.", exception.getMessage());
//                }
//            }
//
//            @Nested
//            @DisplayName("나쁜예시")
//            class cBad{
//                @Test
//                @DisplayName("null")
//                void fail1(){
//                    // given
//                    challengeBad = null;
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("나쁜예시가 비었습니다.", exception.getMessage());
//                }
//
//                @Test
//                @DisplayName("empty")
//                void fail2(){
//                    // given
//                    challengeBad = "";
//                    // when
//                    Exception exception = assertThrows(ApiRequestException.
//                            class, ()-> {
//                        new ChallengeRequestDto(challengeTitle,
//                                challengeContent,
//                                challengePassword,
//                                categoryName,
//                                challengeStartDate,
//                                challengeEndDate,
//                                challengeImgUrl,
//                                challengeGood,
//                                challengeBad,
//                                challengeHoliday);
//                    });
//                    // then
//                    assertEquals("나쁜예시가 비었습니다.", exception.getMessage());
//                }
//            }
//
//
//
//        }
//    }
//}
//
