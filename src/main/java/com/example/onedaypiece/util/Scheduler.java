package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.history.UserHistory;
import com.example.onedaypiece.web.domain.history.UserHistoryRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.posting.Posting;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostingRepository postingRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final UserHistoryRepository userHistoryRepository;

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
                    whenChallengeStart(recordList, challenge);
                } else if (isChallengeTimeToEnd(challenge)) {
                    whenChallengeEnd(recordList, record, challenge);
                }
            }
        }
    }

    private boolean isChallengeTimeToStart(Challenge c) {
        return c.getChallengeProgress() == 1L && setTimeToZero(c.getChallengeStartDate()).isEqual(today);
    }

    private boolean isChallengeTimeToEnd(Challenge c) {
        return c.getChallengeProgress() == 2L && setTimeToZero(c.getChallengeEndDate()).isEqual(today);
    }

    private void whenChallengeStart(List<ChallengeRecord> recordList, Challenge c) {
        List<Member> userList = whenSetProgressAndSetUserList(recordList, c, 2L);

        for (Member member : userList) {
            //~님의 ~챌린지가 시작되었어요
            UserHistory history = new UserHistory(member);
            history.setContentWhenChallengeStart(c);
            userHistoryRepository.save(history);
        }
    }

    private List<Member> whenSetProgressAndSetUserList(List<ChallengeRecord> recordList, Challenge c, Long l) {
        c.setChallengeProgress(l);
        return recordList.stream().filter(r -> r.getChallenge().equals(c)).map(ChallengeRecord::getMember).collect(Collectors.toList());
    }

    private void whenChallengeEnd(List<ChallengeRecord> recordList, ChallengeRecord record, Challenge c) {
        List<Member> userList = whenSetProgressAndSetUserList(recordList, c, 3L);
        record.setStatusFalse();

        for (Member member : userList) {
            //축하드려요! ~님의 챌린지가 완료되었어요
            UserHistory challengeHistory = new UserHistory(member);
            challengeHistory.setContentWhenChallengeEnd(c);
            userHistoryRepository.save(challengeHistory);

            //챌린지에 80% 이상 성실히 참여하여 50 * 일수의 포인트를 획득하셨어요
            UserHistory pointHistory = new UserHistory(member);
            pointHistory.setContentEarnPointWhenChallengeEnd(c);
            userHistoryRepository.save(pointHistory);
        }
    }

    private LocalDateTime setTimeToZero(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0);
    }
}
