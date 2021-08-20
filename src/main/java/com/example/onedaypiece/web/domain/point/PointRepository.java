package com.example.onedaypiece.web.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findAllByOrderByAcquiredPointDesc();
}
