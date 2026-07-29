package com.steamclone.repository;

import com.steamclone.data.DadosExemplo;
import com.steamclone.model.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repositório em memória para persistência dos dados da loja.
 */
public class LojaRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Path STORAGE_PATH = Paths.get("store-data.bin");

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

    public boolean removerCliente(Cliente cliente) {
        return clientes.remove(cliente);
    }

    public void adicionarDesenvolvedor(Desenvolvedor desenvolvedor) {
        desenvolvedores.add(desenvolvedor);
    }

    public boolean removerDesenvolvedor(Desenvolvedor desenvolvedor) {
        return desenvolvedores.remove(desenvolvedor);
    }

    public void adicionarDesenvolvedora(Desenvolvedora desenvolvedora) {
        desenvolvedoras.add(desenvolvedora);
    }

    public boolean removerDesenvolvedora(Desenvolvedora desenvolvedora) {
        return desenvolvedoras.remove(desenvolvedora);
    }

    public void adicionarJogo(Jogo jogo) {
        jogos.add(jogo);
    }

    public boolean removerJogo(Jogo jogo) {
        return jogos.remove(jogo);
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

    public boolean removerPedido(Pedido pedido) {
        return pedidos.remove(pedido);
    }

    public void adicionarPagamento(Pagamento pagamento) {
        pagamentos.add(pagamento);
    }

    public boolean removerPagamento(Pagamento pagamento) {
        return pagamentos.remove(pagamento);
    }

    public void adicionarComplemento(Complemento complemento) {
        complementos.add(complemento);
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public boolean atualizarCliente(Cliente cliente) {
        return buscarClientePorCpf(cliente.getCpf())
                .map(existing -> {
                    existing.setNome(cliente.getNome());
                    existing.setEmail(cliente.getEmail());
                    existing.setDataNascimento(cliente.getDataNascimento());
                    existing.setNickname(cliente.getNickname());
                    existing.setDataCadastro(cliente.getDataCadastro());
                    return true;
                })
                .orElse(false);
    }

    public boolean atualizarJogo(Jogo jogo) {
        return buscarJogoPorTitulo(jogo.getTitulo())
                .map(existing -> {
                    existing.setPreco(jogo.getPreco());
                    existing.setDataLancamento(jogo.getDataLancamento());
                    return true;
                })
                .orElse(false);
    }

    public boolean atualizarPedido(Pedido pedido) {
        return buscarPedidoPorId(pedido.getIdPedido())
                .map(existing -> {
                    existing.setStatus(pedido.getStatus());
                    existing.setPagamento(pedido.getPagamento());
                    return true;
                })
                .orElse(false);
    }

    public boolean atualizarPagamento(Pagamento pagamento) {
        for (int i = 0; i < pagamentos.size(); i++) {
            if (pagamentos.get(i).getIdPagamento() == pagamento.getIdPagamento()) {
                pagamentos.set(i, pagamento);
                return true;
            }
        }
        return false;
    }

    public boolean salvar() {
        try {
            Files.createDirectories(STORAGE_PATH.getParent() == null ? Paths.get(".") : STORAGE_PATH.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(STORAGE_PATH)))) {
                out.writeObject(this);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LojaRepository carregar() {
        if (Files.exists(STORAGE_PATH)) {
            try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(STORAGE_PATH)))) {
                return (LojaRepository) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        LojaRepository repo = new LojaRepository();
        DadosExemplo.carregar(repo);
        repo.salvar();
        return repo;
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

    public Optional<Cliente> buscarClientePorNickname(String nickname) {
        return clientes.stream()
                .filter(c -> c.getNickname() != null && c.getNickname().equalsIgnoreCase(nickname))
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
