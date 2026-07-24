package br.com.cantinaja.carteira.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "carteiras")
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "saldo", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldo;

    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL)
    private List<TransacaoCarteira> transacoes = new ArrayList<>();

    protected Carteira() {
        // Construtor protegido para uso do JPA
    }

    public Carteira(Long alunoId, BigDecimal saldo) {
        this.alunoId = alunoId;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public List<TransacaoCarteira> getTransacoes() {
        return transacoes;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
