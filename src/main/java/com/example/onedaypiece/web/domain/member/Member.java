package com.example.onedaypiece.web.domain.member;



import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Member extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column
    private String profileImg;

    @Column
    private Long memberStatus;


    @OneToMany
    private List<Point> points = new ArrayList<>();


//    @Column(nullable = true)
//    private String kakaoEmail;


    public Member(SignupRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
        this.profileImg = requestDto.getProfileImg();
        this.memberStatus = 1L;
        this.role = MemberRole.MEMBER;
    }

    public Member(String email, String password, String nickname, String profileImg, Long memberStatus){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.memberStatus = 1L;
        this.role = MemberRole.MEMBER;
    }
}

