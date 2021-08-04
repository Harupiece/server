package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.point.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostingTestDto {

    private Long pointHistoryId;
    private LocalDateTime createdAt;
    private String challengeTitle;
    private Long getPoint;
    private Long memberId;
    private String nickname;
    private String profileImg;
    private Long acquiredPoint;
    private Point point;

}
