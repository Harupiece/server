package com.example.onedaypiece.web.domain.point;


import com.example.onedaypiece.web.domain.common.Timestamped;
import lombok.Getter;
import lombok.Setter;

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


    public Point(){
        this.acquiredPoint = 0L;
    }

}
