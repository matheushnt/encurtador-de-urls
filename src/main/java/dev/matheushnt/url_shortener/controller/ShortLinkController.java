package dev.matheushnt.url_shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.matheushnt.url_shortener.documentation.ShortLinkApi;
import dev.matheushnt.url_shortener.dto.CreateShortLinkRequest;
import dev.matheushnt.url_shortener.dto.ShortLinkResult;
import dev.matheushnt.url_shortener.service.CreateShortLinkService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/links")
public class ShortLinkController implements ShortLinkApi {

    @Autowired
    private CreateShortLinkService createShortLinkService;

    @PostMapping
    @Override
    public ResponseEntity<ShortLinkResult> create(@Valid @RequestBody CreateShortLinkRequest shortLinkRequest) {
        ShortLinkResult result = this.createShortLinkService.create(shortLinkRequest.originalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
