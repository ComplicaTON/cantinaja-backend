package br.com.cantinaja.cardapio.service;

import br.com.cantinaja.cardapio.dto.ItemRequest;
import br.com.cantinaja.cardapio.dto.ItemResponse;
import br.com.cantinaja.cardapio.model.Item;
import br.com.cantinaja.cardapio.repository.ItemRepository;
import br.com.cantinaja.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public ItemResponse criar(ItemRequest dto) {
        String nomeSanitizado = dto.nome().trim().replace("-", "");
        Boolean existeItem = repository.existsByNomeIgnoreCase(nomeSanitizado);

        if (existeItem) {
            throw new BusinessException(HttpStatus.CONFLICT, dto.nome() + " já existe no cardápio");
        }

        // Implementar salvamento e retorno de dados
        return null;
    }
}
