package br.com.cantinaja.carteira.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "transacoes_carteira")
public class TransacaoCarteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTransacao tipo;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    protected TransacaoCarteira() {

    }
    public TransacaoCarteira(
        Carteira carteira,
        TipoTransacao tipo,
        BigDecimal valor
    ) {
        this.carteira = carteira;
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Carteira getCarteira() {
        return carteira;
    public Long getCarteiraId() {
        return carteiraId;
    }

    public TipoTransacao getTipo() {
    public String getTipo() {

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