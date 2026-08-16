package dev.matheushnt.url_shortener.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import dev.matheushnt.url_shortener.dto.ShortLinkResult;
import dev.matheushnt.url_shortener.exception.InvalidUrlException;
import dev.matheushnt.url_shortener.model.ShortLink;
import dev.matheushnt.url_shortener.repository.ShortLinkRepository;
import dev.matheushnt.url_shortener.util.Base62;

@Service
public class CreateShortLinkService {

    @Autowired
    private ShortLinkRepository shortLinkRepository;

    @Autowired
    private Base62 base62;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Value("${app.durationDaysShortLink}")
    private int durationDaysShortLink;

    @Transactional
    public ShortLinkResult create(String originalUrl) {
        if (!this.isValidUrl(originalUrl)) {
            throw new InvalidUrlException("URL informada é inválida");
        }

        if (!this.isValidProtocol(originalUrl)) {
            throw new InvalidUrlException("Apenas protocolos HTTP e HTTPS são aceitos");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(durationDaysShortLink);

        ShortLink shortLink = this.shortLinkRepository.save(ShortLink.create("short_code_temp", originalUrl, now, expiresAt));
        String shortCode = base62.encode(shortLink.getId());
        shortLink.setShortCode(shortCode);
        this.shortLinkRepository.save(shortLink);
        String shortUrl = this.buildShortUrl(shortCode);

        return new ShortLinkResult(shortLink.getShortCode(), shortUrl, originalUrl, now, expiresAt);
    }

    private boolean isValidProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private String buildShortUrl(String shortCode) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/{shortCode}")
                .buildAndExpand(shortCode)
                .encode()
                .toUriString();
    }

}
