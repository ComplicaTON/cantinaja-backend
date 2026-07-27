package br.com.cantinaja.carteira.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cantinaja.carteira.model.Carteira;
import br.com.cantinaja.carteira.model.TransacaoCarteira;
import br.com.cantinaja.carteira.repository.CarteiraRepository;

@Service
public class CarteiraService {

    private final CarteiraRepository carteiraRepository;

    public CarteiraService(CarteiraRepository carteiraRepository) {
        this.carteiraRepository = carteiraRepository;
    }

    @Transactional
    public void debitar(Long alunoId, BigDecimal valor) {
        // Regra 1: Valor não pode ser menor ou igual a zero
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("DADO_INVALIDO");
        }

        // Regra 2: Buscar carteira pelo alunoId
        Carteira carteira = carteiraRepository.findByAlunoId(alunoId)
                .orElseThrow(() -> new RuntimeException("CARTEIRA_NAO_ENCONTRADA"));

        // Regra 3: Saldo não pode ser menor que o valor a ser debitado
        if (carteira.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("SALDO_INSUFICIENTE");
        }

        // Regra 4: Debitar valor do saldo
        carteira.setSaldo(carteira.getSaldo().subtract(valor));
        carteiraRepository.save(carteira);

        // Regra 5: Criar e registrar a TransacaoCarteira
        TransacaoCarteira transacao = new TransacaoCarteira(
                carteira.getId(),
                "DEBITO",
                "Débito de compra na cantina",
                valor,
                LocalDateTime.now()
        );
        // Aqui o registro da transação seria salvo em seu próprio repositório se necessário
    }
}

