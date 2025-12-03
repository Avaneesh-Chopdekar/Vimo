package com.avaneesh.vimo_backend.insights.controllers;

import com.avaneesh.vimo_backend.common.entities.Video;
import com.avaneesh.vimo_backend.insights.dtos.MetadataResponse;
import com.avaneesh.vimo_backend.insights.services.InsightsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/insights")
@CrossOrigin("*")
@Tag(name = "Insights API")
public class InsightController {

    private final InsightsService insightsService;

    public InsightController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }


    @GetMapping("/metadata/{id}")
    public ResponseEntity<MetadataResponse> getMetadata(@PathVariable Long id) {
        Optional<Video> video = insightsService.getVideoById(id);

        return video.map(value -> ResponseEntity.ok().body(MetadataResponse.fromVideo(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
