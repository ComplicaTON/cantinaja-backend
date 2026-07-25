package br.com.cantinaja.cardapio.service;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.dto.ItemUpdateRequestDTO;
import br.com.cantinaja.cardapio.model.Item;
import br.com.cantinaja.cardapio.repository.ItemRepository;
import br.com.cantinaja.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ItemService {
    private static final BigDecimal DOBRO = new BigDecimal("2");
    private static final BigDecimal METADE = new BigDecimal("0.5");

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Item criar(ItemRequestDTO dto) {
        String nomeSanitizado = dto.nome().strip();
        Boolean existeItem = repository.existsByNomeIgnoreCase(nomeSanitizado);

        if (existeItem) {
            throw new BusinessException(HttpStatus.CONFLICT, "NOME_DUPLICADO", dto.nome() + " já existe no cardápio");
        }

        Item item = new Item(nomeSanitizado, dto.preco(), true);
        return repository.save(item);
    }

    @Transactional
    public Item atualizar(Long id, ItemUpdateRequestDTO dto) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "ITEM_NAO_ENCONTRADO", "Item não encontrado."));

        if (dto.preco() != null) {
            validarVariacaoPreco(item.getPreco(), dto.preco());
            item.setPreco(dto.preco());
        }

        if (dto.nome() != null && !dto.nome().isBlank()) {
            String nomeSanitizado = dto.nome().strip();

            if (!item.getNome().equalsIgnoreCase(nomeSanitizado)) {
                if (repository.existsByNomeIgnoreCaseAndIdNot(nomeSanitizado, id)) {
                    throw new BusinessException(HttpStatus.CONFLICT, "NOME_DUPLICADO", nomeSanitizado + " Já existe este item no cardápio");
                }
                item.setNome(nomeSanitizado);
            }
        }

        return item;
}

private void validarVariacaoPreco(BigDecimal precoAtual, BigDecimal precoNovo) {
    BigDecimal limiteMaximo = precoAtual.multiply(DOBRO);
    BigDecimal limiteMinimo = precoAtual.multiply(METADE);

    if (precoNovo.compareTo(limiteMaximo) > 0 || precoNovo.compareTo(limiteMinimo) < 0) {
        throw new BusinessException(HttpStatus.BAD_REQUEST, "VARIACAO_PRECO_INVALIDA", "A atualização de preço não pode passar do dobro nem da metade do valor atual");
    }
}
}
