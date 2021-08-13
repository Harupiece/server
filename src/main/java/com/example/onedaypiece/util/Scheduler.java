//package com.example.onedaypiece.util;
//
//import com.example.onedaypiece.exception.ApiRequestException;
//import com.example.onedaypiece.web.domain.challenge.CategoryName;
//import com.example.onedaypiece.web.domain.challenge.Challenge;
//import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
//import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
//import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
//import com.example.onedaypiece.web.domain.member.Member;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
//import com.example.onedaypiece.web.domain.posting.Posting;
//import com.example.onedaypiece.web.domain.posting.PostingQueryRepository;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import com.example.onedaypiece.web.dto.query.posting.SchedulerPostingDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class Scheduler {
//
//    private final PostingQueryRepository postingQueryRepository;
//    private final PostingRepository postingRepository;
//    private final ChallengeRecordRepository challengeRecordRepository;
//    private final ChallengeRepository challengeRepository;
//    private final PointHistoryRepository pointHistoryRepository;
//    private final MemberRepository memberRepository;
//
//    private final LocalDateTime today = LocalDate.now().atStartOfDay();
//
//    //    01 00 00
//    @Scheduled(cron = "01 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
//    @Transactional
//    public void certificationKick() {
//        List<ChallengeRecord> challengeMember = challengeRecordRepository.findAllByChallenge();
//
//
//        //진행중인 챌린지 리스트
//        List<Long> challengeId = challengeMember.stream()
//                .map(challengeRecord -> challengeRecord.getChallenge().getChallengeId())
//                .distinct()
//                .collect(Collectors.toList());
//
//        // 챌린지 참여중인 멤버
//        List<Long> memberId = challengeMember.stream()
//                .map(challengeRecord -> challengeRecord.getMember().getMemberId())
//                .distinct()
//                .collect(Collectors.toList());
//
//        // 인증 글 올렸지만 인증 받지 못한 친구
//        List<SchedulerPostingDto> postingList = postingQueryRepository.findPostingListTest(challengeId, memberId, today);
//        for (SchedulerPostingDto posting : postingList) {
//            System.out.println("인증 글 올렸지만 인증 받지 못한 친구");
//            System.out.println("challengeRecord = " + posting.toString());
//            System.out.println("challengeRecord.getMember().getMemberId() = " + posting.getMemberId());
//            System.out.println("challengeRecord.getChallenge().getChallengeId() = " + posting.getChallengeId());
//        }
//
//
//        // 인증글 작성하지 않은 사람.
//        List<ChallengeRecord> postingList2 = challengeRecordRepository.findPostingListTest2(challengeId);
//        for (ChallengeRecord challengeRecord : postingList2) {
//            System.out.println("인증글 작성하지 않은 사람.");
//            System.out.println("challengeRecord = " + challengeRecord.toString());
//            System.out.println("challengeRecord.getMember().getMemberId() = " + challengeRecord.getMember().getMemberId());
//            System.out.println("challengeRecord.getChallenge().getChallengeId() = " + challengeRecord.getChallenge().getChallengeId());
//        }
//        System.out.println(postingList2.size());
//
//        List<Long> kickMember = postingList.stream()
//                .map(SchedulerPostingDto::getMemberId)
//                .collect(Collectors.toList());
//        for (Long aLong : kickMember) {
//            System.out.println("인증 글 올렸지만 인증 받지 못한 친구");
//            System.out.println("aLong = " + aLong);
//        }
//
//        List<Long> kickMember2 = postingList2.stream()
//                .map(posting -> posting.getMember().getMemberId())
//                .collect(Collectors.toList());
//        for (Long aLong : kickMember2) {
//            System.out.println("인증 글안올린친구");
//            System.out.println("aLong = " + aLong);
//        }
//
//
//        List<Long> kickChallenge = postingList.stream()
//                .map(SchedulerPostingDto::getMemberId)
//                .collect(Collectors.toList());
//
//        for (Long aLong : kickChallenge) {
//            System.out.println("인증 올렸지만 인증 못받은 친구 ㅊ ㅐㄹ린지 아이디");
//            System.out.println("aLong = " + aLong);
//        }
//
//        List<Long> kickChallenge2 = postingList2.stream()
//                .map(posting -> posting.getChallenge().getChallengeId())
//                .collect(Collectors.toList());
//        for (Long aLong : kickChallenge2) {
//            System.out.println("인증 글안올린친구 챌린지 아이디");
//            System.out.println("aLong = " + aLong);
//        }
//
//
//        kickMember.addAll(kickMember2);
//        kickChallenge.addAll(kickChallenge2);
//
//
//        for (Long aLong : kickMember) {
//            System.out.println("aLong Member= " + aLong);
//        }
//        for (Long aLong : kickChallenge) {
//            System.out.println("aLong challenge = " + aLong);
//        }
//
//
//        int result = challengeRecordRepository.kickMemberOnChallenge(kickMember, kickChallenge);
//
//        System.out.println("result = " + result);
//
//    }
//
//    @Scheduled(cron = "02 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
//    @Transactional
//    public void postingStatusUpdate() {
//        List<Long> postingIdList = postingQueryRepository.findSchedulerUpdatePosting(today);
//        // 벌크성 쿼리 업데이트
//        long updateResult = postingRepository.updatePostingStatus(postingIdList);
//        log.info("updateResult 벌크 연산 result: {} ", updateResult);
//    }
//
//    @Scheduled(cron = "03 00 00 * * *") // 초, 분, 시, 일, 월, 주 순서
//    @Transactional
//    public void challengeStatusUpdate() {
//        List<Challenge> challengeList = challengeRepository.findAllByChallengeStatusTrueAndChallengeProgressLessThan(3L);
//
//        List<Challenge> startList = challengeList
//                .stream()
//                .filter(this::isChallengeTimeToStart)
//                .collect(Collectors.toList());
//        whenChallengeStart(startList);
//
//        List<Challenge> endList = challengeList
//                .stream()
//                .filter(this::isChallengeTimeToEnd)
//                .peek(this::getPointWhenChallengeEnd)
//                .collect(Collectors.toList());
//        whenChallengeEnd(endList);
//    }
//
//    private void whenChallengeStart(List<Challenge> challengeList) {
//        int result = challengeRepository.updateChallengeProgress(2L, challengeList);
//        log.info(today + " / " + result + " Challenge Start");
//    }
//
//    private void whenChallengeEnd(List<Challenge> challengeList) {
//        int result = challengeRepository.updateChallengeProgress(3L, challengeList);
//        challengeRecordRepository.updateChallengePoint(challengeList);
//        log.info(today + " / " + result + " Challenge End");
//    }
//
//    private void getPointWhenChallengeEnd(Challenge challenge) {
//        List<ChallengeRecord> recordList = challengeRecordRepository.findAllByChallenge(challenge);
//        List<Member> memberList = recordList
//                .stream()
//                .map(ChallengeRecord::getMember)
//                .collect(Collectors.toList());
//        int certificatedPostingCount = postingRepository.findAllByChallengeAndMember(challenge, memberList.get(0)).size();
//        final Long getPoint = certificatedPostingCount *
//                50L * (challenge.getCategoryName().equals(CategoryName.OFFICIAL) ? 2L : 1L);
//
//        List<PointHistory> pointHistoryList = recordList
//                .stream()
//                .map(r -> new PointHistory(getPoint, r))
//                .collect(Collectors.toList());
//
//        pointHistoryRepository.saveAll(pointHistoryList);
//        memberRepository.updatePointAll(memberList, getPoint);
//    }
//
//    private boolean isChallengeTimeToStart(Challenge c) {
//        return c.getChallengeProgress() == 1L && c.getChallengeStartDate().isEqual(today);
//    }
//
//    private boolean isChallengeTimeToEnd(Challenge c) {
//        return c.getChallengeProgress() == 2L && c.getChallengeEndDate().isBefore(today);
//    }
//}
