package br.com.cantinaja.carteira.dto;
import br.com.cantinaja.carteira.model.TipoTransacao;
import br.com.cantinaja.carteira.model.TransacaoCarteira;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record TransacaoResponseDTO(
        Long id,
        TipoTransacao tipo,
        BigDecimal valor,
        LocalDateTime dataHora
) {
    public static TransacaoResponseDTO fromEntity(TransacaoCarteira transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getTipo(),
                transacao.getValor(),
                transacao.getDataHora()
        );
    }

}