package com.example.onedaypiece.web.dto.response.posting;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostingListDto {
    private List<PostingResponseDto> postList = new ArrayList<>();
    private boolean hasNext;

    @Builder
    public PostingListDto(List<PostingResponseDto> postList, boolean hasNext) {
        this.postList = postList;
        this.hasNext = hasNext;
    }

    public static PostingListDto createPostingListDto(List<PostingResponseDto> postingResponseDtoList, boolean hasNext) {

        return PostingListDto.builder()
                .postList(postingResponseDtoList)
                .hasNext(hasNext)
                .build();
    }
}
