package com.example.onedaypiece.web.domain.member;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Nested
    @DisplayName("회원가입을 통한 회원 객체 생성")
    class CreateMember{

        private Long memberId;
        private String email;
        private String password;
        private String nickname;
        private MemberRole memberRole;
        private String profileImg;
        private Long memberStatus;
        private Point point;

        @BeforeEach
        void setUp(){
            memberId = 100L;
            email = "test1@naver.com";
            password = "1234";
            nickname = "테스트닉네임1";
            memberRole = MemberRole.MEMBER;
            profileImg = "프로필이미지";
            memberStatus = 1L;
            point = new Point();
        }

        @Test
        @DisplayName("정상 케이스")
        void createMember_Normal(){
            // given
            SignupRequestDto requestDto = new SignupRequestDto(
                    email,
                    password,
                    nickname,
                    "1234",
                    profileImg
            );
            // when
            Member member = new Member(requestDto, point);

            // then
            assertNull(member.getMemberId());
            assertEquals(email, member.getEmail());
            assertEquals(password, member.getPassword());
            assertEquals(nickname, member.getNickname());
            assertEquals(profileImg, member.getProfileImg());
            assertEquals(memberRole, member.getRole());
            assertEquals(memberStatus, member.getMemberStatus());
            assertEquals(point, member.getPoint());
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases{

            @Nested
            @DisplayName("회원 이메일")
            class memberId{
                @Test
                @DisplayName("null")
                void fail1(){
                    // given
                    email = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()-> {
                        new SignupRequestDto(email, password, nickname, "1234",profileImg);
                    });
                    // then
                    assertEquals("email(ID)를 입력해주세요", exception.getMessage());
                }

                @Test
                @DisplayName("empty")
                void fail2(){
                    // given
                    email = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()-> {
                        new SignupRequestDto(email,password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("email(ID)를 입력해주세요", exception.getMessage());
                }

                @Test
                @DisplayName("이메일형식이 아닌경우")
                void fail3(){
                    // given
                    email = "teset1";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email,password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("올바른 이메일 형식이 아닙니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("패스워드")
            class memberPassword{
                @Test
                @DisplayName("null")
                void fail1(){
                    // given
                    password = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()-> {
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("패스워드가 null이므로 입력해 주세요.", exception.getMessage());
                }

                @Test
                @DisplayName("empty")
                void fail2(){
                    // given
                    password = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class,()->{
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("패스워드를 입력해 주세요.", exception.getMessage());
                }

                @Test
                @DisplayName("4자리 보다 적은경우")
                void fail3(){
                    // given
                    password = "123";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("비밀번호는  4~20자리를 사용해야 합니다.", exception.getMessage());
                }

                @Test
                @DisplayName("20자리 보다 큰경우")
                void fail4(){
                    // given
                    password = "123456789123456789123";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("비밀번호는  4~20자리를 사용해야 합니다.", exception.getMessage());
                }

                @Test
                @DisplayName("비밀번호확인이랑 다른경우")
                void fail5(){
                    // given
                    password = "12345";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("비밀번호가 서로같지않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("패스워드 확인")
            class memberPasswordCheck{
                @Test
                @DisplayName("null")
                void fail1(){
                    // given
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email,password, nickname, null, profileImg);
                    });
                    // then
                    assertEquals("패스워드가 null이므로 입력해 주세요.", exception.getMessage());
                }
                @Test
                @DisplayName("empty")
                void fail2(){
                    // given
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email,password, nickname, "", profileImg);
                    });
                    // then
                    assertEquals("패스워드를 입력해 주세요.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("닉네임")
            class memberNickname{
                @Test
                @DisplayName("null")
                void fail1(){
                    // given
                    nickname = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("닉네임을 입력해주세요", exception.getMessage());
                }

                @Test
                @DisplayName("empty")
                void fail2(){
                    // given
                    nickname = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.
                            class, ()->{
                        new SignupRequestDto(email, password, nickname, "1234", profileImg);
                    });
                    // then
                    assertEquals("닉네임을 입력해주세요", exception.getMessage());
                }
            }
        }
    }

}