package com.steamclone.model;

import java.io.Serializable;

/**
 * Item de um pedido: associa um jogo à compra.
 */
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Jogo jogo;
    private int quantidade;

    public ItemPedido(Jogo jogo, int quantidade) {
        this.jogo = jogo;
        this.quantidade = quantidade;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return jogo.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return quantidade + "x " + jogo.getTitulo() + " - R$ " + String.format("%.2f", getSubtotal());
    }
}
