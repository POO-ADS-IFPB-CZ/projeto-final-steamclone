package com.steamclone.service;

import com.steamclone.model.*;
import com.steamclone.model.enums.StatusPagamento;
import com.steamclone.repository.LojaRepository;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço com as consultas exigidas pelo plano de negócio da loja.
 */
public class ConsultaService {

    private final LojaRepository repository;

    public ConsultaService(LojaRepository repository) {
        this.repository = repository;
    }

    /**
     * Mostrar os detalhes de um pedido específico, incluindo o nome do cliente
     * e o status do pagamento.
     */
    public Optional<String> detalhesPedido(int idPedido) {
        return repository.buscarPedidoPorId(idPedido).map(pedido -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Detalhes do Pedido #").append(pedido.getIdPedido()).append(" ===\n");
            sb.append("Cliente: ").append(pedido.getCliente().getNome()).append("\n");
            sb.append("Data: ").append(pedido.getDataPedido()).append("\n");
            sb.append("Status do Pedido: ").append(pedido.getStatus()).append("\n");
            sb.append("Valor Total: R$ ").append(String.format("%.2f", pedido.getValorTotal())).append("\n");

            if (pedido.getPagamento() != null) {
                Pagamento pag = pedido.getPagamento();
                sb.append("Status do Pagamento: ").append(pag.getStatus()).append("\n");
                sb.append("Método: ").append(pag.getMetodo()).append("\n");
            } else {
                sb.append("Status do Pagamento: N/A\n");
            }

            sb.append("\nItens:\n");
            for (ItemPedido item : pedido.getItens()) {
                sb.append("  - ").append(item).append("\n");
            }
            return sb.toString();
        });
    }

    /**
     * Listar os clientes que compraram um jogo específico.
     */
    public List<Cliente> clientesQueCompraramJogo(String tituloJogo) {
        return repository.getPedidos().stream()
                .flatMap(pedido -> pedido.getItens().stream()
                        .filter(item -> item.getJogo().getTitulo().equalsIgnoreCase(tituloJogo))
                        .map(item -> pedido.getCliente()))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Calcular o total de vendas (receita) da loja em um determinado mês.
     */
    public double totalVendasPorMes(int ano, int mes) {
        YearMonth periodo = YearMonth.of(ano, mes);
        return repository.getPedidos().stream()
                .filter(p -> YearMonth.from(p.getDataPedido()).equals(periodo))
                .filter(p -> p.getPagamento() != null && p.getPagamento().getStatus() == StatusPagamento.PAGO)
                .mapToDouble(Pedido::getValorTotal)
                .sum();
    }

    /**
     * Listar os jogos compatíveis com uma plataforma específica.
     */
    public List<Jogo> jogosPorPlataforma(String nomePlataforma) {
        return repository.buscarPlataformaPorNome(nomePlataforma)
                .map(plataforma -> List.copyOf(plataforma.getJogos()))
                .orElse(List.of());
    }

    /**
     * Mostrar a lista de desenvolvedores ativos ordenados por salário.
     */
    public List<Desenvolvedor> desenvolvedoresAtivosPorSalario() {
        return repository.getDesenvolvedores().stream()
                .filter(Desenvolvedor::isAtivo)
                .sorted(Comparator.comparingDouble(Desenvolvedor::getSalario).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Encontrar o jogo com a maior média de notas nas avaliações.
     */
    public Optional<Jogo> jogoComMaiorMediaNotas() {
        return repository.getJogos().stream()
                .filter(j -> !j.getAvaliacoes().isEmpty())
                .max(Comparator.comparingDouble(Jogo::getMediaNotas));
    }

    /**
     * Listar os pagamentos pendentes aguardando compensação.
     */
    public List<Pagamento> pagamentosPendentes() {
        return repository.getPagamentos().stream()
                .filter(p -> p.getStatus() == StatusPagamento.PENDENTE)
                .collect(Collectors.toList());
    }

    /**
     * Consultar o histórico de pedidos de um cliente específico informando o seu CPF.
     */
    public List<Pedido> historicoPedidosPorCpf(String cpf) {
        return repository.buscarClientePorCpf(cpf)
                .map(cliente -> List.copyOf(cliente.getPedidos()))
                .orElse(List.of());
    }

    /**
     * Contar quantos jogos uma desenvolvedora específica possui registrados na loja.
     */
    public int contarJogosPorDesenvolvedora(String nomeDesenvolvedora) {
        return repository.buscarDesenvolvedoraPorNome(nomeDesenvolvedora)
                .map(d -> d.getJogos().size())
                .orElse(0);
    }

    /**
     * Listar os acessórios disponíveis para um determinado jogo.
     */
    public List<Acessorio> acessoriosPorJogo(String tituloJogo) {
        return repository.buscarJogoPorTitulo(tituloJogo)
                .map(jogo -> List.copyOf(jogo.getAcessorios()))
                .orElse(List.of());
    }
}
