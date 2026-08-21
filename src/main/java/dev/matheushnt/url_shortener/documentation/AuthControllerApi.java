package dev.matheushnt.url_shortener.documentation;

import dev.matheushnt.url_shortener.dto.AccessToken;
import dev.matheushnt.url_shortener.dto.SignInRequest;
import dev.matheushnt.url_shortener.dto.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Operações relacionadas a Autenticação")
public interface AuthControllerApi {

    @Operation(summary = "Registrar", description = "Registra um novo usuário")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cadastro realizado com sucesso",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "O corpo da requisição está malformado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                                    {
                                                        "type": "https://api.url_shortener.com/problems/invalid-data",
                                                        "title": "BAD_REQUEST",
                                                        "status": 400,
                                                        "detail": "O corpo da requisição está malformado",
                                                        "instance": "/links"
                                                    }
                                            """
                            )
                    )
            ), @ApiResponse(
            responseCode = "409",
            description = "Outro usuário já cadastrado com o mesmo e-mail",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(
                            value = """
                                            {
                                                 "detail": "E-mail já cadastrado",
                                                 "instance": "/auth/sign-up",
                                                 "status": 409,
                                                 "title": "CONFLICT",
                                                 "type": "https://api.url_shortener.com/problems/conflict"
                                             }
                                    """
                    )
            )
    ),
            @ApiResponse(
                    responseCode = "422",
                    description = "A requisição não pôde ser processada devido a erro de validação ou regra de negócio",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                                    {
                                                        "detail": "Um ou mais campos são inválidos",
                                                        "instance": "/auth/sign-up",
                                                        "status": 422,
                                                        "title": "UNPROCESSABLE_CONTENT",
                                                        "type": "https://api.url_shortener.com/problems/validation-error",
                                                        "errors": [
                                                            {
                                                                "field": "password",
                                                                "message": "deve conter ao menos um número"
                                                            },
                                                            {
                                                                "field": "password",
                                                                "message": "deve conter ao menos um símbolo (!@#$%&*_)"
                                                            },
                                                            {
                                                                "field": "email",
                                                                "message": "E-mail é inválido"
                                                            }
                                                        ]
                                                    }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest);

    @Operation(summary = "Realizar login", description = "Realiza login com e-mail e senha")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessToken.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "O corpo da requisição está malformado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                                    {
                                                        "type": "https://api.url_shortener.com/problems/invalid-data",
                                                        "title": "BAD_REQUEST",
                                                        "status": 400,
                                                        "detail": "O corpo da requisição está malformado",
                                                        "instance": "/links"
                                                    }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "As credenciais informadas são inválidas",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                                    {
                                                        "detail": "Credenciais inválidas",
                                                        "instance": "/auth/sign-in",
                                                        "status": 401,
                                                        "title": "UNAUTHORIZED",
                                                        "type": "https://api.url_shortener.com/problems/bad-credentials"
                                                    }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "A requisição não pôde ser processada devido a erro de validação ou regra de negócio",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                                    {
                                                         "detail": "Um ou mais campos são inválidos",
                                                         "instance": "/auth/sign-in",
                                                         "status": 422,
                                                         "title": "UNPROCESSABLE_CONTENT",
                                                         "type": "https://api.url_shortener.com/problems/validation-error",
                                                         "errors": [
                                                             {
                                                                 "field": "password",
                                                                 "message": "O campo [password] é obrigatório"
                                                             }
                                                         ]
                                                    }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<AccessToken> signIn(@Valid @RequestBody SignInRequest signInRequest);

}
