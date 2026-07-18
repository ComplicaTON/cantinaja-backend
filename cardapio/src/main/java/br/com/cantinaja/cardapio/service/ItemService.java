package br.com.cantinaja.cardapio.service;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
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

    public Item criar(ItemRequestDTO dto) {
        String nomeSanitizado = dto.nome().strip();
        Boolean existeItem = repository.existsByNomeIgnoreCase(nomeSanitizado);

        if (existeItem) {
            throw new BusinessException(HttpStatus.CONFLICT, dto.nome() + " já existe no cardápio");
        }

        Item item = new Item(dto.nome(), dto.preco(), true);
        return repository.save(item);
    }
}
