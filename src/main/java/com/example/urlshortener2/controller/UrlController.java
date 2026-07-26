package com.example.urlshortener2.controller;

import com.example.urlshortener2.entity.ShortUrl;
import com.example.urlshortener2.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody String originalUrl) {

        try {
            return ResponseEntity.ok(
                    urlService.shortenUrl(originalUrl)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

@GetMapping("/short/{shortCode}")
public RedirectView redirectToOriginalUrl(
        @PathVariable String shortCode) {

    String originalUrl =
            urlService.getOriginalUrl(shortCode);

    return new RedirectView(originalUrl);
}
}