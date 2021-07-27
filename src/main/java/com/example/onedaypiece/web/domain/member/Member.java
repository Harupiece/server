package com.example.onedaypiece.web.domain.member;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
