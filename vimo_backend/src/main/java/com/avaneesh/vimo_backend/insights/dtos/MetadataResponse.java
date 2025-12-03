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

    public static MetadataResponse fromEntity(Video video) {
        return MetadataResponse.builder()
                .title(video.getTitle())
                .description(video.getDescription())
                .thumbnailUrl(video.getThumbnailUrl())
                .views(video.getViews())
                .likes(video.getLikes())
                .dislikes(video.getDislikes())
                .visibility(video.getVisibility())
                .uploadDate(video.getUploadDate())
                .build();
    }
}
