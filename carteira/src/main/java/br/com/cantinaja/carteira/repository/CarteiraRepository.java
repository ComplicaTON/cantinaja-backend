package br.com.cantinaja.carteira.repository;

import br.com.cantinaja.carteira.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    Optional<Carteira> findByAlunoId(Long alunoId);
}
