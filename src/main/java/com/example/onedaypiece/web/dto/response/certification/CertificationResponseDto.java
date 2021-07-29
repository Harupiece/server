package com.example.onedaypiece.web.dto.response.certification;

import com.example.onedaypiece.web.domain.certification.Certification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class CertificationResponseDto {
    private Long memberId;
    private String nickName;

    public CertificationResponseDto(Certification certification) {
        this.memberId =certification.getMember().getMemberId();
        this.nickName=certification.getMember().getNickname();
    }
}
