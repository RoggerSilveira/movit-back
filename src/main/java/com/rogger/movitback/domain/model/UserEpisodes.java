package com.rogger.movitback.domain.model;

import com.rogger.movitback.domain.model.enums.WatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="user_episodes")
public class UserEpisodes extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private WatchStatus watchStatus;

    @Column(nullable = false)
    private double score = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episodes_id", nullable = false)
    private Episode episode;
}
