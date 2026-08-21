package dev.matheushnt.url_shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados do link encurtado")
public record ShortLinkResult(
        @Schema(description = "Código identificador do link curto", example = "aZ91k")
        String shortCode,

        @Schema(description = "URL pública do link curto", example = "https://example.com/aZ91k")
        String shortUrl,

        @Schema(description = "URL original", example = "https://www.example.com/artigo")
        String originalUrl,

        @Schema(description = "Data e hora de criação do link", example = "2026-08-15T19:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora de expiração do link", example = "2026-08-22T19:30:00")
        LocalDateTime expiresAt
) {
}
