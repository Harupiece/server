package com.example.onedaypiece.web.dto.query.certification;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CertificationQueryDto {

    private Long postingId;
    private Long memberId;


    @QueryProjection
    public CertificationQueryDto(Long postingId, Long memberId) {
        this.postingId = postingId;
        this.memberId = memberId;
    }
}
