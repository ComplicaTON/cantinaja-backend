package br.com.cantinaja.carteira.repository;

import br.com.cantinaja.carteira.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import br.com.cantinaja.carteira.service.CarteiraService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cantinaja.carteira.model.Carteira;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    Optional<Carteira> findByAlunoId(Long alunoId);
}