package br.com.cantinaja.cardapio.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", unique = true, length = 100)
    private String nome;
    @Column(name = "preco", precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(name = "disponivel", columnDefinition = "boolean default true")
    private Boolean disponivel;

    public Item() {
    }

    public Item(String nome, BigDecimal preco, Boolean disponivel) {
        this.nome = nome;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    public Item(Long id, String nome, BigDecimal preco, Boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }
}
