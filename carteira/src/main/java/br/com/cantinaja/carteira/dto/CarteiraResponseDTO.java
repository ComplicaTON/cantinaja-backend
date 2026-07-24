package br.com.cantinaja.carteira.dto;

import java.math.BigDecimal;

public record CarteiraResponseDTO(

        Long alunoId,
        BigDecimal saldo,
        boolean saldoBaixo

) {
}