//package com.example.onedaypiece.util;
//
//import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
//import com.example.onedaypiece.web.domain.point.PointRepository;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
//import com.example.onedaypiece.web.domain.posting.Posting;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import com.example.onedaypiece.web.dto.query.posting.SchedulerIdListDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//@SpringBootTest
//@Transactional
//public class SchedulerTest {
//
//    @Autowired
//    private PostingRepository postingRepository;
//    @Autowired
//    private  PointHistoryRepository pointHistoryRepository;
//    @Autowired
//    private  SchedulerQueryRepository schedulerQueryRepository;
//
//    @Autowired
//    private ChallengeRecordRepository challengeRecordRepository;
//
//    @Autowired
//    private  PointRepository pointRepository;
//
//
//    @Test
//    public void test1() {
//
//        LocalDateTime today = LocalDate.now().atStartOfDay();
//
//
//
//        List<Posting> approvalPostingList = schedulerQueryRepository.findChallengeMember();
//
//
//        int postingApprovalUpdate = postingRepository.updatePostingApproval(approvalPostingList);
//
//
//        List<PointHistory> pointHistoryList = approvalPostingList.stream()
//                .map(posting -> PointHistory.createPostingPointHistory(1L,posting))
//                .collect(Collectors.toList());
//
//        pointHistoryRepository.saveAll(pointHistoryList);
//
//        List<Long> memberList = approvalPostingList.stream()
//                .map(posting -> posting.getMember().getMemberId())
//                .collect(Collectors.toList());
//
//        int result = pointRepository.updatePoint(memberList);
//
//    }
//
//
//
//
//    @Test
//    public void test(){
//        LocalDateTime today = LocalDate.now().atStartOfDay();
//
//        int week = today.getDayOfWeek().getValue();
//
//        System.out.println("week = " + week);
////        week = 7;
//
//        // 주말 여부를 체크해서 챌린지 레코드를 가져옴.
//        List<Long> challengeId = schedulerQueryRepository.findAllByChallenge(week);
//
//        // 작성하지 않은 인원 가져옴
//        List<SchedulerIdListDto> notWrittenList = schedulerQueryRepository.findNotWrittenList(challengeId);
//
//        List<Long> notWrittenMember = getKickMember(notWrittenList);
//        List<Long> notWrittenChallenge = getKickChallenge(notWrittenList);
//
//        // 벌크쿼리로 challengeRecordStatus false로 변경.
//        int notWrittenChallengeRecordKick = challengeRecordRepository.kickMemberOnChallenge(notWrittenMember, notWrittenChallenge);
//
//    }
//
//    private List<Long> getKickChallenge(List<SchedulerIdListDto> postingList) {
//        return postingList.stream()
//                .map(SchedulerIdListDto::getChallengeId).distinct()
//                .collect(Collectors.toList());
//    }
//
//    private List<Long> getKickMember(List<SchedulerIdListDto> postingList) {
//        return postingList.stream()
//                .map(SchedulerIdListDto::getMemberId).distinct()
//                .collect(Collectors.toList());
//    }
//
//    /**벌크 연산 적용 전
//     *
//
//     @Test
//     public void update(){
//     //given
//     LocalDateTime today = LocalDate.now().atStartOfDay();
//     List<Posting> postingList2 = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkTrue(today);
//     //when
//     for (Posting p : postingList2) {
//     if (p.getCreatedAt().isBefore(today)) {
//     p.updateStatus();
//     }
//     }
//     List<Posting> postingResultList = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkFalse();
//     //then
//     assertEquals(3,postingResultList.size());
//
//
//     }
//     *
//     */
//
////    @Test
////    public void bulkUpdate(){
////        //given
////        LocalDateTime today = LocalDate.now().atStartOfDay();
////        List<Posting> postingList2 = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkTrue(today);
////
////        //when
////        int result = postingRepository.updatePostingStatus(postingList2);
////        List<Posting> postingResultList = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkFalse();
////
////        //then
////        assertEquals(3,result);
////        assertEquals(3,postingResultList.size());
////
////    }
//
//
//
//}