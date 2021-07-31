package com.example.onedaypiece.web.domain.member;

import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.dto.request.mypage.ProfileUpdateRequestDto;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(indexes = {@Index(name = "idx_member_status", columnList = "member_status")})
public class Member extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="member_id")
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

    @Column(name = "member_status")
    private Long memberStatus;

    // fk를가져야하니까
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINT_ID")
    private Point point;



    public Member(SignupRequestDto requestDto, Point point){
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
        this.profileImg = requestDto.getProfileImg();
        this.memberStatus = 1L;
        this.role = MemberRole.MEMBER;
        this.point = point;
    }

    public Member(String email, String password, String nickname, String profileImg){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.memberStatus = 1L;
        this.role = MemberRole.MEMBER;
    }

    // 마이페이지 비밀번호 수정
    public void updatePassword(String newpassword){
        this.password = newpassword;
    }

    // 마이페이지 프로필 수정
    public void updateProfile(ProfileUpdateRequestDto requestDto){
        this.nickname = requestDto.getNickname();
        this.profileImg = requestDto.getProfileImage();
    }

    // 업데이트완료된 토탈 포인트 보내주기
    public Long updatePoint(Long getPoint) {
        Long before = this.getPoint().getAcquiredPoint();
        Long result;
        result = before + getPoint;
        this.getPoint().setAcquiredPoint(result);
        return result;
    }





}

