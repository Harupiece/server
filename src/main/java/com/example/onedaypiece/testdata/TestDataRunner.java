package com.example.onedaypiece.testdata;


import com.example.onedaypiece.service.MemberService;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.point.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PointRepository pointRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member testMember1 = new Member("test1@naver.com", passwordEncoder.encode("1234"),"닉네임1","프로필1" );
        memberRepository.save(testMember1);

        Point pointTest1 = new Point(testMember1);
        pointTest1.setAcquiredPoint(20L);
        pointRepository.save(pointTest1);

        Point pointTest2 = new Point(testMember1);
        pointTest2.setAcquiredPoint(30L);
        pointRepository.save(pointTest2);
    }
}
