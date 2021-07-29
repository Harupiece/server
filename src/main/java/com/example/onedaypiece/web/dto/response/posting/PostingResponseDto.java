package com.example.onedaypiece.web.dto.response.posting;

import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.dto.response.certification.CertificationResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostingResponseDto {

    private Long postingId;
    private String nickName;
    private String profileImg;
    private String postingImg;
    private String postingContent;
    private Long postingCount;
    private boolean isPostingApproval;
    private boolean postingModifyOk;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Long> memberResponseDto = new ArrayList<>();
    // 추후 인증 한 사람 이름 필요할 경우
//    private List<CertificationResponseDto> certificationResponseDto;


    public PostingResponseDto(Posting posting) {
        this.postingId =posting.getPostingId();
        this.postingContent = posting.getPostingContent();
        this.nickName =posting.getMember().getNickname();
        this.profileImg = posting.getMember().getProfileImg();
        this.postingImg = posting.getPostingImg();
        this.postingCount =posting.getPostingCount();
        this.isPostingApproval =posting.isPostingApproval();
        this.postingModifyOk =posting.isPostingModifyOk();
        this.createdAt = posting.getCreatedAt();
        this.modifiedAt =posting.getModifiedAt();
        this.memberResponseDto = posting.getCertificationList()
                .stream()
                .map(memberId -> memberId.getMember().getMemberId())
                .collect(Collectors.toList());

        // 추후 인증 한 사람 이름 필요할 경우
//        this.certificationResponseDto = posting.getCertificationList()
//                .stream()
//                .map(CertificationResponseDto::new)
//                .collect(Collectors.toList());
    }



}
