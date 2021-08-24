package com.example.onedaypiece.web.dto.request.posting;


import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostingCreateRequestDto {

    private String postingImg;
    @NotBlank(message = "내용이 비어있어요!")
    private String postingContent;
    private Long memberId;
    private Long challengeId;
    private Long totalNumber;

    @Builder
    public PostingCreateRequestDto(String postingImg, String postingContent, Long memberId, Long challengeId, Long totalNumber) {
        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.memberId = memberId;
        this.challengeId = challengeId;
        this.totalNumber = totalNumber;
    }

}
