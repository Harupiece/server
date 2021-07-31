package com.example.onedaypiece.web.domain.pointhistory;

import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("select p from PointHistory p join fetch  p.certification where p.certification.member = :member")
    List<PointHistory> find(Member member);

}
