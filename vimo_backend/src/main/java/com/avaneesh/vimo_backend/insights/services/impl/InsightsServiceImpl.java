package com.avaneesh.vimo_backend.insights.services.impl;

import com.avaneesh.vimo_backend.common.entities.Video;
import com.avaneesh.vimo_backend.common.repositories.VideoRepository;
import com.avaneesh.vimo_backend.insights.services.InsightsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InsightsServiceImpl implements InsightsService {
    private final VideoRepository videoRepository;

    public InsightsServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Optional<Video> getVideoById(Long videoId) {
        return Optional.ofNullable(videoRepository.findById(videoId).orElse(null));
    }
}
