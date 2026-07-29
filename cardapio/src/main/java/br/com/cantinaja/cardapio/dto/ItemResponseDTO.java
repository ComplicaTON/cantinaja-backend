package br.com.cantinaja.cardapio.dto;

import br.com.cantinaja.cardapio.model.Item;

import java.math.BigDecimal;

public record ItemResponseDTO(Long id, String nome, BigDecimal preco, Boolean disponivel) {

    public static ItemResponseDTO fromEntity(Item item) {
        return new ItemResponseDTO(item.getId(), item.getNome(), item.getPreco(), item.getDisponivel());
    }
}
