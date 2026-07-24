package br.com.cantinaja.carteira.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cantinaja.carteira.model.TipoTransacao;
import br.com.cantinaja.carteira.model.TransacaoCarteira;

public interface TransacaoRepository extends JpaRepository<TransacaoCarteira, Long> {
    
    List<TransacaoCarteira> findByCarteiraIdOrderByDataHoraDesc(Long carteiraId);
    
    List<TransacaoCarteira> findByCarteiraIdAndTipoOrderByDataHoraDesc(Long carteiraId, TipoTransacao tipo);
    
}
