package com.example.onedaypiece.web.domain.member;



import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.point.Point;
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
    private String profileImg;

    @Column
    private Long memberStatus;


    @OneToMany
    private List<Point> points = new ArrayList<>();


    @Column(nullable = true)
    private String kakaoEmail;



    public Member(String email, String password, String nickname){

    }
}
