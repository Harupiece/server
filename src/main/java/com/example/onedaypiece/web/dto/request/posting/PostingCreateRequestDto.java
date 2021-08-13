package com.example.onedaypiece.web.dto.request.posting;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    // 테스트 코드용 생성자
    public PostingCreateRequestDto(String postingImg, String postingContent, long memberId, long challengeId) {

        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.memberId = memberId;
        this.challengeId =challengeId;
    }
}
