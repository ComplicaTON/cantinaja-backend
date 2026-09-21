package br.com.cantinaja.carteira.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
import br.com.cantinaja.carteira.dto.TransacaoResponseDTO;
import br.com.cantinaja.carteira.model.Carteira;
import br.com.cantinaja.carteira.model.TipoTransacao;
import br.com.cantinaja.carteira.model.TransacaoCarteira;
import br.com.cantinaja.carteira.repository.CarteiraRepository;
import br.com.cantinaja.carteira.repository.TransacaoRepository;
import br.com.cantinaja.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarteiraService {

    private static final BigDecimal RECARGA_MIN = new BigDecimal("5.00");
    private static final BigDecimal RECARGA_MAX = new BigDecimal("500.00");
    private static final BigDecimal SALDO_BAIXO_LIMITE = new BigDecimal("10.00");

    private final CarteiraRepository carteiraRepository;
    private final TransacaoRepository transacaoRepository;

    public CarteiraService(CarteiraRepository carteiraRepository, TransacaoRepository transacaoRepository) {
        this.carteiraRepository = carteiraRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public void debitar(Long alunoId, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("DADO_INVALIDO");
    public CarteiraResponseDTO recarregar(Long alunoId, RecargaRequestDTO request) {
        BigDecimal valor = request.valor();

        if (valor.compareTo(RECARGA_MIN) < 0 || valor.compareTo(RECARGA_MAX) > 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "RECARGA_FORA_DA_FAIXA",
                    "A recarga deve estar entre R$ 5,00 e R$ 500,00");
        }

        Carteira carteira = carteiraRepository.findByAlunoId(alunoId)
                .orElseThrow(() -> new RuntimeException("CARTEIRA_NAO_ENCONTRADA"));
                .orElseGet(() -> new Carteira(alunoId, BigDecimal.ZERO));

        carteira.setSaldo(carteira.getSaldo().add(valor));
        carteira = carteiraRepository.save(carteira);

        transacaoRepository.save(new TransacaoCarteira(carteira, TipoTransacao.RECARGA, valor));

        if (carteira.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("SALDO_INSUFICIENTE");
        }
        return montarResponse(carteira);
    }

        carteira.setSaldo(carteira.getSaldo().subtract(valor));
        carteiraRepository.save(carteira);
    public CarteiraResponseDTO consultar(Long alunoId) {
        BigDecimal saldo = carteiraRepository.findByAlunoId(alunoId)
                .map(Carteira::getSaldo)
                .orElse(BigDecimal.ZERO);


        TransacaoCarteira transacao = new TransacaoCarteira(
                carteira.getId(),
                "DEBITO",
                valor,
                LocalDateTime.now()
        );
        boolean saldoBaixo = saldo.compareTo(SALDO_BAIXO_LIMITE) < 0;
        return new CarteiraResponseDTO(alunoId, saldo, saldoBaixo);
    }

    private CarteiraResponseDTO montarResponse(Carteira carteira) {
        return new CarteiraResponseDTO(carteira.getAlunoId(), carteira.getSaldo(), false);
    }
}


    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> consultarTransacoes(Long alunoId, TipoTransacao tipo){
        Carteira carteira = carteiraRepository.findByAlunoId(alunoId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "CARTEIRA_NAO_ENCONTRADA", "Carteira não encontrada"));

        List<TransacaoCarteira> transacoes;
        if (tipo != null) {
            transacoes = transacaoRepository.findByCarteiraIdAndTipoOrderByDataHoraDesc(carteira.getId(), tipo);
        } else {
            transacoes = transacaoRepository.findByCarteiraIdOrderByDataHoraDesc(carteira.getId());
        }

        return transacoes.stream()
                .map(TransacaoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
};