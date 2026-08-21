package dev.matheushnt.url_shortener.documentation;

import dev.matheushnt.url_shortener.dto.CreateShortLinkRequest;
import dev.matheushnt.url_shortener.dto.ShortLinkResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
                                                "instance": "/links"
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
                                                        "instance": "/links",
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
                                                        "instance": "/links"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<ShortLinkResult> create(@Valid @RequestBody CreateShortLinkRequest shortLinkRequest);

    @Operation(summary = "Busca um link curto e redireciona para a URL original", description = "Busca os dados de um link curto utilizando seu código e redireciona para a URL original.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "302",
                    description = "Código encontrado e redirecionado para a URL original"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Link curto não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.url_shortener.com/problems/short-link-not-found",
                                                "title": "NOT_FOUND",
                                                "status": 404,
                                                "detail": "Link curto não encontrado",
                                                "instance": "/links/trBLg"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "O link curto encontra-se expirado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.url_shortener.com/problems/short-link-has-expired",
                                                "title": "GONE",
                                                "status": 410,
                                                "detail": "O link curto expirou",
                                                "instance": "/links/trBLg"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> findByShortCode(
            @Parameter(
                    description = "Código do link curto",
                    example = "trBLg"
            )
            @PathVariable String shortCode
    );

    @Operation(summary = "Busca os metadados de um link curto", description = "Busca pelo código os metadados de um link curto.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna os metadados de um link curto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShortLinkResult.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "shortCode": "aZ91k",
                                                "shortUrl": "https://url_shortener.com/aZ91k",
                                                "originalUrl": "https://www.example.com/artigo",
                                                "createdAt": "2026-08-15T19:30:00",
                                                "expiresAt": "2026-08-22T19:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Link curto não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.url_shortener.com/problems/short-link-not-found",
                                                "title": "NOT_FOUND",
                                                "status": 404,
                                                "detail": "Link curto não encontrado",
                                                "instance": "/links/aZ91k"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ShortLinkResult> getMetadata(
            @Parameter(
                    description = "Código do link curto",
                    example = "aZ91k"
            )
            @PathVariable String shortCode
    );

    @Operation(summary = "Busca um link curto e o exclui", description = "Busca pelo código um link curto e exclui ele.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Link curto excluído com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Link curto não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.url_shortener.com/problems/short-link-not-found",
                                                "title": "NOT_FOUND",
                                                "status": 404,
                                                "detail": "Link curto não encontrado",
                                                "instance": "/links/aZ91k"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "O link curto encontra-se expirado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.url_shortener.com/problems/short-link-has-expired",
                                                "title": "GONE",
                                                "status": 410,
                                                "detail": "O link curto expirou",
                                                "instance": "/links/trBLg"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Código do link curto",
                    example = "aZ91k"
            )
            @PathVariable String shortCode
    );

}
