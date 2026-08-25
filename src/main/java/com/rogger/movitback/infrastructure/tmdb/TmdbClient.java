package com.rogger.movitback.infrastructure.tmdb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class TmdbClient {

    private final RestClient tmdbRestClient;

}