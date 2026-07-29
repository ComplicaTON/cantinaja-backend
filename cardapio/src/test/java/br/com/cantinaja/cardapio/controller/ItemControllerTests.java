package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.model.Item;
import br.com.cantinaja.cardapio.service.ItemService;
import br.com.cantinaja.common.exception.BusinessException;
import br.com.cantinaja.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(GlobalExceptionHandler.class)
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService service;

    @Test
    void deveRetornar201ECadastrarOItem() throws Exception {
        when(service.cadastrar(any(ItemRequestDTO.class)))
                .thenReturn(new Item(1L, "Coxinha de Frango", new BigDecimal("10.00"), true));

        mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Coxinha de Frango","preco":10.00}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar400AoCadastrarItemComNomeContendoCaracteresInvalidos() throws Exception {
        mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Coxinha123","preco":10.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoCadastrarItemComNomeVazio() throws Exception {
        mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"","preco":10.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoCadastrarItemComPrecoNegativo() throws Exception {
        mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Coxinha de Frango","preco":-1.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoCadastrarItemSemPreco() throws Exception {
        mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Coxinha de Frango"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409AoCadastrarItemExistente() throws Exception {
        doThrow(new BusinessException(HttpStatus.CONFLICT, "NOME_DUPLICADO", "Coxinha de Frango já existe no cardápio"))
                .when(service).cadastrar(any(ItemRequestDTO.class));

        mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Coxinha de Frango","preco":10.00}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Coxinha de Frango já existe no cardápio"));
    }
}
