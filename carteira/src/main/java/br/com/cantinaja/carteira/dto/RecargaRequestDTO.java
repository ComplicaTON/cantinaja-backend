package br.com.cantinaja.carteira.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecargaRequestDTO (

    @NotNull(message = "O valor da recarga é obrigatório")
    @Positive(message = "O valor da recarga não pode ser negativo")
    BigDecimal valor
) {}
