package com.example.onedaypiece.web.domain.pointHistory;

import com.example.onedaypiece.web.domain.posting.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    PointHistory findByPosting(Posting posting);
}
