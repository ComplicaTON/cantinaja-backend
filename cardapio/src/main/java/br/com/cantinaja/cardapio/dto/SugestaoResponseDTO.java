package br.com.cantinaja.cardapio.dto;

import br.com.cantinaja.cardapio.model.Sugestao;

import java.time.LocalDateTime;

public record SugestaoResponseDTO(
        Long id,
        String descricao,
        Long alunoId,
        LocalDateTime dataHora
) {
    public static SugestaoResponseDTO dados(Sugestao s) {
        return new SugestaoResponseDTO(s.getId(), s.getDescricao(), s.getAlunoId(), s.getDataHora());
    }
}
