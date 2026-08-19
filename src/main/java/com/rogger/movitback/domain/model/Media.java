package com.rogger.movitback.domain.model;

import com.rogger.movitback.domain.model.enums.MediaStatus;
import com.rogger.movitback.domain.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medias")
@Getter
@Setter
@NoArgsConstructor
public class Media extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    @Column(nullable = false)
    private String title;

    private String originalTitle;

    @Column(nullable = false)
    private int tmdbId;

    @Column(nullable = false)
    private String imdbId;

    private String thumbnailUrl;

    private String sinopse;

    private int totalSeasons;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaStatus mediaStatus;

    private int totalEpisodes;

    private int rank;

    private int totalFavorites;

    @Column(nullable = false)
    private int totalWatched;

    private Float duration;

    @Column(nullable = false)
    private double score = 0;

    @Column(nullable = false)
    private int scoredBy = 0;

    private String[] alternativeTitleList;

}
