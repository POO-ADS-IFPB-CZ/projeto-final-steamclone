package com.steamclone.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sistema ou console onde o jogo roda.
 */
public class Plataforma implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPlataforma;
    private String nome;
    private LocalDate dataLancamento;
    private Desenvolvedora fabricante;
    private final List<Jogo> jogos = new ArrayList<>();

    public Plataforma(int idPlataforma, String nome, LocalDate dataLancamento) {
        this.idPlataforma = idPlataforma;
        this.nome = nome;
        this.dataLancamento = dataLancamento;
    }

    public int getIdPlataforma() {
        return idPlataforma;
    }

    public void setIdPlataforma(int idPlataforma) {
        this.idPlataforma = idPlataforma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Desenvolvedora getFabricante() {
        return fabricante;
    }

    void setFabricante(Desenvolvedora fabricante) {
        this.fabricante = fabricante;
    }

    public List<Jogo> getJogos() {
        return Collections.unmodifiableList(jogos);
    }

    void adicionarJogo(Jogo jogo) {
        if (!jogos.contains(jogo)) {
            jogos.add(jogo);
        }
    }

    @Override
    public String toString() {
        return nome + (fabricante != null ? " (" + fabricante.getNome() + ")" : "");
    }
}
