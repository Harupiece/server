package com.example.onedaypiece.web.dto.response.mypage;

import com.example.onedaypiece.web.dto.response.mypage.end.MyPageEndResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.proceed.MypageProceedResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.scheduled.MyPageScheduledResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {

    private MemberHistoryResponseDto memberHistoryResponseDto; // 히스토리
    private MypageProceedResponseDto mypageProceedResponseDto; // 진행중
    private MyPageScheduledResponseDto myPageScheduledResponseDto; // 예정인
    private MyPageEndResponseDto myPageEndResponseDto; // 종료된 챌린지


    @Builder
    public MyPageResponseDto(MemberHistoryResponseDto history, MypageProceedResponseDto proceed,
                             MyPageScheduledResponseDto schedule, MyPageEndResponseDto end){

        this.memberHistoryResponseDto = history;
        this.mypageProceedResponseDto = proceed;
        this.myPageScheduledResponseDto = schedule;
        this.myPageEndResponseDto = end;
    }

    public static MyPageResponseDto createMyPageResponseDto(MemberHistoryResponseDto history,
                                                            MypageProceedResponseDto proceed,
                                                            MyPageScheduledResponseDto schedule,
                                                            MyPageEndResponseDto end){

        return MyPageResponseDto.builder().history(history)
                .proceed(proceed)
                .schedule(schedule)
                .end(end)
                .build();
    }
}
