package com.avaneesh.vimo_backend.common.entities;

import com.avaneesh.vimo_backend.common.enums.Visibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(length = 5000)
    private String description;

    private String contentType;
    private String filePath;
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC;

    @Min(value = 0, message = "View count cannot be negative")
    @Builder.Default
    private Long views = 0L;

    @Min(value = 0, message = "Like count cannot be negative")
    @Builder.Default
    private Long likes = 0L;

    @Min(value = 0, message = "Dislike count cannot be negative")
    @Builder.Default
    private Long dislikes = 0L;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

//    @ManyToOne
//    @JoinColumn(name = "playlist_id")
//    private Playlist playlist;

}