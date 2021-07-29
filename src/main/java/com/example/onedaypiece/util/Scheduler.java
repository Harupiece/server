package com.example.onedaypiece.util;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Scheduler {

    final PostingRepository postingRepository;
//    01 00 00
    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<Posting> postingList = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkTrue();

        for (Posting p : postingList) {
            if (p.getCreatedAt().isBefore(today)) {
                p.updateStatus();
            }
        }
//        postingList.stream()
//                .filter(p -> p.getCreatedAt().isBefore(today))
//                .forEach(Posting::updateStatus);
    }
}
