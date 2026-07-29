package br.com.cantinaja.cardapio.controller.swagger;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.dto.ItemResponseDTO;
import br.com.cantinaja.cardapio.dto.ItemUpdateRequestDTO;
import br.com.cantinaja.cardapio.model.Item;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

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
    ResponseEntity<Void> cadastrar(
            @RequestBody(required = true, content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Item válido",
                            value = "{\"nome\": \"Coxinha de Frango\", \"preco\": 10.00}")))
            ItemRequestDTO dto,
            HttpServletRequest request);

    @Operation(summary = "Atualiza parcialmente um item (Nome e/ou Preço)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Variação de preço inválida (acima do dobro ou abaixo da metade)",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe outro item com este novo nome",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponse.class)))
    })
    ResponseEntity<Item> atualizar(
            Long id,
            @RequestBody(required = true, content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "Atualizar só o Preço", value = "{\"preco\": 12.50}"),
                            @ExampleObject(name = "Atualizar só o Nome", value = "{\"nome\": \"Coxinha de Catupiry\"}"),
                            @ExampleObject(name = "Atualizar Ambos", value = "{\"nome\": \"Coxinha Especial\", \"preco\": 15.00}")
                    }))
            ItemUpdateRequestDTO dto);

    ResponseEntity<Page<ItemResponseDTO>> listarTodos(
            @RequestParam(required = false) Boolean disponivel,
            @PageableDefault(size = 10, sort = "id") Pageable pageable);
}

