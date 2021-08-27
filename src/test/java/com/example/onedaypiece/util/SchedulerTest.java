//package com.example.onedaypiece.util;
//
//import com.example.onedaypiece.exception.ApiRequestException;
//import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
//import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.domain.point.PointRepository;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
//import com.example.onedaypiece.web.domain.posting.Posting;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
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
//    private  PointRepository pointRepository;
//
//
//    @Test
//    public void test1() {
//
//        LocalDateTime today = LocalDate.now().atStartOfDay();
//
//        List<RemainingMember> challengeRecords =  schedulerQueryRepository.findChallengeMember(today);
//
//        List<Posting> approvalPostingList = challengeRecords.stream()
//                .map(RemainingMember::getPosting)
//                .collect(Collectors.toList());
//
//        int postingApprovalUpdate = postingRepository.updatePostingApproval(approvalPostingList);
//
//
//        List<PointHistory> pointHistoryList = approvalPostingList.stream()
//                .map(posting -> new PointHistory(1L, posting))
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
//
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