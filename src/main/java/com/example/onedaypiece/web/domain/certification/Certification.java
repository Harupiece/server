package com.example.onedaypiece.web.domain.certification;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Certification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long certificationId;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Posting posting;

    //포인트는 디비상에만 존재하는거
}
