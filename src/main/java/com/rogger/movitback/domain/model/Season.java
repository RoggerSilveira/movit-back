package com.rogger.movitback.domain.model;

import com.rogger.movitback.domain.model.enums.SeasonStatus;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="seasons")
public class Season extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medias_id", nullable = false)
    private Media media;

    @Column(nullable = false)
    private String tmdbId;

    private String sinopse;

    @Enumerated(EnumType.STRING)
    private SeasonStatus status;

    @Column(nullable = false)
    private int seasonNumber;

    private String thumbnailUrl;

    private String title;

    @Timestamp
    private String airedAt;
}
