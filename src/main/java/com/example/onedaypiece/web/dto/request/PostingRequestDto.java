package com.example.onedaypiece.web.dto.request;


import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;

@Getter
public class PostingRequestDto {

    private String postingImg;
    private String postingContent;
    private boolean postingStatus;
    private boolean postingApproval;
    private boolean postingModifyOk;
    private boolean postingPoint;
    private Long postingCount;
    private Member member;
    private Challenge challenge;


}
