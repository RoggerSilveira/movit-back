package com.rogger.movitback.domain.model;

import jakarta.persistence.Column;

public class Comment extends BaseEntity {

    @Column(nullable = false)
    private String comment;

}
