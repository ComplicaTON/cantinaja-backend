package br.com.cantinaja.carteira.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cantinaja.carteira.model.Carteira;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    // Busca a carteira pelo ID do aluno
    Optional<Carteira> findByAlunoId(Long alunoId);
}