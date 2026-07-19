package br.com.cantinaja.cardapio.controller.swagger;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.common.exception.ErroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

/**
 * Documentação OpenAPI do {@code ItemController}. Concentra TODA a configuração
 * Swagger (operação, exemplos de request e respostas de erro) para manter o
 * controller e os DTOs limpos. O controller implementa esta interface e o
 * springdoc herda as anotações.
 */
@Tag(name = "Itens", description = "Cadastro e gestão dos itens do cardápio")
public interface ItemControllerSwagger {

    @Operation(summary = "Cadastra um novo item do cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso (sem corpo)"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe um item com esse nome",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponse.class)))
    })
    ResponseEntity<Void> cadastrarItem(
            @RequestBody(required = true, content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Item válido",
                            value = "{\"nome\": \"Coxinha de Frango\", \"preco\": 10.00}")))
            ItemRequestDTO dto,
            HttpServletRequest request);
}
