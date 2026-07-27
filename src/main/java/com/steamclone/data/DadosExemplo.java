package com.steamclone.data;

import com.steamclone.model.*;
import com.steamclone.model.enums.*;
import com.steamclone.repository.LojaRepository;

import java.time.LocalDate;

/**
 * Popula o repositório com dados de exemplo para demonstração das consultas.
 */
public class DadosExemplo {

    public static void carregar(LojaRepository repo) {
        // Desenvolvedoras
        Desenvolvedora thatgamecompany = new Desenvolvedora("12.345.678/0001-90", "thatgamecompany", "EUA");
        Desenvolvedora valve = new Desenvolvedora("98.765.432/0001-10", "Valve Corporation", "EUA");
        Desenvolvedora sony = new Desenvolvedora("11.222.333/0001-44", "Sony Interactive Entertainment", "Japão");
        Desenvolvedora nintendo = new Desenvolvedora("55.666.777/0001-88", "Nintendo", "Japão");

        repo.adicionarDesenvolvedora(thatgamecompany);
        repo.adicionarDesenvolvedora(valve);
        repo.adicionarDesenvolvedora(sony);
        repo.adicionarDesenvolvedora(nintendo);

        // Desenvolvedores
        Desenvolvedor dev1 = new Desenvolvedor("Jenova Chen", "111.111.111-11", "jenova@tgc.com",
                LocalDate.of(1978, 10, 1), "DEV001", "Diretor Criativo", 25000.0);
        Desenvolvedor dev2 = new Desenvolvedor("Gabe Newell", "222.222.222-22", "gabe@valve.com",
                LocalDate.of(1962, 11, 3), "DEV002", "CEO", 50000.0);
        Desenvolvedor dev3 = new Desenvolvedor("Carlos Silva", "333.333.333-33", "carlos@sony.com",
                LocalDate.of(1990, 5, 15), "DEV003", "Programador", 8000.0);
        Desenvolvedor dev4 = new Desenvolvedor("Ana Inativa", "444.444.444-44", "ana@valve.com",
                LocalDate.of(1985, 3, 20), "DEV004", "Suporte", 5000.0);
        dev4.setAtivo(false);

        dev1.setDesenvolvedora(thatgamecompany);
        dev2.setDesenvolvedora(valve);
        dev3.setDesenvolvedora(sony);
        dev4.setDesenvolvedora(valve);

        repo.adicionarDesenvolvedor(dev1);
        repo.adicionarDesenvolvedor(dev2);
        repo.adicionarDesenvolvedor(dev3);
        repo.adicionarDesenvolvedor(dev4);

        // Plataformas
        Plataforma pc = new Plataforma(1, "PC", LocalDate.of(1981, 8, 12));
        Plataforma ps5 = new Plataforma(2, "PlayStation 5", LocalDate.of(2020, 11, 12));
        Plataforma switchPlat = new Plataforma(3, "Nintendo Switch", LocalDate.of(2017, 3, 3));
        Plataforma steamDeck = new Plataforma(4, "Steam Deck", LocalDate.of(2022, 2, 25));

        sony.adicionarPlataforma(ps5);
        nintendo.adicionarPlataforma(switchPlat);
        valve.adicionarPlataforma(steamDeck);

        repo.adicionarPlataforma(pc);
        repo.adicionarPlataforma(ps5);
        repo.adicionarPlataforma(switchPlat);
        repo.adicionarPlataforma(steamDeck);

        // Jogos
        Jogo sky = new Jogo(1, "Sky: Children of the Light", 0.0, LocalDate.of(2019, 7, 18));
        Jogo cs2 = new Jogo(2, "Counter-Strike 2", 0.0, LocalDate.of(2023, 9, 27));
        Jogo eldenRing = new Jogo(3, "Elden Ring", 249.90, LocalDate.of(2022, 2, 25));
        Jogo zelda = new Jogo(4, "The Legend of Zelda: Tears of the Kingdom", 349.90, LocalDate.of(2023, 5, 12));
        Jogo portal2 = new Jogo(5, "Portal 2", 49.90, LocalDate.of(2011, 4, 19));
        Jogo godOfWar = new Jogo(6, "God of War Ragnarök", 299.90, LocalDate.of(2022, 11, 9));
        Jogo animalCrossing = new Jogo(7, "Animal Crossing: New Horizons", 249.90, LocalDate.of(2020, 3, 20));
        Jogo hades = new Jogo(8, "Hades", 89.90, LocalDate.of(2020, 9, 17));

        thatgamecompany.adicionarJogo(sky);
        valve.adicionarJogo(cs2);
        valve.adicionarJogo(portal2);
        sony.adicionarJogo(eldenRing);
        sony.adicionarJogo(godOfWar);
        nintendo.adicionarJogo(zelda);
        nintendo.adicionarJogo(animalCrossing);
        thatgamecompany.adicionarJogo(hades);

        sky.adicionarPlataforma(pc);
        sky.adicionarPlataforma(switchPlat);
        sky.adicionarPlataforma(ps5);
        cs2.adicionarPlataforma(pc);
        cs2.adicionarPlataforma(steamDeck);
        portal2.adicionarPlataforma(pc);
        portal2.adicionarPlataforma(steamDeck);
        eldenRing.adicionarPlataforma(pc);
        eldenRing.adicionarPlataforma(ps5);
        godOfWar.adicionarPlataforma(ps5);
        zelda.adicionarPlataforma(switchPlat);
        animalCrossing.adicionarPlataforma(switchPlat);
        hades.adicionarPlataforma(pc);

        repo.adicionarJogo(sky);
        repo.adicionarJogo(cs2);
        repo.adicionarJogo(eldenRing);
        repo.adicionarJogo(zelda);
        repo.adicionarJogo(portal2);
        repo.adicionarJogo(godOfWar);
        repo.adicionarJogo(animalCrossing);
        repo.adicionarJogo(hades);

        // Complementos
        Complemento dlcSky = new Complemento(1, "Season of Passage", 29.90, TipoComplemento.EXPANSAO);
        Complemento skinCs = new Complemento(2, "Pacote de Skins Neon", 19.90, TipoComplemento.COSMETICO);
        sky.adicionarComplemento(dlcSky);
        cs2.adicionarComplemento(skinCs);
        repo.adicionarComplemento(dlcSky);
        repo.adicionarComplemento(skinCs);

        // Acessórios
        Acessorio volante = new Acessorio(1, "PC/PS5", "Volante Logitech G29",
                "Volante com pedais para simuladores de corrida", 1899.90, "Logitech");
        Acessorio headset = new Acessorio(2, "Multiplataforma", "Headset Gamer 7.1",
                "Headset surround 7.1 com microfone retrátil", 399.90, "HyperX");
        Acessorio controle = new Acessorio(3, "PC", "Controle Xbox Series X",
                "Controle sem fio compatível com PC e Xbox", 449.90, "Microsoft");

        cs2.adicionarAcessorio(headset);
        cs2.adicionarAcessorio(controle);
        eldenRing.adicionarAcessorio(controle);
        sky.adicionarAcessorio(headset);

        repo.adicionarAcessorio(volante);
        repo.adicionarAcessorio(headset);
        repo.adicionarAcessorio(controle);

        // Clientes
        Cliente cliente1 = new Cliente("João Pedro Santos", "555.666.777-88", "joao@email.com",
                LocalDate.of(1995, 4, 10), "joaogamer", LocalDate.of(2024, 1, 15));
        Cliente cliente2 = new Cliente("Maria Oliveira", "666.777.888-99", "maria@email.com",
                LocalDate.of(1998, 8, 22), "mariaplays", LocalDate.of(2024, 3, 20));
        Cliente cliente3 = new Cliente("Lucas Ferreira", "777.888.999-00", "lucas@email.com",
                LocalDate.of(2000, 12, 5), "lucasfps", LocalDate.of(2025, 6, 1));

        repo.adicionarCliente(cliente1);
        repo.adicionarCliente(cliente2);
        repo.adicionarCliente(cliente3);

        // Pedidos e Pagamentos
        Pedido pedido1 = new Pedido(1001, LocalDate.of(2025, 7, 10), StatusPedido.FINALIZADO);
        pedido1.adicionarItem(sky, 1);
        pedido1.adicionarItem(eldenRing, 1);
        Pagamento pag1 = new Pagamento(5001, MetodoPagamento.PIX, StatusPagamento.PAGO, LocalDate.of(2025, 7, 10));
        pedido1.setPagamento(pag1);
        cliente1.adicionarPedido(pedido1);
        repo.adicionarPedido(pedido1);
        repo.adicionarPagamento(pag1);

        Pedido pedido2 = new Pedido(1002, LocalDate.of(2025, 7, 15), StatusPedido.FINALIZADO);
        pedido2.adicionarItem(cs2, 1);
        Pagamento pag2 = new Pagamento(5002, MetodoPagamento.CARTAO, StatusPagamento.PAGO, LocalDate.of(2025, 7, 15));
        pedido2.setPagamento(pag2);
        cliente2.adicionarPedido(pedido2);
        repo.adicionarPedido(pedido2);
        repo.adicionarPagamento(pag2);

        Pedido pedido3 = new Pedido(1003, LocalDate.of(2025, 7, 20), StatusPedido.PENDENTE);
        pedido3.adicionarItem(sky, 1);
        Pagamento pag3 = new Pagamento(5003, MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null);
        pedido3.setPagamento(pag3);
        cliente3.adicionarPedido(pedido3);
        repo.adicionarPedido(pedido3);
        repo.adicionarPagamento(pag3);

        Pedido pedido4 = new Pedido(1004, LocalDate.of(2025, 6, 5), StatusPedido.FINALIZADO);
        pedido4.adicionarItem(zelda, 1);
        Pagamento pag4 = new Pagamento(5004, MetodoPagamento.PIX, StatusPagamento.PAGO, LocalDate.of(2025, 6, 5));
        pedido4.setPagamento(pag4);
        cliente1.adicionarPedido(pedido4);
        repo.adicionarPedido(pedido4);
        repo.adicionarPagamento(pag4);

        // Avaliações
        repo.adicionarAvaliacao(new Avaliacao(cliente1, sky, 10, LocalDate.of(2025, 7, 12), "Obra de arte!"));
        repo.adicionarAvaliacao(new Avaliacao(cliente2, sky, 9, LocalDate.of(2025, 7, 18), "Muito bonito e emocionante"));
        repo.adicionarAvaliacao(new Avaliacao(cliente3, cs2, 8, LocalDate.of(2025, 7, 22), "Competitivo e viciante"));
        repo.adicionarAvaliacao(new Avaliacao(cliente1, eldenRing, 10, LocalDate.of(2025, 7, 11), "Masterpiece"));
        repo.adicionarAvaliacao(new Avaliacao(cliente2, eldenRing, 7, LocalDate.of(2025, 7, 16), "Difícil demais"));
    }
}
