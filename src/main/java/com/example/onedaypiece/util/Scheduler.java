package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostingRepository postingRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final LocalDateTime today = LocalDate.now().atStartOfDay();

    //    01 00 00
    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        List<Posting> postingList = postingRepository.findSchedulerPosting(today);

        // 벌크성 쿼리 업데이트
        int updateResult = postingRepository.updatePostingStatus(postingList);

        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Async
    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void challengeStatusUpdate() {
        List<ChallengeRecord> recordList = challengeRecordRepository.findAllByChallengeStatusTrue();
        List<Challenge> updatedChallengeList = new ArrayList<>();

        for (ChallengeRecord record : recordList) {
            Challenge challenge = record.getChallenge();

            if (!updatedChallengeList.contains(challenge)) {
                updatedChallengeList.add(challenge);

                if (isChallengeTimeToStart(challenge)) {
                    whenChallengeStart(challenge);

                } else if (isChallengeTimeToEnd(challenge)) {
                    whenChallengeEnd(record, challenge);
                }
            }
        }
    }

    private void whenChallengeStart(Challenge challenge) {
        challenge.updateChallengeProgress(2L);
        log.info("id: " + challenge.getChallengeId() + " Challenge Start");
    }

    private void whenChallengeEnd(ChallengeRecord record, Challenge challenge) {
        challenge.updateChallengeProgress(3L);
        record.setStatusFalse();
        log.info("id: " + challenge.getChallengeId() + " Challenge End");

        Member member = record.getMember();
        List<Posting> postingList = postingRepository.findAllByChallengeAndPostingApprovalTrue(challenge);
        long certificatedPostingCount = postingList.stream().filter(p -> p.getMember().equals(member)).count();

        if (canGetChallengePoint(challenge, certificatedPostingCount)) { // 80% 이상 인증을 받았는가?
            final Long getPoint = certificatedPostingCount * 50L;
            PointHistory pointHistory = new PointHistory(getPoint, record);
            pointHistoryRepository.save(pointHistory);
            member.updatePoint(getPoint);
            record.updateChallengePointTrue();
        }
    }

    private boolean isChallengeTimeToStart(Challenge c) {
        return c.getChallengeProgress() == 1L && setTimeToZero(c.getChallengeStartDate()).isEqual(today);
    }

    private boolean isChallengeTimeToEnd(Challenge c) {
        return c.getChallengeProgress() == 2L && setTimeToZero(c.getChallengeEndDate()).isEqual(today);
    }

    private boolean canGetChallengePoint(Challenge challenge, Long certificatedPostingCount) {
        return certificatedPostingCount /
                ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), challenge.getChallengeEndDate()) * 100 > 80;
    }

    private LocalDateTime setTimeToZero(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0);
    }
}
