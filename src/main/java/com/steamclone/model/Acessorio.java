package com.steamclone.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Acessório físico ou digital associado a jogos.
 */
public class Acessorio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idAcessorio;
    private String plataforma;
    private String nome;
    private String descricao;
    private double preco;
    private String fabricante;
    private final List<Jogo> jogos = new ArrayList<>();

    public Acessorio(int idAcessorio, String plataforma, String nome, String descricao,
                     double preco, String fabricante) {
        this.idAcessorio = idAcessorio;
        this.plataforma = plataforma;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.fabricante = fabricante;
    }

    public int getIdAcessorio() {
        return idAcessorio;
    }

    public void setIdAcessorio(int idAcessorio) {
        this.idAcessorio = idAcessorio;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
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
        return nome + " - R$ " + String.format("%.2f", preco) + " (" + fabricante + ")";
    }
}
