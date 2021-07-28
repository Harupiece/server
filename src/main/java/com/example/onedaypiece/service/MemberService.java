package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.security.TokenProvider;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.point.PointRepository;
import com.example.onedaypiece.web.domain.token.RefreshToken;
import com.example.onedaypiece.web.domain.token.RefreshTokenRepository;
import com.example.onedaypiece.web.dto.request.login.LoginRequestDto;
import com.example.onedaypiece.web.dto.request.mypage.MyPageRequestDto;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import com.example.onedaypiece.web.dto.request.token.TokenRequestDto;
import com.example.onedaypiece.web.dto.response.login.LoginResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.MyPageResponseDto;
import com.example.onedaypiece.web.dto.response.token.TokenDto;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    // 회원가입
    @Transactional
    public void registMember(SignupRequestDto requestDto){
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

        if (memberRepository.existsByEmail(email)) {
            throw new ApiRequestException("이미 가입되어 있는 유저입니다");
        }

        // 회원 email(ID)중복확인
        Optional<Member> found = memberRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new ApiRequestException("중복된 사용자 email(ID)가 존재합니다.");
        }

        // 닉네임 중복확인
        if(memberRepository.existsByNickname(nickname)){
            throw  new ApiRequestException("이미 존재하는 닉네임입니다.");
        }

        // 패스워드 인코딩
        String password= passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(password);



        Member member = new Member(requestDto);
        Point point = new Point(member);
        member.add(point);
        memberRepository.save(member);

    }


//    // 로그인
//    @Transactional
//    public TokenDto loginMember(LoginRequestDto requestDto){
//
//        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
//        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
//
//        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
//        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//        // 3. 인증 정보를 기반으로 JWT 토큰 생성
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//        // 4. RefreshToken 저장
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);
//        return tokenDto;
//    }


    // 로그인 2
    @Transactional
    public LoginResponseDto loginMember(LoginRequestDto requestDto){
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                ()-> new ApiRequestException("로그인할떄 아이디가 존재하지않습니다.")
        );
        List<Point> pointList = member.getPoints();
        Long pointSum = 0L;
        for(int i = 0 ; i< pointList.size(); i++){
            pointSum =  pointSum + pointList.get(i).getAcquiredPoint();
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(tokenDto, member, pointSum);
        return loginResponseDto;
    }



    // 토큰 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }


    // 마이 페이지 상세
    @Transactional
    public MyPageResponseDto getMypageInfo(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                ()-> new ApiRequestException("마이페이지 상세에서 찾는 이메일 존재하지않음")
        );

        // 오류나면 PointRepository로 해결하기
        List<Point> pointList = member.getPoints();
        Long pointSum = 0L;
        for(int i = 0 ; i< pointList.size(); i++){
            pointSum =  pointSum + pointList.get(i).getAcquiredPoint();
        }
        System.out.println("포인트합 적히는지 실험: "+pointSum);
        MyPageResponseDto responseDto = new MyPageResponseDto(member, pointSum);
        return responseDto;
    }


    // 마이 페이지 수정
    @Transactional
    public void updateMember(MyPageRequestDto requestDto, String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                ()-> new ApiRequestException("마이페이지수정에서 멤버 수정하는 아이디찾는거실패")
        );

        System.out.println("인코드 되기전: "+requestDto.getPassword());
        String password = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(password);
        System.out.println("인코드된 후: "+requestDto.getPassword());

        member.update(requestDto);
    }
}

