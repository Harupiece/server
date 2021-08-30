package com.example.onedaypiece.web.dto.response.posting;

import com.example.onedaypiece.web.dto.query.certification.CertificationQueryDto;
import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostingResponseDto {

    private Long postingId;
    private String nickName;
    private Long memberId;
    private String profileImg;
    private String postingImg;
    private String postingContent;
    private boolean postingApproval;
    private boolean postingModifyOk;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long postingCount;
    private List<Long> memberResponseDto = new ArrayList<>();

    @Builder
    public PostingResponseDto(Long postingId, String nickName, Long memberId, String profileImg,
                              String postingImg, String postingContent, boolean postingApproval,
                              boolean postingModifyOk, LocalDateTime createdAt, LocalDateTime modifiedAt,
                              Long postingCount, List<Long> memberResponseDto) {
        this.postingId = postingId;
        this.nickName = nickName;
        this.memberId = memberId;
        this.profileImg = profileImg;
        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.postingApproval = postingApproval;
        this.postingModifyOk = postingModifyOk;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.postingCount = postingCount;
        this.memberResponseDto = memberResponseDto;
    }

    public static PostingResponseDto of(PostingListQueryDto posting, List<CertificationQueryDto> certificationList) {
        List<Long> certificationMember = certificationList.stream()
                .filter(certification ->
                        certification.getPostingId().equals(posting.getPostingId()))
                .map(CertificationQueryDto::getMemberId)
                .collect(Collectors.toList());

        return PostingResponseDto.builder()
                .postingId(posting.getPostingId())
                .nickName(posting.getNickName())
                .memberId(posting.getMemberId())
                .profileImg(posting.getProfileImg())
                .postingImg(posting.getPostingImg())
                .postingContent(posting.getPostingContent())
                .postingCount(posting.getPostingCount())
                .postingApproval(posting.isPostingApproval())
                .postingModifyOk(posting.isPostingModifyOk())
                .createdAt(posting.getCreatedAt())
                .modifiedAt(posting.getModifiedAt())
                .memberResponseDto(certificationMember)
                .build();
    }
}
