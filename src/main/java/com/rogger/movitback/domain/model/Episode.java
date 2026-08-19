package com.rogger.movitback.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "episodes")
@Getter
@Setter
@NoArgsConstructor
public class Episode extends BaseEntity {
    @Column(nullable = false)
    private String title;

    private String sinopse;

    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seasons_id", nullable = false)
    private Season season;

    @Column(nullable = false)
    private int episodeNumber;

    @Column(nullable = false)
    private int seasonNumber;

    @Column(nullable = false)
    private float duration = 0;

    @Column(nullable = false)
    private double score = 0.0;

    private int scoredBy;

    private int tmdbId;
}
