package com.steamclone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pessoa que consome jogos e produtos da loja online.
 */
public class Cliente extends Pessoa {

    private String nickname;
    private LocalDate dataCadastro;
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<Avaliacao> avaliacoes = new ArrayList<>();

    public Cliente(String nome, String cpf, String email, LocalDate dataNascimento,
                   String nickname, LocalDate dataCadastro) {
        super(nome, cpf, email, dataNascimento);
        this.nickname = nickname;
        this.dataCadastro = dataCadastro;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public void adicionarPedido(Pedido pedido) {
        if (!pedidos.contains(pedido)) {
            pedidos.add(pedido);
            pedido.setCliente(this);
        }
    }

    public List<Avaliacao> getAvaliacoes() {
        return Collections.unmodifiableList(avaliacoes);
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        if (!avaliacoes.contains(avaliacao)) {
            avaliacoes.add(avaliacao);
        }
    }

    @Override
    public String getTipo() {
        return "Cliente";
    }

    @Override
    public String toString() {
        return super.toString() + " [@" + nickname + "]";
    }
}
