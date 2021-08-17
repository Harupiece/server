package com.example.onedaypiece.util;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeQueryRepository;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberQueryRepository;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
import com.example.onedaypiece.web.domain.posting.PostingQueryRepository;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.domain.challenge.CategoryName.OFFICIAL;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostingQueryRepository postingQueryRepository;
    private final PostingRepository postingRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberQueryRepository memberQueryRepository;

    private final LocalDateTime today = LocalDate.now().atStartOfDay();

    //    01 00 00
    @Scheduled(cron = "01 00 * * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void certificationKick() {
        List<ChallengeRecord> challengeMember = challengeRecordQueryRepository.findAllByChallenge();

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
        List<SchedulerIdListDto> postingList = postingQueryRepository.findPostingListTest(challengeId, memberId, today);


        // 인증글 작성하지 않은 사람.
        List<ChallengeRecord> postingList2 = challengeRecordRepository.findPostingListTest2(challengeId, today);


        List<Long> kickMember = postingList.stream()
                .map(SchedulerIdListDto::getMemberId).distinct()
                .collect(Collectors.toList());

        List<Long> kickMember2 = postingList2.stream()
                .map(posting -> posting.getMember().getMemberId()).distinct()
                .collect(Collectors.toList());

        List<Long> kickChallenge = postingList.stream()
                .map(SchedulerIdListDto::getChallengeId).distinct()
                .collect(Collectors.toList());

        List<Long> kickChallenge2 = postingList2.stream()
                .map(posting -> posting.getChallenge().getChallengeId()).distinct()
                .collect(Collectors.toList());

        kickMember.addAll(kickMember2);
        kickChallenge.addAll(kickChallenge2);

        int updateResult = challengeRecordRepository.kickMemberOnChallenge(kickMember,kickChallenge);
        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "02 00 * * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        List<Long> postingIdList = postingQueryRepository.findSchedulerUpdatePosting(today);
        // 벌크성 쿼리 업데이트
        long updateResult = postingRepository.updatePostingStatus(postingIdList);
        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "03 00 * * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void challengeStatusUpdate() {
        List<Challenge> challengeList = challengeRepository.findAllByChallengeStatusTrueAndChallengeProgressLessThan(3L);

        // 챌린지 시작
        List<Challenge> startList = challengeList
                .stream()
                .filter(this::isChallengeTimeToStart)
                .collect(Collectors.toList());
        Long result1 = challengeQueryRepository.updateChallengeProgress(2L, startList);
        log.info(today + " / " + result1 + " Challenge Start");

        // 챌린지 종료
        List<Challenge> endList = challengeList
                .stream()
                .filter(this::isChallengeTimeToEnd)
                .collect(Collectors.toList());
        Long result2 = challengeQueryRepository.updateChallengeProgress(3L, endList);
        challengeRecordRepository.updateChallengePoint(endList);
        log.info(today + " / " + result2 + " Challenge End");

        // 챌린지 완주 포인트 지급
        long result3 = endList
                .stream()
                .peek(c -> System.out.println("filteredChallenge : " + c.getChallengeId()))
                .peek(this::getPointWhenChallengeEnd)
                .count();
        log.info(today + " / " + result3 + " members get points");
    }

    private void getPointWhenChallengeEnd(Challenge challenge) {
        List<ChallengeRecord> recordList = challengeRecordQueryRepository.findAllByChallengeOnScheduler(challenge);

        List<Member> memberList = recordList
                .stream()
                .map(ChallengeRecord::getMember)
                .collect(Collectors.toList());

        long postingCount = postingRepository.findAllByChallengeAndMember(challenge, memberList.get(0)).size();
        Long resultPoint = postingCount * 500L * (challenge.getCategoryName().equals(OFFICIAL) ? 2L : 1L);

        List<PointHistory> pointHistoryList = recordList
                .stream()
                .map(r -> new PointHistory(resultPoint, r))
                .collect(Collectors.toList());
        pointHistoryRepository.saveAll(pointHistoryList);

        List<Point> pointList = memberList
                .stream()
                .map(Member::getPoint)
                .collect(Collectors.toList());
        memberQueryRepository.updatePointAll(pointList, resultPoint);
    }

    private boolean isChallengeTimeToStart(Challenge c) {
        return c.getChallengeProgress() == 1L && c.getChallengeStartDate().isEqual(today);
    }

    private boolean isChallengeTimeToEnd(Challenge c) {
        return c.getChallengeProgress() == 2L && c.getChallengeEndDate().isBefore(today);
    }
}