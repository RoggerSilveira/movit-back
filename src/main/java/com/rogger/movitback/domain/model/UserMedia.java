package com.rogger.movitback.domain.model;

import com.rogger.movitback.domain.enums.WatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="user_medias")
public class UserMedia extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WatchStatus watchStatus;

    @Column(nullable = false)
    private double score = 0.0;

    @Column(nullable = false)
    private boolean favorite = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medias_id", nullable = false)
    private Media media;

}
