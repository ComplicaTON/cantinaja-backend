package br.com.cantinaja.cardapio.repository;

import br.com.cantinaja.cardapio.model.Sugestao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SugestaoRepository extends JpaRepository <Sugestao, Long> {
    Boolean existsByDescricaoIgnoreCase(String descricao);

    List<Sugestao> findAllByOrderByDataHoraDesc();
}


