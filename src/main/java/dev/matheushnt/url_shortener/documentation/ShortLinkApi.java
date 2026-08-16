package dev.matheushnt.url_shortener.documentation;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import dev.matheushnt.url_shortener.dto.CreateShortLinkRequest;
import dev.matheushnt.url_shortener.dto.ShortLinkResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Short Links", description = "Operações relacionadas a links encurtados")
public interface ShortLinkApi {

    @Operation(summary = "Cria um novo link curto", description = "Cria um novo link curto a partir de uma URL longa.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Link curto criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ShortLinkResult.class)
            )
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
                            "instance": "/api/short-links"
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
                examples = {
                    @ExampleObject(
                        name = "Erro de validação",
                        value = """
                            {
                                "type": "https://api.url_shortener.com/problems/validation-error",
                                "title": "UNPROCESSABLE_CONTENT",
                                "status": 422,
                                "detail": "Um ou mais campos são inválidos",
                                "instance": "/api/short-links",
                                "errors": [
                                    {
                                        "field": "originalUrl",
                                        "message": "O campo [originalUrl] é obrigatório"
                                    }
                                ]
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "URL inválida",
                        value = """
                            {
                                "type": "https://api.url_shortener.com/problems/invalid-url",
                                "title": "UNPROCESSABLE_CONTENT",
                                "status": 422,
                                "detail": "URL informada é inválida",
                                "instance": "/api/short-links"
                            }
                            """
                    )
                }
            )
        )
    })
    ResponseEntity<ShortLinkResult> create(@Valid @RequestBody CreateShortLinkRequest shortLinkRequest);

}
