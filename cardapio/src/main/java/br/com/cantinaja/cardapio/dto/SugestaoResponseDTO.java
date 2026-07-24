package br.com.cantinaja.cardapio.dto;

import java.time.LocalDateTime;

public record SugestaoResponseDTO(
        Long id,
        String descricao,
        Long alunoId,
        LocalDateTime dataHora
) {
}
