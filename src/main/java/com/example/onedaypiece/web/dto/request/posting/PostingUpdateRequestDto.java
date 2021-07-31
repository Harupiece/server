package com.example.onedaypiece.web.dto.request.posting;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostingUpdateRequestDto {

    private String postingImg;
    private String postingContent;

    //테스트 코드용 생성자
    public PostingUpdateRequestDto(String postingImg, String postingContent) {
        this.postingImg =postingImg;
        this.postingContent = postingContent;
    }
}
