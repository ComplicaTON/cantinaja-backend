package br.com.cantinaja.cardapio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SugestaoRequestDTO (

    @NotBlank(message = "A descrição não pode ser vazia")
    @Size(min = 4, max = 140, message = "A descrição deve ter entre 4 e 140 caracteres")
    String descricao,
    Long alunoId
) {}

