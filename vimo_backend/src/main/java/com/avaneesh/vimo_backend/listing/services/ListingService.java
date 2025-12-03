package com.avaneesh.vimo_backend.listing.services;

import com.avaneesh.vimo_backend.common.entities.Video;

import java.util.List;

public interface ListingService {
    List<Video> getAllVideos();
//    List<Video> getVideosByUser(Long userId);
//    List<Video> getVideosByUserAndVisibility(Long userId, String visibility);
}
