package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.posting.Posting;
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
            "left join p.posting p2 " +
            "left join p.posting.member " +
            "left join p.posting.member.point " +
            "left join p.posting.challenge " +
            "where p.posting.member.email =:email " +
            "and p.status =true ")
    List<MemberHistoryDto> findHistoryPosting(String email);



    @Query("select new com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryDto( " +
            "p.pointHistoryId," +
            "p.createdAt, " +
            "p.challengeRecord.challenge.challengeTitle, " +
            "p.getPoint," +
            "p.challengeRecord.member.memberId," +
            "p.challengeRecord.member.nickname," +
            "p.challengeRecord.member.profileImg, " +
            "p.challengeRecord.member.point.acquiredPoint," +
            "p.challengeRecord.member.point) " +
            "from PointHistory p " +
            "left join p.challengeRecord cr " +
            "left join p.challengeRecord.member " +
            "left join p.challengeRecord.member.point " +
            "left join p.challengeRecord.challenge " +
            "where p.challengeRecord.member.email = :email " +
            "and p.status =true ")
    List<MemberHistoryDto> findHistoryChallenge(String email);

    PointHistory findByPosting(Posting posting);

}
