package com.example.onedaypiece.web.domain.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    @Query("select u from UserHistory u inner join u.member " +
            "where u.member.memberId = :memberId " +
            "order by u.modifiedAt desc")
    List<UserHistory> findAllByMemberId(Long memberId);

    @Query("select u from UserHistory u inner join u.member " +
            "where u.checkStatus = false and u.member.memberId = :memberId")
    List<UserHistory> findAllByStatusFalseAndMemberId(Long memberId);

    @Query("select count(u) from UserHistory u inner join u.member " +
            "where u.checkStatus = false and u.member.email = :email")
    Long countAllByStatusFalseAndMemberEmail(String email);
}
