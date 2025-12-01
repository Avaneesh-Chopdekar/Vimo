package com.avaneesh.vimo_backend.streaming.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/stream")
@CrossOrigin("*")
@Tag(name = "Streaming API")
public class StreamingController {

    @Value("${custom.video.hls_path}")
    String HLS_DIR;

    @GetMapping("/{videoId}/master.m3u8")
    public ResponseEntity<Resource> serveMasterFile(@PathVariable Long videoId) {
        Path path = Paths.get(HLS_DIR, videoId.toString(), "master.m3u8");

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(path);

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl"
                )
                .body(resource);
    }

    @GetMapping("/{videoId}/{segment}.ts")
    public ResponseEntity<Resource> serveSegment(
            @PathVariable Long videoId,
            @PathVariable String segment
    ) {
        Path path = Paths.get(HLS_DIR, videoId.toString(), segment + ".ts");
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(path);

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_TYPE, "video/mp2t"
                )
                .body(resource);
    }
}
