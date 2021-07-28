package com.example.onedaypiece.web.domain.member;



import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.dto.request.mypage.MyPageRequestDto;
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


    // 관계의주인이 저쪽이다 라고알려주는것것 포인트에서 자기자신을 member라고참조하고있다 관계의 주인은 point다
    @OneToMany(mappedBy = "member")
    private List<Point> points = new ArrayList<>();

//    @Column(nullable = true)
//    private String kakaoEmail;


    public void add(Point point){
        point.setMember(this);  // 애가주인인데
        this.points.add(point); // 이걸하는이유 이이유는 jpa를왜쓰냐 이런질문임 객체지향적으로 하기위해서 쓰는거임
    }

    public Member(SignupRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
        this.profileImg = requestDto.getProfileImg();
        this.memberStatus = 1L;
        this.role = MemberRole.MEMBER;
    }

    public Member(String email, String password, String nickname, String profileImg){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.memberStatus = 1L;
        this.role = MemberRole.MEMBER;
    }

    // 마이페이지 수정
    public void update(MyPageRequestDto requestDto){
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
        this.profileImg = requestDto.getProfileImg();
    }

    public Point updatePoint(Member member, Certification certification) {

        return new Point(member,certification);
    }



}

