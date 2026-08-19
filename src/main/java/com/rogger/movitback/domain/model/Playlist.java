package com.rogger.movitback.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="playlists")
public class Playlist extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String imageUrl;

    @Column(nullable = false)
    private boolean isPrivate = true;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Playlist parent;
}