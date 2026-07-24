package br.com.cantinaja.carteira.service;

import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
import br.com.cantinaja.carteira.model.Carteira;
import br.com.cantinaja.carteira.model.TipoTransacao;
import br.com.cantinaja.carteira.model.TransacaoCarteira;
import br.com.cantinaja.carteira.repository.CarteiraRepository;
import br.com.cantinaja.carteira.repository.TransacaoRepository;
import br.com.cantinaja.common.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarteiraServiceTests {

    @InjectMocks
    private CarteiraService service;

    @Mock
    private CarteiraRepository carteiraRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Test
    void deveCriarCarteiraNovaERecarregarComSaldoCorreto() {
        Long alunoId = 1L;
        RecargaRequestDTO request = new RecargaRequestDTO(new BigDecimal("50.00"));

        when(carteiraRepository.findByAlunoId(alunoId)).thenReturn(Optional.empty());
        when(carteiraRepository.save(any(Carteira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarteiraResponseDTO response = service.recarregar(alunoId, request);

        assertEquals(alunoId, response.alunoId());
        assertEquals(0, response.saldo().compareTo(new BigDecimal("50.00")));
    }

    @Test
    void deveSomarAoSaldoDeCarteiraJaExistente() {
        Long alunoId = 1L;
        Carteira carteiraExistente = new Carteira(alunoId, new BigDecimal("20.00"));
        RecargaRequestDTO request = new RecargaRequestDTO(new BigDecimal("30.00"));

        when(carteiraRepository.findByAlunoId(alunoId)).thenReturn(Optional.of(carteiraExistente));
        when(carteiraRepository.save(any(Carteira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarteiraResponseDTO response = service.recarregar(alunoId, request);

        assertEquals(0, response.saldo().compareTo(new BigDecimal("50.00")));
    }

    @Test
    void deveLancarExceptionParaRecargaAbaixoDoMinimo() {
        Long alunoId = 1L;
        RecargaRequestDTO request = new RecargaRequestDTO(new BigDecimal("2.00"));

        BusinessException ex = Assertions.assertThrows(BusinessException.class,
                () -> service.recarregar(alunoId, request));

        assertEquals("RECARGA_FORA_DA_FAIXA", ex.getCodigo());
        verify(carteiraRepository, never()).save(any(Carteira.class));
        verify(transacaoRepository, never()).save(any(TransacaoCarteira.class));
    }

    @Test
    void deveLancarExceptionParaRecargaAcimaDoMaximo() {
        Long alunoId = 1L;
        RecargaRequestDTO request = new RecargaRequestDTO(new BigDecimal("600.00"));

        BusinessException ex = Assertions.assertThrows(BusinessException.class,
                () -> service.recarregar(alunoId, request));

        assertEquals("RECARGA_FORA_DA_FAIXA", ex.getCodigo());
        verify(carteiraRepository, never()).save(any(Carteira.class));
        verify(transacaoRepository, never()).save(any(TransacaoCarteira.class));
    }

    @Test
    void deveGerarExatamenteUmaTransacaoDoTipoRecarga() {
        Long alunoId = 1L;
        RecargaRequestDTO request = new RecargaRequestDTO(new BigDecimal("50.00"));

        when(carteiraRepository.findByAlunoId(alunoId)).thenReturn(Optional.empty());
        when(carteiraRepository.save(any(Carteira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.recarregar(alunoId, request);

        ArgumentCaptor<TransacaoCarteira> captor = ArgumentCaptor.forClass(TransacaoCarteira.class);
        verify(transacaoRepository, times(1)).save(captor.capture());

        TransacaoCarteira transacao = captor.getValue();
        assertEquals(TipoTransacao.RECARGA, transacao.getTipo());
        assertEquals(0, transacao.getValor().compareTo(new BigDecimal("50.00")));
    }
}