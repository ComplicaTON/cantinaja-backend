package br.com.cantinaja.carteira.dto;

import java.math.BigDecimal;

public class DebitoRequest {

    private BigDecimal valor;

    // Construtor vazio
    public DebitoRequest() {
    }

    // Construtor com campo
    public DebitoRequest(BigDecimal valor) {
        this.valor = valor;
    }

    // Getter e Setter
    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}