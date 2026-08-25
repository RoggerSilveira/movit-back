-- ============================================================
-- Movit - V1__create_tables.sql
-- Flyway migration inicial
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto"; -- necessário para gen_random_uuid()

-- ============================================================
-- USERS
-- ============================================================
CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL UNIQUE,
    name          VARCHAR(255) NOT NULL,
    password      VARCHAR(255),
    role          VARCHAR(20) NOT NULL DEFAULT 'USER'
                  CHECK (role IN ('USER', 'ADMIN')),
    enabled       BOOLEAN NOT NULL DEFAULT TRUE,
    disabled_at   TIMESTAMP,
    provider      VARCHAR(50),
    created_at    TIMESTAMP NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- CATEGORIES
-- ============================================================
CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- TAGS
-- ============================================================
CREATE TABLE tags (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- MEDIAS
-- ============================================================
CREATE TABLE medias (
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type                   VARCHAR(20) NOT NULL
                           CHECK (type IN ('MOVIE', 'SERIE')),
    title                  VARCHAR(255) NOT NULL,
    original_title         VARCHAR(255),
    tmdb_id                INT NOT NULL,
    imdb_id                VARCHAR(50) NOT NULL,
    thumbnail_url          TEXT,
    sinopse                TEXT,
    total_seasons          INT,
    media_status           VARCHAR(20) NOT NULL
                           CHECK (media_status IN ('ONGOING', 'HIATUS', 'COMPLETED', 'CANCELLED', 'UNRELEASED')),
    total_episodes         INT,
    rank                   INT,
    total_favorites        INT,
    total_watched          INT NOT NULL DEFAULT 0,
    duration               REAL,
    score                  DOUBLE PRECISION NOT NULL DEFAULT 0,
    scored_by              INT NOT NULL DEFAULT 0,
    alternative_title_list TEXT[],
    created_at             TIMESTAMP NOT NULL DEFAULT now(),
    updated_at             TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- SEASONS
-- ============================================================
CREATE TABLE seasons (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medias_id      UUID NOT NULL REFERENCES medias(id),
    tmdb_id        INT NOT NULL,
    imdb_id        VARCHAR(50) NOT NULL,
    sinopse        TEXT,
    status         VARCHAR(20)
                   CHECK (status IN ('UPCOMING', 'COMPLETED', 'AIRING')),
    season_number  INT NOT NULL,
    thumbnail_url  TEXT,
    title          VARCHAR(255),
    aired_at       TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- EPISODES
-- ============================================================
CREATE TABLE episodes (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title          VARCHAR(255) NOT NULL,
    sinopse        TEXT,
    thumbnail_url  TEXT,
    seasons_id     UUID NOT NULL REFERENCES seasons(id),
    episode_number INT NOT NULL,
    season_number  INT NOT NULL,
    duration       REAL NOT NULL DEFAULT 0,
    score          DOUBLE PRECISION NOT NULL DEFAULT 0,
    scored_by      INT,
    tmdb_id        INT,
    created_at     TIMESTAMP NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- THEMES
-- ============================================================
CREATE TABLE themes (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medias_id   UUID NOT NULL REFERENCES medias(id),
    theme_type  VARCHAR(20) NOT NULL
                CHECK (theme_type IN ('OPENING', 'ENDING', 'MUSIC', 'VIDEO', 'TRAILER', 'OST')),
    url         TEXT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- COMMENTS
-- ============================================================
CREATE TABLE comments (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    comment     TEXT NOT NULL,
    medias_id   UUID NOT NULL REFERENCES medias(id),
    users_id    UUID NOT NULL REFERENCES users(id),
    episodes_id UUID NOT NULL REFERENCES episodes(id),
    parent_id   UUID REFERENCES comments(id),
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- MEDIA_CATEGORIES (junction)
-- ============================================================
CREATE TABLE media_categories (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medias_id     UUID NOT NULL REFERENCES medias(id),
    categories_id UUID NOT NULL REFERENCES categories(id),
    created_at    TIMESTAMP NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (medias_id, categories_id)
);

-- ============================================================
-- MEDIAS_TAGS (junction)
-- ============================================================
CREATE TABLE medias_tags (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medias_id  UUID NOT NULL REFERENCES medias(id),
    tags_id    UUID NOT NULL REFERENCES tags(id),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (medias_id, tags_id)
);

-- ============================================================
-- PLAYLISTS
-- ============================================================
CREATE TABLE playlists (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL,
    image_url   TEXT,
    is_private  BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    owner_id    UUID NOT NULL REFERENCES users(id),
    parent_id   UUID REFERENCES playlists(id),
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- PLAYLIST_ITEMS
-- ============================================================
CREATE TABLE playlist_items (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    playlist_id UUID NOT NULL REFERENCES playlists(id),
    media_id    UUID NOT NULL REFERENCES medias(id),
    position    INT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (playlist_id, media_id)
);

-- ============================================================
-- USER_MEDIAS
-- ============================================================
CREATE TABLE user_medias (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    watch_status VARCHAR(20) NOT NULL
                 CHECK (watch_status IN ('WATCHING', 'WATCHED', 'DROPPED', 'WATCH_LATER', 'NOT_STARTED', 'NONE')),
    score        DOUBLE PRECISION NOT NULL DEFAULT 0,
    favorite     BOOLEAN NOT NULL DEFAULT FALSE,
    users_id     UUID NOT NULL REFERENCES users(id),
    review       TEXT,
    medias_id    UUID NOT NULL REFERENCES medias(id),
    created_at   TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (users_id, medias_id)
);

-- ============================================================
-- USER_EPISODES
-- ============================================================
CREATE TABLE user_episodes (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    watch_status VARCHAR(20)
                 CHECK (watch_status IN ('WATCHING', 'WATCHED', 'DROPPED', 'WATCH_LATER', 'NOT_STARTED', 'NONE')),
    score        DOUBLE PRECISION NOT NULL DEFAULT 0,
    users_id     UUID NOT NULL REFERENCES users(id),
    episodes_id  UUID NOT NULL REFERENCES episodes(id),
    created_at   TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (users_id, episodes_id)
);

-- ============================================================
-- REFRESH_TOKEN
-- ============================================================
CREATE TABLE refresh_token (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(500) NOT NULL UNIQUE,
    users_id    UUID NOT NULL REFERENCES users(id),
    expires_at  TIMESTAMP NOT NULL,
    revoked     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- INDEXES úteis (FKs não geram índice automático no Postgres)
-- ============================================================
CREATE INDEX idx_seasons_medias_id        ON seasons(medias_id);
CREATE INDEX idx_episodes_seasons_id      ON episodes(seasons_id);
CREATE INDEX idx_themes_medias_id         ON themes(medias_id);
CREATE INDEX idx_comments_medias_id       ON comments(medias_id);
CREATE INDEX idx_comments_users_id        ON comments(users_id);
CREATE INDEX idx_comments_episodes_id     ON comments(episodes_id);
CREATE INDEX idx_comments_parent_id       ON comments(parent_id);
CREATE INDEX idx_media_categories_medias  ON media_categories(medias_id);
CREATE INDEX idx_media_categories_cats    ON media_categories(categories_id);
CREATE INDEX idx_medias_tags_medias       ON medias_tags(medias_id);
CREATE INDEX idx_medias_tags_tags         ON medias_tags(tags_id);
CREATE INDEX idx_playlists_owner_id       ON playlists(owner_id);
CREATE INDEX idx_playlists_parent_id      ON playlists(parent_id);
CREATE INDEX idx_playlist_items_playlist  ON playlist_items(playlist_id);
CREATE INDEX idx_playlist_items_media     ON playlist_items(media_id);
CREATE INDEX idx_user_medias_users_id     ON user_medias(users_id);
CREATE INDEX idx_user_medias_medias_id    ON user_medias(medias_id);
CREATE INDEX idx_user_episodes_users_id   ON user_episodes(users_id);
CREATE INDEX idx_user_episodes_episodes   ON user_episodes(episodes_id);
CREATE INDEX idx_refresh_token_users_id   ON refresh_token(users_id);
