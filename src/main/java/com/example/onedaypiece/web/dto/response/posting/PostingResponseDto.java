package com.example.onedaypiece.web.dto.response.posting;

import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.dto.response.login.MemberResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
public class PostingResponseDto {



    private Long postingId;
    private String nickName;
    private String profileImg;
    private String postingImg;
    private String postingContent;
    private Long certificationCount;
    private boolean isPostingApproval;
    private boolean postingModifyOk;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public PostingResponseDto(Posting posting) {
        this.postingId =posting.getPostingId();
        this.postingContent = posting.getPostingContent();
        this.nickName =posting.getMember().getNickname();
        this.profileImg = posting.getMember().getProfileImg();
        this.postingImg = posting.getPostingImg();
        this.certificationCount=posting.getCertificationCount();
        this.isPostingApproval =posting.isPostingApproval();
        this.postingModifyOk =posting.isPostingModifyOk();
        this.createdAt = posting.getCreatedAt();
        this.modifiedAt =posting.getModifiedAt();

    }
}
