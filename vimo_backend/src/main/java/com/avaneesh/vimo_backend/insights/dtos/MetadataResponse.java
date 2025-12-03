package com.avaneesh.vimo_backend.insights.dtos;

import com.avaneesh.vimo_backend.common.entities.Video;
import com.avaneesh.vimo_backend.common.enums.Visibility;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MetadataResponse {
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long views;
    private Long likes;
    private Long dislikes;
    private Visibility visibility;
    private LocalDateTime uploadDate;

    public static MetadataResponse fromVideo(Video video) {
        MetadataResponse response = new MetadataResponse();
        response.setTitle(video.getTitle());
        response.setDescription(video.getDescription());
        response.setThumbnailUrl(video.getThumbnailUrl());
        response.setViews(video.getViews());
        response.setLikes(video.getLikes());
        response.setDislikes(video.getDislikes());
        response.setVisibility(video.getVisibility());
        response.setUploadDate(video.getUploadDate());
        return response;
    }
}
