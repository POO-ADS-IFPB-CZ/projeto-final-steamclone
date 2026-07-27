package com.steamclone.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Produto digital à venda na loja.
 */
public class Jogo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idJogo;
    private String titulo;
    private double preco;
    private LocalDate dataLancamento;
    private Desenvolvedora desenvolvedora;
    private final List<Plataforma> plataformas = new ArrayList<>();
    private final List<Acessorio> acessorios = new ArrayList<>();
    private final List<Complemento> complementos = new ArrayList<>();
    private final List<Avaliacao> avaliacoes = new ArrayList<>();
    private String imagemCapa;

    public Jogo(int idJogo, String titulo, double preco, LocalDate dataLancamento) {
        this.idJogo = idJogo;
        this.titulo = titulo;
        this.preco = preco;
        this.dataLancamento = dataLancamento;
    }

    public Jogo(int idJogo, String titulo, double preco, LocalDate dataLancamento, String imagemCapa) {
        this(idJogo, titulo, preco, dataLancamento);
        this.imagemCapa = imagemCapa;
    }

    public int getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Desenvolvedora getDesenvolvedora() {
        return desenvolvedora;
    }

    public void setDesenvolvedora(Desenvolvedora desenvolvedora) {
        this.desenvolvedora = desenvolvedora;
    }

    public List<Plataforma> getPlataformas() {
        return Collections.unmodifiableList(plataformas);
    }

    public void adicionarPlataforma(Plataforma plataforma) {
        if (!plataformas.contains(plataforma)) {
            plataformas.add(plataforma);
            plataforma.adicionarJogo(this);
        }
    }

    public List<Acessorio> getAcessorios() {
        return Collections.unmodifiableList(acessorios);
    }

    public void adicionarAcessorio(Acessorio acessorio) {
        if (!acessorios.contains(acessorio)) {
            acessorios.add(acessorio);
            acessorio.adicionarJogo(this);
        }
    }

    public List<Complemento> getComplementos() {
        return Collections.unmodifiableList(complementos);
    }

    public void adicionarComplemento(Complemento complemento) {
        if (!complementos.contains(complemento)) {
            complementos.add(complemento);
            complemento.setJogo(this);
        }
    }

    public List<Avaliacao> getAvaliacoes() {
        return Collections.unmodifiableList(avaliacoes);
    }

    void adicionarAvaliacao(Avaliacao avaliacao) {
        if (!avaliacoes.contains(avaliacao)) {
            avaliacoes.add(avaliacao);
        }
    }

    public double getMediaNotas() {
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        return avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return titulo + " - R$ " + String.format("%.2f", preco);
    }
}
