package com.example.onedaypiece.web.domain.certification;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Certification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="certification_id")
    private Long certificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="posting_id")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
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
