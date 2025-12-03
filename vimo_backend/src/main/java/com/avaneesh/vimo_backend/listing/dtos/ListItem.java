package com.avaneesh.vimo_backend.listing.dtos;

import com.avaneesh.vimo_backend.common.entities.Video;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ListItem {
    private Long id;
    private String title;
    private String thumbnailUrl;

    public static ListItem fromEntity(Video video) {
        return ListItem.builder()
                .id(video.getId())
                .title(video.getTitle())
                .thumbnailUrl(video.getThumbnailUrl())
                .build();
    }
}

// TODO: In future add more fields like duration, views, uploadDate etc.