package com.example.onedaypiece.web.domain.pointhistory;

import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

//    @Query("select p " +
//            "from PointHistory p " +
//            "join fetch p.certification " +
//            "where p.certification.member = :member")
//    List<PointHistory> find(Member member);

    // 1차
//    @Query("select p,p.certification.member.point " +
//            "from PointHistory p " +
//            "join fetch p.certification " +
//            "where p.certification.m in :certifications")
//    List<PointHistory> find(List<Certification> certifications);

    //2 차
//        @Query("select p,p.certification.member.point,p.certification.posting.challenge,  p.certification.posting " +
//            "from PointHistory p " +
//            "join fetch p.certification " +
//            "where p.certification.member = :member")
//    List<PointHistory> find(Member member);
    //3 차
    @Query("select p,p.certification.member, p.certification.member.point,p.certification.posting.challenge,  p.certification.posting " +
            "from PointHistory p " +
            "join fetch p.certification " +
            "where p.certification.member.email = :email")
    List<PointHistory> find(String email);

}
