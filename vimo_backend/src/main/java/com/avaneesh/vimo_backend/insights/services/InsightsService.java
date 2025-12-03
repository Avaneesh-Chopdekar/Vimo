package com.avaneesh.vimo_backend.insights.services;

import com.avaneesh.vimo_backend.common.entities.Video;

import java.util.Optional;

public interface InsightsService {
    Optional<Video> getVideoById(Long videoId);
}
