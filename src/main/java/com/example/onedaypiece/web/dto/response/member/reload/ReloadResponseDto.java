package com.example.onedaypiece.web.dto.response.member.reload;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.member.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReloadResponseDto {

    private MemberResponseDto userInfo;

    @Builder
    public ReloadResponseDto(Member member, int challengeCount, int completeChallengeCount){
        this.userInfo = new MemberResponseDto(member, challengeCount, completeChallengeCount);
    }

    public static ReloadResponseDto createReloadResponseDto(Member membmer,
                                                            int challengeCount,
                                                            int completeChallengeCount){

        return ReloadResponseDto.builder()
                .member(membmer)
                .challengeCount(challengeCount)
                .completeChallengeCount(completeChallengeCount)
                .build();
    }
}
