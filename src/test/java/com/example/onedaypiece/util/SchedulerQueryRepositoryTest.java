//package com.example.onedaypiece.util;
//
//import com.example.onedaypiece.web.domain.posting.Posting;
//import com.example.onedaypiece.web.domain.posting.PostingQueryRepository;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import com.example.onedaypiece.web.domain.posting.QPosting;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static com.example.onedaypiece.web.domain.posting.QPosting.*;
//
//@SpringBootTest
//class SchedulerQueryRepositoryTest {
//
//    @Autowired
//     JPAQueryFactory queryFactory;
//
//    @Autowired
//    PostingRepository postingRepository;
//
//    @Autowired
//    SchedulerQueryRepository schedulerQueryRepository;
//
//    @Test
//    @Transactional
//
//    public void test(){
//
//        Posting posting = postingRepository.findById(10L).get();
//
//        System.out.println("posting.get(0).getPostingId() = " + posting.getPostingId());
//        System.out.println("posting2.get(0).getPostingId() = " + posting.isPostingStatus());
//
//        Assertions.assertEquals(posting.isPostingStatus(),true);
//
////        schedulerQueryRepository.test(10L);
//
//        Posting posting2 = postingRepository.findById(10L).get();
//        System.out.println("posting2.get(0).getPostingId() = " + posting2.getPostingId());
//        System.out.println("posting2.get(0).getPostingId() = " + posting2.isPostingStatus());
//
//        Assertions.assertEquals(posting2.isPostingStatus(),false);
//
//
//    }
//
//
//
//}