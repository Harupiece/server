package com.example.onedaypiece.web.domain.challenge;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Challenge {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
