package br.com.cantinaja.carteira.controller;

import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
import br.com.cantinaja.carteira.service.CarteiraService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarteiraController.class)
@Import(GlobalExceptionHandler.class)
public class CarteiraControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarteiraService service;

    @Test
    void deveRetornar201ERecarregarComSucesso() throws Exception {
        when(service.recarregar(anyLong(), any(RecargaRequestDTO.class)))
                .thenReturn(new CarteiraResponseDTO(1L, new BigDecimal("50.00"), false));

        mockMvc.perform(post("/api/v1/carteiras/1/recargas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"valor":50.00}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alunoId").value(1))
                .andExpect(jsonPath("$.saldo").value(50.00))
                .andExpect(jsonPath("$.saldoBaixo").value(false));
    }

    @Test
    void deveRetornar400AoRecarregarComValorAusente() throws Exception {
        mockMvc.perform(post("/api/v1/carteiras/1/recargas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoRecarregarComValorNegativoOuZero() throws Exception {
        mockMvc.perform(post("/api/v1/carteiras/1/recargas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"valor":-10.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoRecarregarForaDaFaixa() throws Exception {
        doThrow(new BusinessException(HttpStatus.BAD_REQUEST, "RECARGA_FORA_DA_FAIXA",
                "A recarga deve estar entre R$ 5,00 e R$ 500,00"))
                .when(service).recarregar(anyLong(), any(RecargaRequestDTO.class));

        mockMvc.perform(post("/api/v1/carteiras/1/recargas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"valor":2.00}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("RECARGA_FORA_DA_FAIXA"))
                .andExpect(jsonPath("$.mensagem").value("A recarga deve estar entre R$ 5,00 e R$ 500,00"));
    }
}