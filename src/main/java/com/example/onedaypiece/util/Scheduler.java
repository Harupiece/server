package com.example.onedaypiece.util;

import com.example.onedaypiece.chat.model.ChatRoom;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
import com.example.onedaypiece.web.domain.posting.PostingRepository;
import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private final ChallengeRepository challengeRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final SchedulerQueryRepository schedulerQueryRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    private final LocalDateTime today = LocalDate.now().atStartOfDay();

    @Scheduled(cron = "0/30 * * * * *")
    public void timeCheck() {
        log.info(String.valueOf(LocalDateTime.now()));
        log.info(String.valueOf(LocalDateTime.now()));
        log.info(String.valueOf(LocalDateTime.now()));
        log.info(String.valueOf(LocalDateTime.now()));
        log.info(String.valueOf(LocalDateTime.now()));
    }

    //    01 00 00
    @Scheduled(cron = "10 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void certificationKick() {

        int week = today.getDayOfWeek().getValue();

        List<ChallengeRecord> challengeMember = schedulerQueryRepository.findAllByChallenge(week);

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

    @Scheduled(cron = "11 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {
        List<Long> postingIdList = schedulerQueryRepository.findSchedulerUpdatePosting(today);
        // 벌크성 쿼리 업데이트
        long updateResult = postingRepository.updatePostingStatus(postingIdList);
        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "12 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void challengeStatusUpdate() {
        List<ChallengeRecord> recordList = schedulerQueryRepository.findAllByChallengeProgressLessThan(3L);

        List<Challenge> challengeList = recordList
                .stream()
                .map(ChallengeRecord::getChallenge)
                .distinct()
                .collect(Collectors.toList());

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

    @Scheduled(cron = "13 0 0 * * *")
    @Transactional
    public void createOfficialChallenge() {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new NullPointerException("없는 유저입니다."));

        final int CREATE_DELAY = 7;
        final int PROGRESS_PERIOD = 6;

        ChallengeRequestDto requestDto = null;
        int dayValue = today.getDayOfWeek().getValue();
        String title = "";
        // 월요일이 1, 일요일이 7
        if (dayValue == 1) {
            title = "매일 산책하기";
            requestDto = new ChallengeRequestDto(
                    title,
                    "1. 하루에 한번 자기 신체 부위가 포함된 바깥 사진을 찍어서 올려주세요! 공원을 걸으며 찍은 사진이면 더 좋겠죠? \n" +
                            "2. 사진은 언제 찍어도 괜찮지만 인증시간은 밤 9시에서 10시 사이에 해주세요.  단, 형체를 알아보기 힘들 정도로 흔들린 사진은 안 돼요❗❗  \n" +
                            "3. 인증샷을 올리고 다른 분들의 인증 게시물도 구경하면서 인증버튼을 눌러주세요\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(CREATE_DELAY),
                    today.plusDays(CREATE_DELAY + PROGRESS_PERIOD),
                    "https://i.ibb.co/YQCrYJR/banner-01.png",
                    "https://i.ibb.co/Bg9T9JZ/image.jpg",
                    "https://i.ibb.co/Y0GL0TP/badf.jpg",
                    ""
            );
        } else if (dayValue == 3) {
            title = "작심육일 금연!";
            requestDto = new ChallengeRequestDto(
                    title,
                    "1. 하루에 한번 담배 대신 자기가 한 일에 대한 사진을 올려주세요! 사탕이나 초콜릿 , 젤리처럼 간단하면서도 달콤한 간식으로 담배를 이겨낸 걸 뿌듯한 마음으로 인증하는 것도 가능하겠죠? \n" +
                            "2. 사진은 언제 찍어도 괜찮지만 인증시간은 밤 9시에서 10시 사이에 해주세요.  단, 담배를 피는 사진은 당연히 안 돼요! \n" +
                            "3. 인증샷을 올리고 다른 분들의 인증 게시물도 구경하면서 인증버튼을 꼭 눌러주세요\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(CREATE_DELAY),
                    today.plusDays(CREATE_DELAY + PROGRESS_PERIOD),
                    "https://i.ibb.co/4WDR3nd/Kakao-Talk-20210821-171834310.jpg",
                    "https://i.ibb.co/j8rmx81/bad.jpg",
                    "https://i.ibb.co/NpDZLYL/good.jpg",
                    ""
            );
        } else if (dayValue == 6) {
            title = "매일 영양제 챙겨먹기";
            requestDto = new ChallengeRequestDto(
                    title,
                    "1. 매일 매일 영양도 쑥쑥! 조각도 쑥쑥! 영양제를 먹기 직전에 찍어주시면 정확한 인증샷이 될 수 있겠죠?\n" +
                            "2. 사진은 언제 찍어도 괜찮지만 인증시간은 밤 9시에서 10시 사이에 해주세요.  단, 영양제 같이 생긴 젤리나 사탕은 안 돼요!\n" +
                            "3. 인증샷을 올리고 다른 분들의 인증 게시물도 구경하면서 인증버튼을 꼭 눌러주세요\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(CREATE_DELAY),
                    today.plusDays(CREATE_DELAY + PROGRESS_PERIOD),
                    "https://i.ibb.co/5j6TNb1/Kakao-Talk-20210821-171849115.jpg",
                    "https://i.ibb.co/88pwY4r/image.jpg",
                    "https://i.ibb.co/y8p1LJx/bad.jpg",
                    ""
            );
        }

        if (requestDto != null && schedulerQueryRepository.findAllByOfficialAndChallengeTitle(title)) {
            Challenge challenge = new Challenge(requestDto, member);
            ChallengeRecord challengeRecord = new ChallengeRecord(challenge, member);
            challengeRecordRepository.save(challengeRecord);
            Long challengeId = challengeRepository.save(challenge).getChallengeId();

            ChatRoom chatRoom = new ChatRoom(challengeId);
            chatRoomRepository.save(chatRoom);
            hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);

            log.info(challengeId + " OFFICIAL challenge start / day value = " + dayValue);

            challengeRecordRepository.deleteByMember(member);
        }
    }

    private void challengeStart(List<Challenge> startList) {
        Long result1 = schedulerQueryRepository.updateChallengeProgress(2L, startList);
        log.info(today + " / " + result1 + " Challenge Start");
    }

    private void challengeEnd(List<Challenge> endList) {
        Long result2 = schedulerQueryRepository.updateChallengeProgress(3L, endList);
        Long result3 = schedulerQueryRepository.updateChallengePoint(endList);
        log.info(today + " / " + result2 + " Challenge End, " + result3 + "challengePoint update");
    }

    private void challengeEndPoint(List<Challenge> endList) {
        long result3 = endList
                .stream()
                .peek(this::getPointWhenChallengeEnd)
                .count();
        log.info(today + " / " + result3 + " members get points");
    }

    private void getPointWhenChallengeEnd(Challenge challenge) {
        List<ChallengeRecord> recordList = schedulerQueryRepository.findAllByChallengeOnScheduler(challenge);

        List<Member> memberList = recordList
                .stream()
                .map(ChallengeRecord::getMember)
                .collect(Collectors.toList());

        if (memberList.size() > 0) {
            Long postingCount = schedulerQueryRepository.findAllByChallengeAndFirstMember(challenge, memberList.get(0));
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
            schedulerQueryRepository.updatePointAll(pointList, resultPoint);
        }
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