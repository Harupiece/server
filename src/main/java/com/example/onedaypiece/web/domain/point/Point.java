package com.example.onedaypiece.web.domain.point;


import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Point extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pointId;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Certification certification;

    @Column
    private Long totalPoint;

}
