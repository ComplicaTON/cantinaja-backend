package br.com.cantinaja.cardapio.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sugestoes")
public class Sugestao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", length = 140)
    private String descricao;

    @Column(name = "aluno_id")
    private Long alunoId;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;


    public Sugestao() {}

    public Sugestao(Long id, String descricao, Long alunoId, LocalDateTime dataHora) {
        this.id = id;
        this.descricao = descricao;
        this.alunoId = alunoId;
        this.dataHora = dataHora;
    }

    public Sugestao(String descricao, Long alunoId, LocalDateTime dataHora) {
        this.descricao = descricao;
        this.alunoId = alunoId;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Long getAlunoId() {
        return alunoId;
    }


    public LocalDateTime getDataHora() {
        return dataHora;
    }

}
