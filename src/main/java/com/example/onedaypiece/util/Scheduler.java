package com.example.onedaypiece.util;

import com.example.onedaypiece.chat.model.ChatRoom;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
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
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.domain.challenge.CategoryName.OFFICIAL;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostingRepository postingRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;
    private final SchedulerQueryRepository schedulerQueryRepository;
    private final ChatRoomRepository chatRoomRepository;

    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    private final LocalDateTime today = LocalDate.now().atStartOfDay();

    //    01 00 00
    @Scheduled(cron = "01 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void certificationKick() {

        List<ChallengeRecord> challengeMember = schedulerQueryRepository.findAllByChallenge();

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
        List<SchedulerIdListDto> UncertifiedList = schedulerQueryRepository.findUncertifiedList(challengeId, memberId, today);

        // 인증글 작성하지 않은 사람.
        List<SchedulerIdListDto> NotWrittenList = schedulerQueryRepository.findNotWrittenList(challengeId);

        List<Long> UncertifiedMember = getKickMember(UncertifiedList);
        List<Long> NotWrittenMember = getKickMember(NotWrittenList);
        List<Long> UncertifiedChallenge = getKickChallenge(UncertifiedList);
        List<Long> NotWrittenChallenge = getKickChallenge(NotWrittenList);

        UncertifiedMember.addAll(NotWrittenMember);
        UncertifiedChallenge.addAll(NotWrittenChallenge);

        int updateResult = challengeRecordRepository.kickMemberOnChallenge(UncertifiedMember, UncertifiedChallenge);
        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "02 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        List<Long> postingIdList = schedulerQueryRepository.findSchedulerUpdatePosting(today);
        // 벌크성 쿼리 업데이트
        long updateResult = postingRepository.updatePostingStatus(postingIdList);
        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "03 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void challengeStatusUpdate() {
        List<Challenge> challengeList = challengeRepository.findAllByChallengeStatusTrueAndChallengeProgressLessThan(3L);

        List<Challenge> startList = challengeList
                .stream()
                .filter(this::isChallengeTimeToStart)
                .collect(Collectors.toList());

        List<Challenge> endList = challengeList
                .stream()
                .filter(this::isChallengeTimeToEnd)
                .collect(Collectors.toList());

        // 챌린지 시작
        challengeStart(startList);

        // 챌린지 종료
        challengeEnd(endList);

        // 챌린지 완주 포인트 지급
        challengeEndPoint(endList);
    }

    @Scheduled(cron = "04 0 0 * * *")
    public void createOfficialChallenge() {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new NullPointerException("없는 유저입니다."));

        ChallengeRequestDto requestDto = null;
        int dayValue = today.getDayOfWeek().getValue();
        // 월요일이 1, 일요일이 7
        if (dayValue == 1) {
            requestDto = new ChallengeRequestDto(
                    "매일 물 2L 마시기",
                    "매일 물 2L 마시기\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(7),
                    today.plusDays(7 + 6),
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    ""
            );
        } else if (dayValue == 3) {
            requestDto = new ChallengeRequestDto(
                    "매일 산책하기",
                    "매일 산책하기",
                    "",
                    OFFICIAL,
                    today.plusDays(7),
                    today.plusDays(7 + 6),
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    ""
            );
        } else if (dayValue == 5) {
            requestDto = new ChallengeRequestDto(
                    "매일 물 2L 마시기",
                    "매일 물 2L 마시기",
                    "",
                    OFFICIAL,
                    today.plusDays(7),
                    today.plusDays(7 + 6),
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/02/12/16/45/cat-1196374_960_720.jpg",
                    ""
            );
        }
        if (requestDto != null) {
            Challenge challenge = new Challenge(requestDto, member);
            ChallengeRecord challengeRecord = new ChallengeRecord(challenge, member);
            challengeRecordRepository.save(challengeRecord);
            Long challengeId = challengeRepository.save(challenge).getChallengeId();

            ChatRoom chatRoom = new ChatRoom(challengeId);
            chatRoomRepository.save(chatRoom);
            hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        }
    }

    private void challengeStart(List<Challenge> startList) {
        Long result1 = challengeQueryRepository.updateChallengeProgress(2L, startList);
        log.info(today + " / " + result1 + " Challenge Start");
    }

    private void challengeEnd(List<Challenge> endList) {
        Long result2 = challengeQueryRepository.updateChallengeProgress(3L, endList);
        challengeRecordRepository.updateChallengePoint(endList);
        log.info(today + " / " + result2 + " Challenge End");
    }

    private void challengeEndPoint(List<Challenge> endList) {
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

    private List<Long> getKickChallenge(List<SchedulerIdListDto> postingList) {
        return postingList.stream()
                .map(SchedulerIdListDto::getChallengeId).distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getKickMember(List<SchedulerIdListDto> postingList) {
        return postingList.stream()
                .map(SchedulerIdListDto::getMemberId).distinct()
                .collect(Collectors.toList());
    }
}
