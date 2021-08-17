package com.example.onedaypiece.web.domain.member;

import com.example.onedaypiece.web.domain.point.Point;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.domain.point.QPoint.point;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * @Modifying(clearAutomatically = true)
     * @Query("update Point p set p.acquiredPoint = p.acquiredPoint + :getPoint where p in :pointList")
     * void updatePointAll(List<Point> pointList, Long getPoint);
     */
    @Modifying
    public void updatePointAll(List<Point> pointList, Long getPoint) {
        queryFactory
                .update(point)
                .set(point.acquiredPoint, point.acquiredPoint.add(getPoint))
                .where(point.in(pointList))
                .execute();
    }
}
