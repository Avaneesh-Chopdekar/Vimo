package com.avaneesh.vimo_backend.common.repositories;

import com.avaneesh.vimo_backend.common.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByTitleContainingIgnoreCase(String title);
}
