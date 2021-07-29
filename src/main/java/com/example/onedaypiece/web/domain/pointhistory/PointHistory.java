package com.example.onedaypiece.web.domain.pointhistory;


import com.example.onedaypiece.web.domain.certification.Certification;
import com.example.onedaypiece.web.domain.common.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PointHistory extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pointhistoryId;

    @Column
    private Long getPoint;

    @OneToOne
    @JoinColumn(name="certification_id")
    private Certification certification;

    // 상태 아직안쓴거면 true / 사용했으면 false
    @Column
    private boolean status;

    public PointHistory(Long getPoint, Certification certification){
        this.getPoint = getPoint;
        this.certification = certification;
        this.status = true;
    }


}
