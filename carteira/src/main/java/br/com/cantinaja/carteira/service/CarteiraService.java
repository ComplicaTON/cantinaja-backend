package br.com.cantinaja.carteira.service;

import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
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

@Service
public class CarteiraService {

    private static final BigDecimal RECARGA_MIN = new BigDecimal("5.00");
    private static final BigDecimal RECARGA_MAX = new BigDecimal("500.00");

    private final CarteiraRepository carteiraRepository;
    private final TransacaoRepository transacaoRepository;

    public CarteiraService(CarteiraRepository carteiraRepository, TransacaoRepository transacaoRepository) {
        this.carteiraRepository = carteiraRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public CarteiraResponseDTO recarregar(Long alunoId, RecargaRequestDTO request) {
        BigDecimal valor = request.valor();

        if (valor.compareTo(RECARGA_MIN) < 0 || valor.compareTo(RECARGA_MAX) > 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "RECARGA_FORA_DA_FAIXA",
                    "A recarga deve estar entre R$ 5,00 e R$ 500,00");
        }

        Carteira carteira = carteiraRepository.findByAlunoId(alunoId)
                .orElseGet(() -> new Carteira(alunoId, BigDecimal.ZERO));

        carteira.setSaldo(carteira.getSaldo().add(valor));
        carteira = carteiraRepository.save(carteira);

        transacaoRepository.save(new TransacaoCarteira(carteira, TipoTransacao.RECARGA, valor));

        return montarResponse(carteira);
    }

    private CarteiraResponseDTO montarResponse(Carteira carteira) {
        return new CarteiraResponseDTO(carteira.getAlunoId(), carteira.getSaldo(), false);
    }
}