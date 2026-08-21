package dev.matheushnt.url_shortener.dto;

import dev.matheushnt.url_shortener.enums.UserRole;
import dev.matheushnt.url_shortener.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotBlank(message = "O campo [fullName] é obrigatório")
        String fullName,

        @NotBlank(message = "O campo [email] é obrigatório")
        @Email(message = "E-mail é inválido", regexp = "^\\S+@\\S+\\.\\S+$")
        String email,

        @NotBlank(message = "O campo [password] é obrigatório")
        @Password
        String password,

        @NotNull(message = "O campo [role] é obrigatório")
        UserRole role
) {
}
