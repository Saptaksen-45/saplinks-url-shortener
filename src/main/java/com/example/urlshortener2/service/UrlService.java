package com.example.urlshortener2.service;

import com.example.urlshortener2.entity.ShortUrl;
import com.example.urlshortener2.repository.ShortUrlRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;

    public UrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public ShortUrl shortenUrl(String originalUrl) {

        // URL validation
        if (originalUrl == null ||
                (!originalUrl.startsWith("http://")
                        && !originalUrl.startsWith("https://"))) {

            throw new IllegalArgumentException("Invalid URL");
        }

        // Check if URL already exists
        Optional<ShortUrl> existingUrl =
                shortUrlRepository.findByOriginalUrl(originalUrl);

        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        // Generate unique short code
        String shortCode = UUID.randomUUID()
                .toString()
                .substring(0, 6);

        // Create ShortUrl object
        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);

        // Save URL in database
        return shortUrlRepository.save(shortUrl);
    }

    public String getOriginalUrl(String shortCode) {

        ShortUrl shortUrl = shortUrlRepository
                .findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Short URL not found"
                ));

        return shortUrl.getOriginalUrl();
    }
}
