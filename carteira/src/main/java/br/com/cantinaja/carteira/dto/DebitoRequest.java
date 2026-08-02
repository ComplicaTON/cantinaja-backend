package br.com.cantinaja.carteira.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;

public record DebitoRequest(

        @NotNull(message = "O valor do débito é obrigatório")
        @Positive(message = "O valor do débito deve ser maior que zero")
        BigDecimal valor
) {}