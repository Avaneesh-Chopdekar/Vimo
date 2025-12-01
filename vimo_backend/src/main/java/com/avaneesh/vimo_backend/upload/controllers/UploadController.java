package com.avaneesh.vimo_backend.upload.controllers;

import com.avaneesh.vimo_backend.common.entities.Video;
import com.avaneesh.vimo_backend.common.payloads.CustomMessage;
import com.avaneesh.vimo_backend.upload.services.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/upload")
@CrossOrigin("*")
@Tag(name = "Video API")
public class UploadController {

    @Value("${custom.video.hls_path}")
    String HLS_DIR;

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public ResponseEntity<CustomMessage<?>> create(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        try {
            Video video = new Video();
            video.setTitle(title);
            video.setDescription(description);
            Video savedVideo = uploadService.save(video, file);
            if (savedVideo != null) {
                return ResponseEntity.ok().body(new CustomMessage<Video>("Video upload successfully", true, video));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new CustomMessage<String>("Video uploaded successfully", false, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomMessage<Exception>("Video upload failed", false, e));
        }
    }
}
