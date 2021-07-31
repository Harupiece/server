package com.example.onedaypiece.util;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    final PostingRepository postingRepository;
//    01 00 00
    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<Posting> postingList = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkTrue(today);

        // 벌크성 쿼리 업데이트
        int updateResult = postingRepository.updatePostingStatus(postingList);

        log.info("updateResult 벌크 연산 result: {} ",updateResult);
    }
}
