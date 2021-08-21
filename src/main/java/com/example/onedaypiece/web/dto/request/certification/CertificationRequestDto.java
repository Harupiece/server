package com.example.onedaypiece.web.dto.request.certification;


import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CertificationRequestDto {

    private Long postingId;
    private Long totalNumber;

    @Builder
    public CertificationRequestDto(Long postingId, Long totalNumber) {
        this.postingId = postingId;
        this.totalNumber = totalNumber;
    }
}
