package dev.matheushnt.url_shortener.controller;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.matheushnt.url_shortener.documentation.ShortLinkApi;
import dev.matheushnt.url_shortener.dto.CreateShortLinkRequest;
import dev.matheushnt.url_shortener.dto.ShortLinkResult;
import dev.matheushnt.url_shortener.exception.ResourceNotFoundException;
import dev.matheushnt.url_shortener.exception.ShortLinkExpiredException;
import dev.matheushnt.url_shortener.model.ShortLink;
import dev.matheushnt.url_shortener.repository.ShortLinkRepository;
import dev.matheushnt.url_shortener.service.CreateShortLinkService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/links")
public class ShortLinkController implements ShortLinkApi {

    @Autowired
    private CreateShortLinkService createShortLinkService;

    @Autowired
    private ShortLinkRepository shortLinkRepository;

    @PostMapping
    @Override
    public ResponseEntity<ShortLinkResult> create(@Valid @RequestBody CreateShortLinkRequest shortLinkRequest) {
        ShortLinkResult result = this.createShortLinkService.create(shortLinkRequest.originalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{shortCode}")
    @Override
    public ResponseEntity<Void> findByShortCode(@PathVariable String shortCode) {
        ShortLink shortLink = this.shortLinkRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new ResourceNotFoundException("URL original não encontrada"));

        boolean hasExpired = LocalDateTime.now().isAfter(shortLink.getExpiresAt());
        if (hasExpired) {
            throw new ShortLinkExpiredException("O link curto expirou");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(shortLink.getOriginalUrl()))
            .build();
    }

}
