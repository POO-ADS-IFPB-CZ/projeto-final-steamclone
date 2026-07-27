package com.steamclone.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Avaliação de um jogo feita por um cliente.
 */
public class Avaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private Cliente cliente;
    private Jogo jogo;
    private int nota;
    private LocalDate dataAvaliacao;
    private String comentario;

    public Avaliacao(Cliente cliente, Jogo jogo, int nota, LocalDate dataAvaliacao, String comentario) {
        this.cliente = cliente;
        this.jogo = jogo;
        this.nota = nota;
        this.dataAvaliacao = dataAvaliacao;
        this.comentario = comentario;
        cliente.adicionarAvaliacao(this);
        jogo.adicionarAvaliacao(this);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    @Override
    public String toString() {
        return cliente.getNickname() + " avaliou " + jogo.getTitulo() + " com nota " + nota;
    }
}
