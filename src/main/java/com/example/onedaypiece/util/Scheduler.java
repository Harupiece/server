package com.example.onedaypiece.util;

import com.example.onedaypiece.chat.model.ChatRoom;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.point.PointRepository;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
import com.example.onedaypiece.web.domain.posting.Posting;
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
import static com.example.onedaypiece.web.domain.challenge.Challenge.createChallenge;
import static com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord.createChallengeRecord;
import static com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto.createChallengeRequestDto;

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
    private final PointRepository pointRepository;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final static String SCHEDULE_MODE = System.getProperty( "schedule.mode" );

    LocalDateTime today;

    @Scheduled(cron = "01 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void certificationKick() {

        if ( isNotScheduleMode() ) {
            return;
        }
        initializeToday();

        int week = today.getDayOfWeek().getValue();

        List<ChallengeRecord> challengeMember = schedulerQueryRepository.findAllByChallenge(week);

        //진행중인 챌린지 리스트
        List<Long> challengeId = challengeMember.stream()
                .map(challengeRecord -> challengeRecord.getChallenge().getChallengeId())
                .distinct()
                .collect(Collectors.toList());

        List<SchedulerIdListDto> notWrittenList = schedulerQueryRepository.findNotWrittenList(challengeId);

        List<Long> notWrittenMember = getKickMember(notWrittenList);
        List<Long> notWrittenChallenge = getKickChallenge(notWrittenList);

        int notWrittenChallengeRecordKick = challengeRecordRepository.kickMemberOnChallenge(notWrittenMember, notWrittenChallenge);
        log.info("포스팅 작성하지 않은 인원 update : {} ", notWrittenChallengeRecordKick);

    }


    @Scheduled(cron = "04 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void changePostingApproval() {

        if ( isNotScheduleMode() ) {
            return;
        }

        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<RemainingMember> challengeRecords = schedulerQueryRepository.findChallengeMember(today);
        List<Posting> approvalPostingList = challengeRecords.stream()
                .map(RemainingMember::getPosting)
                .collect(Collectors.toList());

        int postingApprovalUpdate = postingRepository.updatePostingApproval(approvalPostingList);
        log.info("postingApprovalUpdate 벌크 연산 result: {} ", postingApprovalUpdate);

        List<PointHistory> pointHistoryList = approvalPostingList.stream()
                .map(posting -> new PointHistory(1L, posting))
                .collect(Collectors.toList());

        pointHistoryRepository.saveAll(pointHistoryList);

        List<Long> memberList = approvalPostingList.stream()
                .map(posting -> posting.getMember().getMemberId())
                .collect(Collectors.toList());

        int updatePoint = pointRepository.updatePoint(memberList);
        log.info("updatePoint 벌크 연산 result: {} ", updatePoint);
    }

    @Scheduled(cron = "05 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void postingStatusUpdate() {

        if ( isNotScheduleMode() ) {
            return;
        }
        List<Long> postingIdList = schedulerQueryRepository.findSchedulerUpdatePosting(today);
        // 벌크성 쿼리 업데이트
        long updateResult = postingRepository.updatePostingStatus(postingIdList);
        log.info("updateResult 벌크 연산 result: {} ", updateResult);
    }

    @Scheduled(cron = "07 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void challengeStatusUpdate() {

        if ( isNotScheduleMode() ) {
            return;
        }
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

    @Scheduled(cron = "10 0 0 * * *")
    @Transactional
    public void createOfficialChallenge() {

//        if ( isNotScheduleMode() ) {
//            return;
//        }
        Member member = memberRepository.findById(1L).orElseThrow(() -> new NullPointerException("없는 유저입니다."));

        final int CREATE_DELAY = 7;
        final int PROGRESS_PERIOD = 6;

        initializeToday();

        ChallengeRequestDto requestDto = null;
        int dayValue = today.getDayOfWeek().getValue();
        String title = "";
        // 월요일이 1, 일요일이 7
        if (dayValue == 1) {
            title = "매일 산책하기";
            requestDto = createChallengeRequestDto(
                    title,
                    "1. 하루에 한번 자기 신체 부위가 포함된 바깥 사진을 찍어서 올려주세요! 공원을 걸으며 찍은 사진이면 더 좋겠죠? \n" +
                            "2. 사진은 언제 찍어도 괜찮지만 인증시간은 밤 9시에서 10시 사이에 해주세요.  단, 형체를 알아보기 힘들 정도로 흔들린 사진은 안 돼요❗❗  \n" +
                            "3. 인증샷을 올리고 다른 분들의 인증 게시물도 구경하면서 인증버튼을 눌러주세요\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(CREATE_DELAY),
                    today.plusDays(CREATE_DELAY + PROGRESS_PERIOD),
                    "https://i.ibb.co/nD0s4F4/banner-01.png",
                    "https://i.ibb.co/Bg9T9JZ/image.jpg",
                    "https://i.ibb.co/Y0GL0TP/badf.jpg",
                    ""
            );
        } else if (dayValue == 3) {
            title = "작심육일 금연!";
            requestDto = createChallengeRequestDto(
                    title,
                    "1. 하루에 한번 담배 대신 자기가 한 일에 대한 사진을 올려주세요! 사탕이나 초콜릿 , 젤리처럼 간단하면서도 달콤한 간식으로 담배를 이겨낸 걸 뿌듯한 마음으로 인증하는 것도 가능하겠죠? \n" +
                            "2. 사진은 언제 찍어도 괜찮지만 인증시간은 밤 9시에서 10시 사이에 해주세요.  단, 담배를 피는 사진은 당연히 안 돼요! \n" +
                            "3. 인증샷을 올리고 다른 분들의 인증 게시물도 구경하면서 인증버튼을 꼭 눌러주세요\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(CREATE_DELAY),
                    today.plusDays(CREATE_DELAY + PROGRESS_PERIOD),
                    "https://i.ibb.co/HhKkDD5/Kakao-Talk-20210826-121609453-min.jpg",
                    "https://i.ibb.co/j8rmx81/bad.jpg",
                    "https://i.ibb.co/NpDZLYL/good.jpg",
                    ""
            );
        } else if (dayValue == 5) {
            title = "매일 영양제 챙겨먹기";
            requestDto = createChallengeRequestDto(
                    title,
                    "1. 매일 매일 영양도 쑥쑥! 조각도 쑥쑥! 영양제를 먹기 직전에 찍어주시면 정확한 인증샷이 될 수 있겠죠?\n" +
                            "2. 사진은 언제 찍어도 괜찮지만 인증시간은 밤 9시에서 10시 사이에 해주세요.  단, 영양제 같이 생긴 젤리나 사탕은 안 돼요!\n" +
                            "3. 인증샷을 올리고 다른 분들의 인증 게시물도 구경하면서 인증버튼을 꼭 눌러주세요\uD83D\uDE04",
                    "",
                    OFFICIAL,
                    today.plusDays(CREATE_DELAY),
                    today.plusDays(CREATE_DELAY + PROGRESS_PERIOD),
                    "https://i.ibb.co/4fKQ9SM/Kakao-Talk-20210826-121602210-min.jpg",
                    "https://i.ibb.co/88pwY4r/image.jpg",
                    "https://i.ibb.co/y8p1LJx/bad.jpg",
                    ""
            );
        }

        if (requestDto != null && schedulerQueryRepository.findAllByOfficialAndChallengeTitle(title)) {
            Challenge challenge = createChallenge(requestDto, member);
            ChallengeRecord challengeRecord = createChallengeRecord(challenge, member);
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
            Long resultPoint = postingCount * 50L * (challenge.getCategoryName().equals(OFFICIAL) ? 2L : 1L);

            if (resultPoint != 0L) {
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

    public void initializeToday() {

        today = LocalDate.now().atStartOfDay();
    }

    private boolean isNotScheduleMode() {

        if ( null != SCHEDULE_MODE && SCHEDULE_MODE.equals( "on" ) ) {
            return false;
        }
        return true;

    }
}