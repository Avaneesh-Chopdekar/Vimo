package com.avaneesh.vimo_backend.listing.services.impl;

import com.avaneesh.vimo_backend.common.entities.Video;
import com.avaneesh.vimo_backend.common.repositories.VideoRepository;
import com.avaneesh.vimo_backend.listing.services.ListingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingServiceImpl implements ListingService {
    private final VideoRepository videoRepository;

    public ListingServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
}
