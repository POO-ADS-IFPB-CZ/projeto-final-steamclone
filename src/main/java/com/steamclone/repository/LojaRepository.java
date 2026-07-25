package com.steamclone.repository;

import com.steamclone.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repositório em memória para persistência dos dados da loja.
 */
public class LojaRepository {

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Desenvolvedor> desenvolvedores = new ArrayList<>();
    private final List<Desenvolvedora> desenvolvedoras = new ArrayList<>();
    private final List<Jogo> jogos = new ArrayList<>();
    private final List<Plataforma> plataformas = new ArrayList<>();
    private final List<Acessorio> acessorios = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<Pagamento> pagamentos = new ArrayList<>();
    private final List<Complemento> complementos = new ArrayList<>();
    private final List<Avaliacao> avaliacoes = new ArrayList<>();

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void adicionarDesenvolvedor(Desenvolvedor desenvolvedor) {
        desenvolvedores.add(desenvolvedor);
    }

    public void adicionarDesenvolvedora(Desenvolvedora desenvolvedora) {
        desenvolvedoras.add(desenvolvedora);
    }

    public void adicionarJogo(Jogo jogo) {
        jogos.add(jogo);
    }

    public void adicionarPlataforma(Plataforma plataforma) {
        plataformas.add(plataforma);
    }

    public void adicionarAcessorio(Acessorio acessorio) {
        acessorios.add(acessorio);
    }

    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public void adicionarPagamento(Pagamento pagamento) {
        pagamentos.add(pagamento);
    }

    public void adicionarComplemento(Complemento complemento) {
        complementos.add(complemento);
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public List<Desenvolvedor> getDesenvolvedores() {
        return Collections.unmodifiableList(desenvolvedores);
    }

    public List<Desenvolvedora> getDesenvolvedoras() {
        return Collections.unmodifiableList(desenvolvedoras);
    }

    public List<Jogo> getJogos() {
        return Collections.unmodifiableList(jogos);
    }

    public List<Plataforma> getPlataformas() {
        return Collections.unmodifiableList(plataformas);
    }

    public List<Acessorio> getAcessorios() {
        return Collections.unmodifiableList(acessorios);
    }

    public List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public List<Pagamento> getPagamentos() {
        return Collections.unmodifiableList(pagamentos);
    }

    public List<Avaliacao> getAvaliacoes() {
        return Collections.unmodifiableList(avaliacoes);
    }

    public Optional<Pedido> buscarPedidoPorId(int idPedido) {
        return pedidos.stream()
                .filter(p -> p.getIdPedido() == idPedido)
                .findFirst();
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clientes.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst();
    }

    public Optional<Jogo> buscarJogoPorTitulo(String titulo) {
        return jogos.stream()
                .filter(j -> j.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    public Optional<Desenvolvedora> buscarDesenvolvedoraPorNome(String nome) {
        return desenvolvedoras.stream()
                .filter(d -> d.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    public Optional<Plataforma> buscarPlataformaPorNome(String nome) {
        return plataformas.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }
}
