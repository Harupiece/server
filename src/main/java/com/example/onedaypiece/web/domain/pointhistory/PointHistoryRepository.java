package com.example.onedaypiece.web.domain.pointhistory;

import com.example.onedaypiece.service.PostingTestDto;
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
    @Query("select p, " +
            "p.certification.member, " +
            "p.certification.member.point, " +
            "p.certification.posting.challenge, " +
            "p.certification.posting " +
            "from PointHistory p " +
            "join fetch p.certification " +
            "where p.certification.member.email = :email")
    List<PointHistory> find(String email);

    //4 차
    @Query("select new com.example.onedaypiece.service.PostingTestDto( " +
            "p.pointhistoryId," +
            "p.createdAt, " +
            "p.certification.posting.challenge.challengeTitle, " +
            "p.getPoint," +
            "p.certification.member.memberId," +
            "p.certification.member.nickname," +
            "p.certification.member.profileImg, " +
            "p.certification.member.point.acquiredPoint," +
            "p.certification.member.point) " +
            "from PointHistory p " +
            "where p.certification.member.email =:email")
//            "where p.postingId = 5")
//            "join p.member")
    List<PostingTestDto> findtest(String email);
}
