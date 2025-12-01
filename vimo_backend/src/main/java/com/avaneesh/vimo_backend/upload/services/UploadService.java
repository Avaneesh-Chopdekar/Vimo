package com.avaneesh.vimo_backend.upload.services;


import com.avaneesh.vimo_backend.common.entities.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    Video save(Video video, MultipartFile file) throws IOException;

    void processVideo(Long videoId);
}
