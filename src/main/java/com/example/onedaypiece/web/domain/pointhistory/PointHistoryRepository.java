package com.example.onedaypiece.web.domain.pointhistory;

import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("select p from PointHistory p Where p.status = true and p.certification.member = :member")
    List<PointHistory> findAllByMember(Member member);

}
