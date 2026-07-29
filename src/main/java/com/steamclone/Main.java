package com.steamclone;

import com.steamclone.data.DadosExemplo;
import com.steamclone.model.*;
import com.steamclone.repository.LojaRepository;
import com.steamclone.service.ConsultaService;

import java.util.List;
import java.util.Scanner;

/**
 * Aplicação principal — Clone da loja Steam (trabalho de POO).
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static ConsultaService consultaService;

    public static void main(String[] args) {
        LojaRepository repository = new LojaRepository();
        DadosExemplo.carregar(repository);
        consultaService = new ConsultaService(repository);

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     STEAM CLONE — Loja de Jogos (POO)    ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = lerInt("Escolha uma opção: ");
            System.out.println();

            switch (opcao) {
                case 1 -> consultarDetalhesPedido();
                case 2 -> listarClientesPorJogo();
                case 3 -> calcularVendasMes();
                case 4 -> listarJogosPorPlataforma();
                case 5 -> listarDesenvolvedoresAtivos();
                case 6 -> jogoMaiorMediaNotas();
                case 7 -> listarPagamentosPendentes();
                case 8 -> historicoPedidosCliente();
                case 9 -> contarJogosDesenvolvedora();
                case 10 -> listarAcessoriosPorJogo();
                case 0 -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    executando = false;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        }
    }

    private static void exibirMenu() {
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│              CONSULTAS (2.2)             │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Detalhes de um pedido                │");
        System.out.println("│  2. Clientes que compraram um jogo       │");
        System.out.println("│  3. Total de vendas por mês              │");
        System.out.println("│  4. Jogos por plataforma                 │");
        System.out.println("│  5. Desenvolvedores ativos por salário   │");
        System.out.println("│  6. Jogo com maior média de notas        │");
        System.out.println("│  7. Pagamentos pendentes                 │");
        System.out.println("│  8. Histórico de pedidos por CPF         │");
        System.out.println("│  9. Jogos de uma desenvolvedora          │");
        System.out.println("│ 10. Acessórios de um jogo                │");
        System.out.println("│  0. Sair                                 │");
        System.out.println("└──────────────────────────────────────────┘");
    }

    private static void consultarDetalhesPedido() {
        int id = lerInt("Informe o ID do pedido: ");
        consultaService.detalhesPedido(id)
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("Pedido não encontrado."));
    }

    private static void listarClientesPorJogo() {
        String titulo = lerTexto("Informe o título do jogo: ");
        List<Cliente> clientes = consultaService.clientesQueCompraramJogo(titulo);
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado para o jogo \"" + titulo + "\".");
        } else {
            System.out.println("Clientes que compraram \"" + titulo + "\":");
            clientes.forEach(c -> System.out.println("  - " + c));
        }
    }

    private static void calcularVendasMes() {
        int ano = lerInt("Informe o ano (ex: 2025): ");
        int mes = lerInt("Informe o mês (1-12): ");
        double total = consultaService.totalVendasPorMes(ano, mes);
        System.out.printf("Receita total em %02d/%d: R$ %.2f%n", mes, ano, total);
    }

    private static void listarJogosPorPlataforma() {
        String plataforma = lerTexto("Informe o nome da plataforma: ");
        List<Jogo> jogos = consultaService.jogosPorPlataforma(plataforma);
        if (jogos.isEmpty()) {
            System.out.println("Nenhum jogo encontrado para a plataforma \"" + plataforma + "\".");
        } else {
            System.out.println("Jogos compatíveis com \"" + plataforma + "\":");
            jogos.forEach(j -> System.out.println("  - " + j));
        }
    }

    private static void listarDesenvolvedoresAtivos() {
        List<Desenvolvedor> devs = consultaService.desenvolvedoresAtivosPorSalario();
        if (devs.isEmpty()) {
            System.out.println("Nenhum desenvolvedor ativo encontrado.");
        } else {
            System.out.println("Desenvolvedores ativos (ordenados por salário):");
            devs.forEach(d -> System.out.println("  - " + d));
        }
    }

    private static void jogoMaiorMediaNotas() {
        consultaService.jogoComMaiorMediaNotas()
                .ifPresentOrElse(
                        j -> System.out.printf("Jogo com maior média: %s (%.1f/10)%n",
                                j.getTitulo(), j.getMediaNotas()),
                        () -> System.out.println("Nenhum jogo com avaliações encontrado."));
    }

    private static void listarPagamentosPendentes() {
        List<Pagamento> pendentes = consultaService.pagamentosPendentes();
        if (pendentes.isEmpty()) {
            System.out.println("Nenhum pagamento pendente.");
        } else {
            System.out.println("Pagamentos pendentes:");
            pendentes.forEach(p -> System.out.println("  - " + p));
        }
    }

    private static void historicoPedidosCliente() {
        String cpf = lerTexto("Informe o CPF do cliente: ");
        List<Pedido> pedidos = consultaService.historicoPedidosPorCpf(cpf);
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para o CPF informado.");
        } else {
            System.out.println("Histórico de pedidos:");
            pedidos.forEach(p -> System.out.println("  - " + p));
        }
    }

    private static void contarJogosDesenvolvedora() {
        String nome = lerTexto("Informe o nome da desenvolvedora: ");
        int total = consultaService.contarJogosPorDesenvolvedora(nome);
        if (total == 0) {
            System.out.println("Desenvolvedora não encontrada ou sem jogos registrados.");
        } else {
            System.out.println("A desenvolvedora \"" + nome + "\" possui " + total + " jogo(s) registrado(s).");
        }
    }

    private static void listarAcessoriosPorJogo() {
        String titulo = lerTexto("Informe o título do jogo: ");
        List<Acessorio> acessorios = consultaService.acessoriosPorJogo(titulo);
        if (acessorios.isEmpty()) {
            System.out.println("Nenhum acessório encontrado para o jogo \"" + titulo + "\".");
        } else {
            System.out.println("Acessórios disponíveis para \"" + titulo + "\":");
            acessorios.forEach(a -> System.out.println("  - " + a));
        }
    }

    private static int lerInt(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Valor inválido. " + mensagem);
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
}
