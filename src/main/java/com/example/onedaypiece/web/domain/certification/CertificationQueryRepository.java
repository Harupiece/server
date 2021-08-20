package com.example.onedaypiece.web.domain.certification;

import com.example.onedaypiece.web.dto.query.certification.CertificationQueryDto;
import com.example.onedaypiece.web.dto.query.certification.QCertificationQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.domain.certification.QCertification.*;

@Repository
@RequiredArgsConstructor
public class CertificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CertificationQueryDto> findAllByPosting(Long challengeId){

        return queryFactory.select(new QCertificationQueryDto(
                certification.posting.postingId,
                certification.member.memberId))
                .from(certification)
                .join(certification.posting)
                .where(certification.posting.challenge.challengeId.eq(challengeId))
                .fetch();
    }
}
