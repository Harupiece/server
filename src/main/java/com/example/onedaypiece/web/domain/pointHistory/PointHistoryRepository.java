package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("select new com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryDto( " +
            "p.pointHistoryId," +
            "p.createdAt, " +
            "p.posting.challenge.challengeTitle, " +
            "p.getPoint," +
            "p.posting.member.memberId," +
            "p.posting.member.nickname," +
            "p.posting.member.profileImg, " +
            "p.posting.member.point.acquiredPoint," +
            "p.posting.member.point) " +
            "from PointHistory p " +
            "join p.challengeRecord " +
            "where p.posting.member.email =:email")
    List<MemberHistoryDto> findHistory(String email);


    @Query("select ph " +
            "from PointHistory ph left join ph.challengeRecord cr " +
            "left join ph.posting p " +
            "left join Member m on m.memberId = p.member.memberId " +
            "left join Member m2 on m2.memberId = cr.member.memberId " +
            "where  p.member =:member or cr.member =:member")
    List<PointHistory> findHistory2(Member member);



}
