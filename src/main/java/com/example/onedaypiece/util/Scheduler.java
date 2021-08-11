package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
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

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostingRepository postingRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ChallengeRepository challengeRepository;

    private final LocalDateTime today = LocalDate.now().atStartOfDay();

    //    01 00 00
    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        List<Posting> postingList = postingRepository.findSchedulerUpdatePosting(today);
        // 벌크성 쿼리 업데이트
        int updateResult = postingRepository.updatePostingStatus(postingList);

        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void certificationKick() {
        List<ChallengeRecord> challengeMember = challengeRecordRepository.findAllByChallenge();

        //진행중인 챌린지 리스트
        List<Long> challengeId = challengeMember.stream()
                .map(challengeRecord -> challengeRecord.getChallenge().getChallengeId())
                .distinct()
                .collect(Collectors.toList());

        // 챌린지 참여중인 멤버
        List<Long> memberId = challengeMember.stream()
                .map(challengeRecord -> challengeRecord.getMember().getMemberId())
                .distinct()
                .collect(Collectors.toList());

        // 인증 글 올렸지만 인증 받지 못한 친구
        List<Posting> postingList = postingRepository.findPostingListTest(challengeId, memberId, today);
        for (Posting challengeRecord : postingList) {
            System.out.println("인증 글 올렸지만 인증 받지 못한 친구");
            System.out.println("challengeRecord = " + challengeRecord.toString());
            System.out.println("challengeRecord.getMember().getMemberId() = " + challengeRecord.getMember().getMemberId());
            System.out.println("challengeRecord.getChallenge().getChallengeId() = " + challengeRecord.getChallenge().getChallengeId());
        }


        // 인증글 작성하지 않은 사람.
        List<ChallengeRecord> postingList2 = challengeRecordRepository.findPostingListTest2(challengeId);
        for (ChallengeRecord challengeRecord : postingList2) {
            System.out.println("인증글 작성하지 않은 사람.");
            System.out.println("challengeRecord = " + challengeRecord.toString());
            System.out.println("challengeRecord.getMember().getMemberId() = " + challengeRecord.getMember().getMemberId());
            System.out.println("challengeRecord.getChallenge().getChallengeId() = " + challengeRecord.getChallenge().getChallengeId());
        }
        System.out.println(postingList2.size());

        List<Long> kickMember = postingList.stream()
                .map(posting -> posting.getMember().getMemberId())
                .collect(Collectors.toList());
        for (Long aLong : kickMember) {
            System.out.println("인증 글 올렸지만 인증 받지 못한 친구");
            System.out.println("aLong = " + aLong);
        }


        List<Long> kickMember2 = postingList2.stream()
                .map(posting -> posting.getMember().getMemberId())
                .collect(Collectors.toList());
        for (Long aLong : kickMember2) {
            System.out.println("인증 글안올린친구");
            System.out.println("aLong = " + aLong);
        }


        List<Long> kickChallenge = postingList.stream()
                .map(posting -> posting.getChallenge().getChallengeId())
                .collect(Collectors.toList());

        for (Long aLong : kickChallenge) {
            System.out.println("인증 올렸지만 인증 못받은 친구 ㅊ ㅐㄹ린지 아이디");
            System.out.println("aLong = " + aLong);
        }

        List<Long> kickChallenge2 = postingList2.stream()
                .map(posting -> posting.getChallenge().getChallengeId())
                .collect(Collectors.toList());
        for (Long aLong : kickChallenge2) {
            System.out.println("인증 글안올린친구 챌린지 아이디");
            System.out.println("aLong = " + aLong);
        }


        kickMember.addAll(kickMember2);
        kickChallenge.addAll(kickChallenge2);


        for (Long aLong : kickMember) {
            System.out.println("aLong Member= " + aLong);
        }
        for (Long aLong : kickChallenge) {
            System.out.println("aLong challenge = " + aLong);
        }


        int result = challengeRepository.kickMemberOnChallenge(kickMember,kickChallenge);

        System.out.println("result = " + result);

    }

    @Async
    @Scheduled(cron = "03 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
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
        log.info(today + " / id: " + challenge.getChallengeId() + " Challenge Start");
    }

    private void whenChallengeEnd(ChallengeRecord record, Challenge challenge) {
        challenge.updateChallengeProgress(3L);
        record.setStatusFalse();
        log.info(today + " / id: " + challenge.getChallengeId() + " Challenge End");

        Member member = record.getMember();
        List<Posting> postingList = postingRepository.findAllByChallengeAndPostingApprovalTrue(challenge);
        long certificatedPostingCount = postingList.stream().filter(p -> p.getMember().equals(member)).count();

        if (canGetChallengePoint(challenge, certificatedPostingCount)) { // 80% 이상 인증샷을 올렸는가?
            final Long getPoint = certificatedPostingCount *
                    50L * (challenge.getCategoryName().equals(CategoryName.OFFICIAL) ? 2L : 1L);
            PointHistory pointHistory = new PointHistory(getPoint, record);
            pointHistoryRepository.save(pointHistory);
            member.updatePoint(getPoint);
            record.updateChallengePointTrue();
        }
    }

    private boolean isChallengeTimeToStart(Challenge c) {
        return c.getChallengeProgress() == 1L &&
                (setTimeToZero(c.getChallengeStartDate()).isEqual(today) ||
                        (setTimeToZero(c.getChallengeStartDate()).isBefore(today)));
    }

    private boolean isChallengeTimeToEnd(Challenge c) {
        return c.getChallengeProgress() == 2L && setTimeToZero(c.getChallengeEndDate()).isBefore(today);
    }

    private boolean canGetChallengePoint(Challenge challenge, Long certificatedPostingCount) {
        return certificatedPostingCount /
                ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), challenge.getChallengeEndDate()) * 100 > 80;
    }

    private LocalDateTime setTimeToZero(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0);
    }
}
