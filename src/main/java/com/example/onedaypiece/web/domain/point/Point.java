package com.example.onedaypiece.web.domain.point;

import com.example.onedaypiece.web.domain.common.Timestamped;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Point extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pointId;

    @Column
    private Long acquiredPoint;

    @Builder
    public Point(Long pointId, Long acquiredPoint) {
        this.pointId = pointId;
        this.acquiredPoint = acquiredPoint;
    }



    public Point(){
        this.acquiredPoint = 0L;
    }

}
