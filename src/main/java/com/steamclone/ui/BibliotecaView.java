package com.steamclone.ui;

import com.steamclone.model.Jogo;
import com.steamclone.model.Pedido;
import com.steamclone.model.enums.StatusPedido;
import com.steamclone.repository.LojaRepository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BibliotecaView {

    private final LojaRepository repository;
    private final String nickname;
    private final List<Jogo> jogosComprados;

    private FlowPane cardsArea;
    private TextField pesquisaField;
    private ComboBox<String> categoriaCombo;
    private Label infoLabel;

    public BibliotecaView(LojaRepository repository, String nickname) {
        this.repository = repository;
        this.nickname = nickname;
        this.jogosComprados = coletarJogosComprados();
    }

    public ScrollPane criarPainel() {
        VBox biblioteca = new VBox(16);
        biblioteca.setPadding(new Insets(22));
        biblioteca.getChildren().addAll(criarTopo(), criarConteudo());

        ScrollPane scroll = new ScrollPane(biblioteca);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        return scroll;
    }

    private HBox criarTopo() {
        Label titulo = new Label("Biblioteca");
        titulo.getStyleClass().add("section-title");

        Label subtitulo = new Label("Jogos de " + nickname);
        subtitulo.getStyleClass().add("section-subtitle");

        VBox texto = new VBox(4, titulo, subtitulo);

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        infoLabel = new Label(jogosComprados.size() + " jogo(s) na biblioteca");
        infoLabel.getStyleClass().add("result-label");

        HBox topo = new HBox(texto, espacador, infoLabel);
        topo.setAlignment(Pos.CENTER_LEFT);
        return topo;
    }

    private SplitPane criarConteudo() {
        SplitPane split = new SplitPane(criarPainelLateral(), criarPainelPrincipal());
        split.setDividerPositions(0.22);
        return split;
    }

    private VBox criarPainelLateral() {
        VBox lateral = new VBox(10);
        lateral.getStyleClass().add("sidebar");
        lateral.setPrefWidth(240);

        Label titulo = new Label("Página inicial");
        titulo.getStyleClass().add("sidebar-title");

        Button btnLoja = criarBotaoLateral("Loja");
        btnLoja.setOnAction(e -> {
            categoriaCombo.getSelectionModel().selectFirst();
            pesquisaField.clear();
            atualizarCards(jogosComprados);
        });

        Button btnFavoritos = criarBotaoLateral("Favoritos");
        btnFavoritos.setOnAction(e -> atualizarCards(jogosComprados.stream().limit(4).collect(Collectors.toList())));

        Button btnAtualizacoes = criarBotaoLateral("Atualizações");
        btnAtualizacoes.setOnAction(e -> atualizarCards(jogosComprados.stream()
                .filter(j -> j.getPreco() <= 0.0 || j.getDataLancamento().getYear() >= 2023)
                .collect(Collectors.toList())));

        pesquisaField = new TextField();
        pesquisaField.setPromptText("Pesquisar jogos na biblioteca");
        pesquisaField.getStyleClass().add("search-field");
        pesquisaField.textProperty().addListener((obs, oldVal, newVal) -> filtrarBiblioteca());

        Label categoriaLabel = new Label("Categoria");
        categoriaLabel.getStyleClass().add("sidebar-subtitle");

        categoriaCombo = new ComboBox<>();
        categoriaCombo.getItems().addAll("Todos", "PC", "PlayStation 5", "Nintendo Switch");
        categoriaCombo.getSelectionModel().selectFirst();
        categoriaCombo.setMaxWidth(Double.MAX_VALUE);
        categoriaCombo.setOnAction(e -> filtrarBiblioteca());

        VBox favoritosBox = new VBox(4);
        favoritosBox.getChildren().add(criarSubtituloLateral("FAVORITOS"));
        jogosComprados.stream().limit(4)
                .forEach(jogo -> favoritosBox.getChildren().add(criarItemLateral(jogo.getTitulo())));

        VBox todosBox = new VBox(4);
        todosBox.getChildren().add(criarSubtituloLateral("TODOS OS JOGOS"));
        repository.getJogos().stream().limit(6)
                .forEach(jogo -> todosBox.getChildren().add(criarItemLateral(jogo.getTitulo())));

        lateral.getChildren().addAll(
                titulo, btnLoja, btnFavoritos, btnAtualizacoes,
                new Separator(), pesquisaField, categoriaLabel, categoriaCombo,
                new Separator(), favoritosBox, todosBox);
        return lateral;
    }

    private VBox criarPainelPrincipal() {
        Label destaque = new Label("Sua coleção");
        destaque.getStyleClass().add("section-title");

        Label subtitulo = new Label("Aqui estão os jogos que você já possui.");
        subtitulo.getStyleClass().add("section-subtitle");

        VBox cabecalho = new VBox(4, destaque, subtitulo);

        cardsArea = new FlowPane(16, 16);
        atualizarCards(jogosComprados);

        ScrollPane scroll = new ScrollPane(cardsArea);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox principal = new VBox(14, cabecalho, scroll);
        principal.setPadding(new Insets(0, 0, 0, 16));
        return principal;
    }

    private void atualizarCards(List<Jogo> jogos) {
        cardsArea.getChildren().clear();
        if (jogos.isEmpty()) {
            Label vazio = new Label("Nenhum jogo corresponde à busca.");
            vazio.getStyleClass().add("empty-label");
            cardsArea.getChildren().add(vazio);
        } else {
            for (Jogo jogo : jogos) {
                cardsArea.getChildren().add(criarCardBiblioteca(jogo));
            }
        }
        infoLabel.setText(jogos.size() + " jogo(s) na biblioteca");
    }

    private void filtrarBiblioteca() {
        String termo = pesquisaField.getText() == null ? "" : pesquisaField.getText().trim().toLowerCase();
        String categoria = categoriaCombo.getValue() != null ? categoriaCombo.getValue() : "Todos";
        List<Jogo> filtrados = jogosComprados.stream()
                .filter(jogo -> termo.isBlank()
                        || jogo.getTitulo().toLowerCase().contains(termo)
                        || (jogo.getDesenvolvedora() != null && jogo.getDesenvolvedora().getNome().toLowerCase().contains(termo))
                        || jogo.getPlataformas().stream().anyMatch(p -> p.getNome().toLowerCase().contains(termo)))
                .filter(jogo -> categoria.equals("Todos")
                        || jogo.getPlataformas().stream().anyMatch(p -> p.getNome().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
    }

    private VBox criarCardBiblioteca(Jogo jogo) {
        StackPane capa = new StackPane(new Label("CAPA DO JOGO"));
        capa.getStyleClass().add("card-image-placeholder");
        capa.setPrefSize(300, 100);

        Label nome = new Label(jogo.getTitulo());
        nome.getStyleClass().add("card-title");
        nome.setWrapText(true);

        Label info = new Label(jogo.getPlataformas().isEmpty() ? "" : jogo.getPlataformas().get(0).getNome());
        info.getStyleClass().add("card-subtitle");

        Label descricao = new Label(jogo.getDesenvolvedora() != null ? jogo.getDesenvolvedora().getNome() : "Desenvolvedora desconhecida");
        descricao.getStyleClass().add("card-subtitle");

        HBox tags = new HBox(6);
        tags.getChildren().add(criarTag("Biblioteca"));
        if (jogo.getPreco() <= 0.0) {
            tags.getChildren().add(criarTag("Grátis"));
        }

        Label preco = new Label(jogo.getPreco() <= 0.0 ? "Grátis" : String.format("R$ %.2f", jogo.getPreco()));
        preco.getStyleClass().add("price-tag");

        Button jogar = new Button("Jogar");
        jogar.getStyleClass().add("btn-primary");
        jogar.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Iniciando: " + jogo.getTitulo());
            alert.setTitle("Jogar");
            alert.setHeaderText(null);
            alert.showAndWait();
        });

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);
        HBox rodape = new HBox(preco, espacador, jogar);
        rodape.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, capa, nome, info, descricao, tags, rodape);
        card.getStyleClass().add("game-card");
        card.setPrefWidth(320);
        return card;
    }

    private Label criarTag(String texto) {
        Label tag = new Label(texto);
        tag.getStyleClass().add("tag");
        return tag;
    }

    private List<Jogo> coletarJogosComprados() {
        var clienteOpt = repository.buscarClientePorNickname(nickname);
        List<Jogo> jogosComprados = new ArrayList<>();
        if (clienteOpt.isPresent()) {
            for (Pedido pedido : clienteOpt.get().getPedidos()) {
                if (pedido.getStatus() == StatusPedido.FINALIZADO) {
                    for (var item : pedido.getItens()) {
                        Jogo jogo = item.getJogo();
                        if (!jogosComprados.contains(jogo)) {
                            jogosComprados.add(jogo);
                        }
                    }
                }
            }
        }
        return jogosComprados;
    }

    private Button criarBotaoLateral(String texto) {
        Button botao = new Button(texto);
        botao.getStyleClass().add("btn-secondary");
        botao.setMaxWidth(Double.MAX_VALUE);
        return botao;
    }

    private Label criarItemLateral(String texto) {
        Label item = new Label(texto);
        item.getStyleClass().add("sidebar-item");
        return item;
    }

    private Label criarSubtituloLateral(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("sidebar-subtitle");
        return label;
    }
}
