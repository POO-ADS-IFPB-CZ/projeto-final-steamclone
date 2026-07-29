package com.steamclone.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estúdio criador de jogos e fabricante de plataformas.
 */
public class Desenvolvedora implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cnpj;
    private String nome;
    private String pais;
    private final List<Jogo> jogos = new ArrayList<>();
    private final List<Desenvolvedor> desenvolvedores = new ArrayList<>();
    private final List<Plataforma> plataformasFabricadas = new ArrayList<>();

    public Desenvolvedora(String cnpj, String nome, String pais) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.pais = pais;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Jogo> getJogos() {
        return Collections.unmodifiableList(jogos);
    }

    public void adicionarJogo(Jogo jogo) {
        if (!jogos.contains(jogo)) {
            jogos.add(jogo);
            jogo.setDesenvolvedora(this);
        }
    }

    public List<Desenvolvedor> getDesenvolvedores() {
        return Collections.unmodifiableList(desenvolvedores);
    }

    void adicionarDesenvolvedor(Desenvolvedor desenvolvedor) {
        if (!desenvolvedores.contains(desenvolvedor)) {
            desenvolvedores.add(desenvolvedor);
        }
    }

    public List<Plataforma> getPlataformasFabricadas() {
        return Collections.unmodifiableList(plataformasFabricadas);
    }

    public void adicionarPlataforma(Plataforma plataforma) {
        if (!plataformasFabricadas.contains(plataforma)) {
            plataformasFabricadas.add(plataforma);
            plataforma.setFabricante(this);
        }
    }

    @Override
    public String toString() {
        return nome + " (" + pais + ")";
    }
}
