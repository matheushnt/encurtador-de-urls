package dev.matheushnt.url_shortener.dto;

import dev.matheushnt.url_shortener.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "O campo [email] é obrigatório")
        @Email(message = "E-mail é inválido", regexp = "^\\S+@\\S+\\.\\S+$")
        String email,

        @NotBlank(message = "O campo [password] é obrigatório")
        @Password
        String password
) {}
