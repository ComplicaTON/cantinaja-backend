package br.com.cantinaja.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Schema de documentação do corpo de erro padrão da API (RFC 9457 +
 * extensões do projeto). Serve apenas para o Swagger desenhar o formato das
 * respostas de erro — o corpo real é montado em runtime pelo
 * {@link GlobalExceptionHandler}, não por esta classe.
 *
 * <p>Referencie nas interfaces de documentação dos controllers:
 * <pre>{@code
 * @ApiResponse(responseCode = "409", description = "Conflito",
 *         content = @Content(schema = @Schema(implementation = ErroResponse.class)))
 * }</pre>
 */
@Schema(name = "ErroResponse", description = "Corpo padrão de erro (application/problem+json)")
public record ErroResponse(

        @Schema(description = "URI do tipo do problema", example = "about:blank")
        String type,

        @Schema(description = "Título curto do erro", example = "Conflict")
        String title,

        @Schema(description = "Código HTTP", example = "409")
        int status,

        @Schema(description = "Detalhe legível do problema",
                example = "Coxinha de Frango já existe no cardápio")
        String detail,

        @Schema(description = "Código estável do erro, para o frontend decidir comportamento",
                example = "NOME_DUPLICADO")
        String erro,

        @Schema(description = "Mensagem exibível ao usuário (espelha detail)",
                example = "Coxinha de Frango já existe no cardápio")
        String mensagem
) {
}
