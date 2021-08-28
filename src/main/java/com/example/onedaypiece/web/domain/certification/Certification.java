package com.example.onedaypiece.web.domain.certification;


import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Certification extends Timestamped {

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

    @Builder
    public Certification(Member member, Posting posting) {
        this.member=member;
        this.posting = posting;
    }

    public static Certification createCertification(Member member, Posting posting) {
        Certification certification =Certification
                .builder()
                .member(member)
                .posting(posting)
                .build();

        posting.addCount();
        return certification;
    }
}
