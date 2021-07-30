package com.example.onedaypiece.web.domain.Ip;

import com.example.onedaypiece.web.domain.common.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Ip extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String ipAddress;

    public Ip(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
