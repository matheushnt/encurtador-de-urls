package dev.matheushnt.url_shortener.service;

import java.time.LocalDateTime;

import dev.matheushnt.url_shortener.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.matheushnt.url_shortener.dto.ShortLinkResult;
import dev.matheushnt.url_shortener.exception.InvalidUrlException;
import dev.matheushnt.url_shortener.model.ShortLink;
import dev.matheushnt.url_shortener.repository.ShortLinkRepository;
import dev.matheushnt.url_shortener.util.Base62;
import dev.matheushnt.url_shortener.util.URL;

@Service
public class CreateShortLinkService {

    @Autowired
    private ShortLinkRepository shortLinkRepository;

    @Autowired
    private Base62 base62;

    @Autowired
    private URL urlUtils;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Value("${app.durationDaysShortLink}")
    private int durationDaysShortLink;

    @Transactional
    public ShortLinkResult create(User user, String originalUrl) {
        if (!this.urlUtils.isValidUrl(originalUrl)) {
            throw new InvalidUrlException("URL informada é inválida");
        }

        if (!this.urlUtils.isValidProtocol(originalUrl)) {
            throw new InvalidUrlException("Apenas protocolos HTTP e HTTPS são aceitos");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(durationDaysShortLink);

        ShortLink shortLink = this.shortLinkRepository.save(ShortLink.create("short_code_temp", originalUrl, now, expiresAt, user));
        String shortCode = base62.encode(shortLink.getId());
        shortLink.setShortCode(shortCode);
        this.shortLinkRepository.save(shortLink);
        String shortUrl = this.urlUtils.buildShortUrl(baseUrl, shortCode);

        return new ShortLinkResult(shortLink.getShortCode(), shortUrl, originalUrl, now, expiresAt);
    }

}
