package br.com.cantinaja.carteira.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carteiras")
public class Carteira {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "carteiras")
    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "saldo", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldo;
}
