package com.example.onedaypiece.web.domain.history;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class UserHistory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long HistoryId;

    @Column
    private String content;

    // 유저가 히스토리를 확인했는지 여부
    @Column
    private boolean checkStatus;
}
