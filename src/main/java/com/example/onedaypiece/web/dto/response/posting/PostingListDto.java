package com.example.onedaypiece.web.dto.response.posting;

import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostingListDto {
    private List<PostingResponseDto> postList;
    private boolean hasNext;

    @Builder
    public PostingListDto(List<PostingResponseDto> postList, boolean hasNext) {
        this.postList = postList;
        this.hasNext = hasNext;
    }


    public static PostingListDto createPostingListDto(Slice<PostingListQueryDto> postingList, List<Certification> certificationList) {
        List<PostingResponseDto> postingResponseDtoList = postingList
                .stream()
                .map(posting -> PostingResponseDto.of(posting, certificationList))
                .collect(Collectors.toList());

        return PostingListDto.builder()
                .postList(postingResponseDtoList)
                .hasNext(postingList.hasNext())
                .build();


    }
}
