package com.example.onedaypiece.web.domain.certification;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Certification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long certificationId;

    @ManyToOne
    private Posting posting;

    @ManyToOne
    private Member member;

    public Certification(Member member, Posting posting) {
        this.member=member;
        this.posting=posting;
    }

    public static Certification createCertification(Member member, Posting posting) {
        Certification certification =new Certification(member,posting);
        posting.addCount();
        return certification;
    }

}
