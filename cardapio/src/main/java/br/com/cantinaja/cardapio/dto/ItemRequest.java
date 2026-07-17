package br.com.cantinaja.cardapio.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank(message = "Nome não pode ser vazio ou nulo")
        String nome,
        @NotNull
        @DecimalMin(value = "0.00", message = "O valor não pode ser menor que 0.00")
        BigDecimal preco
) {
}
