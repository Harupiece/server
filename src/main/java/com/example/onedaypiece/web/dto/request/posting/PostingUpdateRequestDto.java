package com.example.onedaypiece.web.dto.request.posting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostingUpdateRequestDto {

    private String postingImg;
    @NotBlank(message = "내용이 비어있어요!")
    private String postingContent;

    //테스트 코드용 생성자
    public PostingUpdateRequestDto(String postingImg, String postingContent) {
        this.postingImg = postingImg;
        this.postingContent = postingContent;
    }
}
