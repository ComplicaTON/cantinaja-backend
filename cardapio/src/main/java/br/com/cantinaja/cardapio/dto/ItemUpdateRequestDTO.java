package br.com.cantinaja.cardapio.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record ItemUpdateRequestDTO(
        @Nullable
        @Pattern(regexp = "^[\\p{L} ]+$", message = "O nome deve conter apenas letras e espaços") // Refazer o pattern para aceitar no mínimo 3 caracteres
        String nome,
        @Nullable
        @DecimalMin(value = "0.00", message = "O preço deve ser maior que zero")
        BigDecimal preco
) {
}
