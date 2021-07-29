package com.example.onedaypiece.web.domain.point;


import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
