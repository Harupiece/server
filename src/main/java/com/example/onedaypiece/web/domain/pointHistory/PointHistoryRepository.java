package com.example.onedaypiece.web.domain.pointHistory;

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
            "where p.posting.member.email =:email")
    List<MemberHistoryDto> findHistory(String email);
}
