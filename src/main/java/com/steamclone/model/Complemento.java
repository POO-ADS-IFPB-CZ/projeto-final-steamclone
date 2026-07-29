package com.steamclone.model;

import com.steamclone.model.enums.TipoComplemento;

/**
 * Conteúdo adicional digital de um jogo (DLC, expansão, etc.).
 */
public class Complemento {

    private int idComplemento;
    private String nome;
    private double preco;
    private TipoComplemento tipo;
    private Jogo jogo;

    public Complemento(int idComplemento, String nome, double preco, TipoComplemento tipo) {
        this.idComplemento = idComplemento;
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
    }

    public int getIdComplemento() {
        return idComplemento;
    }

    public void setIdComplemento(int idComplemento) {
        this.idComplemento = idComplemento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public TipoComplemento getTipo() {
        return tipo;
    }

    public void setTipo(TipoComplemento tipo) {
        this.tipo = tipo;
    }

    public Jogo getJogo() {
        return jogo;
    }

    void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    @Override
    public String toString() {
        return nome + " [" + tipo + "] - R$ " + String.format("%.2f", preco);
    }
}
