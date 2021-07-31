package com.example.onedaypiece.web.dto.request.posting;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostingCreateRequestDto {

    private String postingImg;
    private String postingContent;
    private Long memberId;
    private Long challengeId;

    // 테스트 코드용 생성자
    public PostingCreateRequestDto(String postingImg, String postingContent, long memberId, long challengeId) {

        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this. memberId = memberId;
        this.challengeId =challengeId;
    }

}
