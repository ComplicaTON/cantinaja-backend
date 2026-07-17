package br.com.cantinaja.cardapio.repository;

import br.com.cantinaja.cardapio.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Boolean existsByNomeIgnoreCase(String nome);
}
