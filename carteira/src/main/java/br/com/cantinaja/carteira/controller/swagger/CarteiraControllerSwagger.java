package br.com.cantinaja.carteira.controller.swagger;

import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
import br.com.cantinaja.carteira.dto.TransacaoResponseDTO;
import br.com.cantinaja.carteira.model.TipoTransacao;
import br.com.cantinaja.common.exception.ErroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Documentação OpenAPI do {@code CarteiraController}. Concentra TODA a
 * configuração Swagger (operação, exemplos de request e respostas de erro)
 * para manter o controller e os DTOs limpos. O controller implementa esta
 * interface e o springdoc herda as anotações.
 */
@Tag(name = "Carteiras", description = "Recarga, consulta e movimentação da carteira do aluno")
public interface CarteiraControllerSwagger {

    @Operation(summary = "Recarrega a carteira do aluno", description = "Cria a carteira automaticamente na primeira recarga. "
            + "Retorna a carteira atualizada, sem necessidade de segunda chamada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recarga realizada com sucesso — retorna a carteira atualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarteiraResponseDTO.class), examples = @ExampleObject(name = "Carteira atualizada", value = "{\"alunoId\": 1, \"saldo\": 50.00, \"saldoBaixo\": false}"))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou valor fora da faixa permitida (R$ 5,00 a R$ 500,00)", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErroResponse.class), examples = {
                    @ExampleObject(name = "Valor fora da faixa", value = "{\"erro\": \"RECARGA_FORA_DA_FAIXA\", \"mensagem\": \"A recarga deve estar entre R$ 5,00 e R$ 500,00\"}"),
                    @ExampleObject(name = "Dado inválido", value = "{\"erro\": \"DADO_INVALIDO\", \"mensagem\": \"valor: O valor da recarga é obrigatório\"}")
            }))
    })
    ResponseEntity<CarteiraResponseDTO> recarregar(
            @Parameter(description = "Identificador do aluno dono da carteira", example = "1") Long alunoId,

            @RequestBody(required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Recarga válida", value = "{\"valor\": 50.00}"))) RecargaRequestDTO request);

    @Operation(summary = "Consultar transações da carteira", description = "Retorna a lista de transações do aluno. Permite filtrar por tipo de transação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de transação inválido", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "TipoInvalido", value = "{\"erro\": \"TIPO_TRANSACAO_INVALIDO\", \"mensagem\": \"O tipo de transação informado é inválido\"}")))
    })
    ResponseEntity<List<TransacaoResponseDTO>> consultarTransacoes(
            @Parameter(description = "Identificador do aluno dono da carteira", example = "1") Long alunoId,
            @Parameter(description = "Tipo da transação para filtro (valores aceitos no enum TipoTransacao)", example = "PIX") TipoTransacao tipo
    );
}

