//package com.example.onedaypiece.util;
//
//import com.example.onedaypiece.web.domain.posting.Posting;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//@SpringBootTest
//@Transactional
//public class SchedulerTest {
//
//    @Autowired
//    private PostingRepository postingRepository;
//
//    /**벌크 연산 적용 전
//     *
//
//    @Test
//    public void update(){
//        //given
//        LocalDateTime today = LocalDate.now().atStartOfDay();
//        List<Posting> postingList2 = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkTrue(today);
//        //when
//        for (Posting p : postingList2) {
//            if (p.getCreatedAt().isBefore(today)) {
//                p.updateStatus();
//            }
//        }
//        List<Posting> postingResultList = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkFalse();
//        //then
//        assertEquals(3,postingResultList.size());
//
//
//    }
//     *
//     */
//
//    @Test
//    public void bulkUpdate(){
//        //given
//        LocalDateTime today = LocalDate.now().atStartOfDay();
//        List<Posting> postingList2 = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkTrue(today);
//
//        //when
//        int result = postingRepository.updatePostingStatus(postingList2);
//        List<Posting> postingResultList = postingRepository.findAllByPostingStatusTrueAndPostingModifyOkFalse();
//
//        //then
//        assertEquals(3,result);
//        assertEquals(3,postingResultList.size());
//
//    }
//
//}