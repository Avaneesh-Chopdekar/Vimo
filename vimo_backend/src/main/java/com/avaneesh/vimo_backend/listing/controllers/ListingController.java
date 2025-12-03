package com.avaneesh.vimo_backend.listing.controllers;

import com.avaneesh.vimo_backend.listing.dtos.ListItem;
import com.avaneesh.vimo_backend.listing.services.ListingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/listing")
@CrossOrigin("*")
@Tag(name = "Listing API")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public ResponseEntity<List<ListItem>> getAllVideos() {
        return ResponseEntity.ok().body(listingService.getAllVideos().stream().map(ListItem::fromEntity).toList());
    }
}
