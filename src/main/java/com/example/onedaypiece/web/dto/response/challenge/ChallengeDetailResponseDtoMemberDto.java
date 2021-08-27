package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChallengeDetailResponseDtoMemberDto {
    private Long memberId;
    private String nickname;
    private String profileImg;

    @Builder
    @QueryProjection
    public ChallengeDetailResponseDtoMemberDto(Long memberId, String nickname, String profileImg) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public static ChallengeDetailResponseDtoMemberDto createChallengeDetailMember(Member member) {
        return ChallengeDetailResponseDtoMemberDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .build();
    }
}
