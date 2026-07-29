package br.com.cantinaja.cardapio.services;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.model.Item;
import br.com.cantinaja.cardapio.repository.ItemRepository;
import br.com.cantinaja.cardapio.service.ItemService;
import br.com.cantinaja.common.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    @InjectMocks
    private ItemService service;

    @Mock
    private ItemRepository repository;

    @Test
    void deveSalvarERetornarOItemSalvo() {
        ItemRequestDTO dto = new ItemRequestDTO("Coxinha de Frango", new BigDecimal("10.00"));
        when(repository.existsByNomeIgnoreCase(dto.nome())).thenReturn(false);
        when(repository.save(any(Item.class))).thenReturn(new Item(1L, dto.nome(), dto.preco(), true));
        Item item = service.cadastrar(dto);
        assertEquals(1L, item.getId().longValue());
        assertEquals(dto.nome(), item.getNome());
        assertEquals(dto.preco(), item.getPreco());
        assertEquals(true, item.getDisponivel());
    }

    @Test
    void deveRetornarExceptionAoCadastrarItemComNomeDuplicado() {
        when(repository.existsByNomeIgnoreCase(any(String.class))).thenReturn(true);
        ItemRequestDTO dto = new ItemRequestDTO("Coxinha de Frango", new BigDecimal("10.00"));
        Assertions.assertThrows(BusinessException.class, () -> service.cadastrar(dto));
    }
}
