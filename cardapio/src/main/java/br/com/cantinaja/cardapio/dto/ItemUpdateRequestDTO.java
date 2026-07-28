package br.com.cantinaja.cardapio.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record ItemUpdateRequestDTO(
        @Pattern(regexp = "^[\\p{L} ]+$", message = "O nome deve conter apenas letras e espaços")
        String nome,
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco
) {
}
