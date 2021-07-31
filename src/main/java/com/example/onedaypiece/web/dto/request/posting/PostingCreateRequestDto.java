package com.example.onedaypiece.web.dto.request.posting;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
public class PostingCreateRequestDto {

    private String postingImg;
    private String postingContent;
    private Long memberId;
    private Long challengeId;
}
