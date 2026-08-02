package br.com.cantinaja.carteira.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transacoes_carteira")
public class TransacaoCarteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carteira_id", nullable = false)
    private Long carteiraId;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    // 1. Construtor Vazio REAL (Sem nenhum parâmetro entre os parênteses)
    public TransacaoCarteira() {
    }

    // 2. Construtor com os parâmetros exatos que você usa no Service
    public TransacaoCarteira(
            Long carteiraId,
            String tipo,
            BigDecimal valor,
            LocalDateTime dataHora
    ) {
        this.carteiraId = carteiraId;
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public Long getCarteiraId() {
        return carteiraId;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}