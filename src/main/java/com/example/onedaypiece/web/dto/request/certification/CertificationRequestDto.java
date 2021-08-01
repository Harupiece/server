package com.example.onedaypiece.web.dto.request.certification;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CertificationRequestDto {

    private Long postingId;
    private Long totalNumber;

}
