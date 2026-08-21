package dev.matheushnt.url_shortener.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public final class URL {

    public String buildShortUrl(String baseUrl, String shortCode) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/{shortCode}")
                .buildAndExpand(shortCode)
                .encode()
                .toUriString();
    }

    public boolean isValidProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

}
