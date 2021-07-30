package com.example.onedaypiece.web.dto.response.posting;

import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.dto.response.certification.CertificationResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PostingResponseDto {

    private Long postingId;
    private String nickName;
    private String profileImg;
    private String postingImg;
    private String postingContent;
    private boolean PostingApproval;
    private boolean postingModifyOk;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long postingCount;
    private List<CertificationResponseDto> certificationUserInfo ;
    private List<Long> memberResponseDto;

    public PostingResponseDto(Posting posting,List<Certification> certificationList) {
        this.postingId = posting.getPostingId();
        this.postingContent = posting.getPostingContent();
        this.nickName = posting.getMember().getNickname();
        this.profileImg = posting.getMember().getProfileImg();
        this.postingImg = posting.getPostingImg();
        this.postingCount = posting.getPostingCount();
        this.PostingApproval = posting.isPostingApproval();
        this.postingModifyOk = posting.isPostingModifyOk();
        this.createdAt = posting.getCreatedAt();
        this.modifiedAt = posting.getModifiedAt();
        this.memberResponseDto = certificationList.stream()
                .filter(certification -> certification.getPosting().getPostingId().equals(posting.getPostingId()))
                .map(certification -> certification.getMember().getMemberId())
                .collect(Collectors.toList());

//        this.certificationUserInfo = certificationList.stream()
//                .filter(certification -> certification.getPosting().getPostingId().equals(posting.getPostingId()))
//                .map(CertificationResponseDto::new)
//                .collect(Collectors.toList());
    }
}
