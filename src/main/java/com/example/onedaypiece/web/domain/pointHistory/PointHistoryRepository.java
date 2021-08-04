package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

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



    @Query("select new com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryDto( " +
            "p.pointHistoryId," +
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

    List<MemberHistoryDto> findHistory(String email);
}
