package dev.matheushnt.url_shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CreateShortLinkRequest(
    @Schema(description = "URL original que será encurtada", example = "https://example.com")
    @NotBlank(message = "O campo [originalUrl] é obrigatório")
    @Size(max = 2048, message = "O campo [originalUrl] deve conter no máximo 2048 caracteres")
    String originalUrl
) {}
