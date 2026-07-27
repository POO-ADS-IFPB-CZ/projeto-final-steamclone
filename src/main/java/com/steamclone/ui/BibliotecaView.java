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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BibliotecaView {

    private static final String CAPA_PADRAO = "/images/sky1.jpg";

    private final LojaRepository repository;
    private final String nickname;
    private final List<Jogo> jogosComprados;
    private final List<Jogo> todosOsJogos;

    private TextField pesquisaField;
    private ComboBox<String> categoriaCombo;
    private Label infoLabel;
    private VBox listaJogosBox;
    private StackPane areaDestaque;

    public BibliotecaView(LojaRepository repository, String nickname) {
        this.repository = repository;
        this.nickname = nickname;
        this.jogosComprados = coletarJogosComprados();
        this.todosOsJogos = new ArrayList<>(repository.getJogos());
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
        split.setDividerPositions(0.25);
        split.setPrefHeight(620);
        split.setMinHeight(560);
        VBox.setVgrow(split, Priority.ALWAYS);
        return split;
    }

    private VBox criarPainelLateral() {
        VBox lateral = new VBox(10);
        lateral.getStyleClass().add("sidebar");
        lateral.setPrefWidth(250);

        Label titulo = new Label("Página inicial");
        titulo.getStyleClass().add("sidebar-title");

        Button btnTodos = criarBotaoLateral("Todos os jogos");
        btnTodos.setOnAction(e -> {
            categoriaCombo.getSelectionModel().selectFirst();
            pesquisaField.clear();
            atualizarListaDeTitulos(jogosComprados);
        });

        Button btnFavoritos = criarBotaoLateral("Favoritos");
        btnFavoritos.setOnAction(e -> atualizarListaDeTitulos(
                jogosComprados.stream().limit(4).collect(Collectors.toList())));

        Button btnAtualizacoes = criarBotaoLateral("Atualizações");
        btnAtualizacoes.setOnAction(e -> atualizarListaDeTitulos(jogosComprados.stream()
                .filter(j -> j.getPreco() <= 0.0 || j.getDataLancamento().getYear() >= 2023)
                .collect(Collectors.toList())));

        pesquisaField = new TextField();
        pesquisaField.setPromptText("Pesquisar jogos na biblioteca");
        pesquisaField.getStyleClass().add("search-field");
        pesquisaField.textProperty().addListener((obs, oldVal, newVal) -> filtrarBiblioteca());

        Label categoriaLabel = new Label("Categoria");
        categoriaLabel.getStyleClass().add("sidebar-subtitle");

        categoriaCombo = new ComboBox<>();
        categoriaCombo.getItems().addAll("Todos", "PC", "PlayStation 5", "Nintendo Switch", "Steam Deck");
        categoriaCombo.getSelectionModel().selectFirst();
        categoriaCombo.setMaxWidth(Double.MAX_VALUE);
        categoriaCombo.setOnAction(e -> filtrarBiblioteca());

        Label jogosLabel = criarSubtituloLateral("JOGOS DA BIBLIOTECA");

        listaJogosBox = new VBox(4);
        atualizarListaDeTitulos(jogosComprados);

        ScrollPane listaScroll = new ScrollPane(listaJogosBox);
        listaScroll.setFitToWidth(true);
        listaScroll.setPrefViewportHeight(300);
        listaScroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(listaScroll, Priority.ALWAYS);

        lateral.getChildren().addAll(
                titulo, btnTodos, btnFavoritos, btnAtualizacoes,
                new Separator(), pesquisaField, categoriaLabel, categoriaCombo,
                new Separator(), jogosLabel, listaScroll);
        return lateral;
    }

    private VBox criarPainelPrincipal() {
        Label destaque = new Label("Jogo selecionado");
        destaque.getStyleClass().add("section-title");

        Label subtitulo = new Label("Clique em um título da lista para trocar o jogo exibido.");
        subtitulo.getStyleClass().add("section-subtitle");

        VBox cabecalho = new VBox(4, destaque, subtitulo);

        areaDestaque = new StackPane();
        areaDestaque.getStyleClass().add("selected-cover-placeholder");
        areaDestaque.setMinHeight(470);
        areaDestaque.setPrefHeight(550);
        areaDestaque.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        areaDestaque.setPadding(Insets.EMPTY);

        if (jogosComprados.isEmpty()) {
            areaDestaque.getChildren().setAll(criarLabelPlaceholder("Nenhum jogo na biblioteca."));
        } else {
            Jogo primeiroJogo = jogosComprados.get(0);
            mostrarCardDoJogo(primeiroJogo);
        }

        VBox principal = new VBox(10, cabecalho, areaDestaque);
        principal.getStyleClass().add("library-main");
        principal.setPadding(new Insets(0));
        principal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(areaDestaque, Priority.ALWAYS);
        return principal;
    }

    private void atualizarListaDeTitulos(List<Jogo> jogos) {
        listaJogosBox.getChildren().clear();

        if (jogos.isEmpty()) {
            Label vazio = new Label("Nenhum jogo corresponde à busca.");
            vazio.getStyleClass().add("empty-label");
            vazio.setWrapText(true);
            listaJogosBox.getChildren().add(vazio);
        } else {
            jogos.forEach(jogo -> listaJogosBox.getChildren().add(criarItemLateral(jogo)));
        }

        infoLabel.setText(jogos.size() + " jogo(s) exibido(s) • " + jogosComprados.size() + " comprado(s)");
    }

    private void filtrarBiblioteca() {
        String termo = pesquisaField.getText() == null ? "" : pesquisaField.getText().trim().toLowerCase();
        String categoria = categoriaCombo.getValue() != null ? categoriaCombo.getValue() : "Todos";

        List<Jogo> filtrados = todosOsJogos.stream()
                .filter(jogo -> termo.isBlank()
                        || jogo.getTitulo().toLowerCase().contains(termo)
                        || (jogo.getDesenvolvedora() != null
                        && jogo.getDesenvolvedora().getNome().toLowerCase().contains(termo))
                        || jogo.getPlataformas().stream()
                        .anyMatch(p -> p.getNome().toLowerCase().contains(termo)))
                .filter(jogo -> categoria.equals("Todos")
                        || jogo.getPlataformas().stream()
                        .anyMatch(p -> p.getNome().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());

        atualizarListaDeTitulos(filtrados);
    }

    private void mostrarCardDoJogo(Jogo jogo) {
        VBox card = criarCardDestaque(jogo);
        card.prefWidthProperty().bind(areaDestaque.widthProperty());
        card.prefHeightProperty().bind(areaDestaque.heightProperty());
        StackPane.setAlignment(card, Pos.CENTER);
        areaDestaque.getChildren().setAll(card);
    }

    private VBox criarCardDestaque(Jogo jogo) {
        String caminhoImagem = jogo.getImagemCapa() != null ? jogo.getImagemCapa() : CAPA_PADRAO;
        Image imagem = carregarImagem(caminhoImagem);

        StackPane capa = new StackPane();
        capa.getStyleClass().add("card-image-placeholder");
        capa.setMinHeight(300);
        capa.setPrefHeight(390);
        capa.setMaxHeight(Double.MAX_VALUE);
        capa.setMaxWidth(Double.MAX_VALUE);

        if (imagem != null) {
            ImageView imageView = new ImageView(imagem);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.fitWidthProperty().bind(capa.widthProperty().subtract(8));
            imageView.fitHeightProperty().bind(capa.heightProperty().subtract(8));
            capa.getChildren().add(imageView);
        } else {
            capa.getChildren().add(criarLabelPlaceholder("Imagem não disponível"));
        }

        Label nome = new Label(jogo.getTitulo());
        nome.getStyleClass().add("card-title");
        nome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        nome.setWrapText(true);

        String desenvolvedora = jogo.getDesenvolvedora() != null
                ? jogo.getDesenvolvedora().getNome()
                : "Desenvolvedora desconhecida";

        Label empresa = new Label("Desenvolvedora: " + desenvolvedora);
        empresa.getStyleClass().add("card-subtitle");

        String plataformas = jogo.getPlataformas().isEmpty()
                ? "Não informada"
                : jogo.getPlataformas().stream()
                .map(p -> p.getNome())
                .collect(Collectors.joining(", "));

        Label plataforma = new Label("Plataformas: " + plataformas);
        plataforma.getStyleClass().add("card-subtitle");
        plataforma.setWrapText(true);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Label lancamento = new Label("Lançamento: " + jogo.getDataLancamento().format(formato));
        lancamento.getStyleClass().add("card-subtitle");

        Label avaliacao = new Label(jogo.getAvaliacoes().isEmpty()
                ? "Avaliação: ainda sem avaliações"
                : String.format("Avaliação média: %.1f/10", jogo.getMediaNotas()));
        avaliacao.getStyleClass().add("card-subtitle");

        HBox tags = new HBox(6);
        tags.getChildren().add(criarTag("Na biblioteca"));
        if (jogo.getPreco() <= 0.0) {
            tags.getChildren().add(criarTag("Grátis"));
        }

        Label preco = new Label(jogo.getPreco() <= 0.0
                ? "Grátis"
                : String.format("R$ %.2f", jogo.getPreco()));
        preco.getStyleClass().add("price-tag");
        preco.setStyle("-fx-font-size: 16px;");

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
        HBox rodape = new HBox(12, preco, espacador, jogar);
        rodape.setAlignment(Pos.CENTER_LEFT);

        VBox informacoes = new VBox(7, nome, empresa, plataforma, lancamento, avaliacao, tags, rodape);
        VBox card = new VBox(10, capa, informacoes);
        card.getStyleClass().add("game-card");
        card.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(capa, Priority.ALWAYS);
        return card;
    }

    private Image carregarImagem(String caminho) {
        var resource = getClass().getResource(caminho);
        if (resource == null) {
            return null;
        }
        return new Image(resource.toExternalForm(), false);
    }

    private Label criarTag(String texto) {
        Label tag = new Label(texto);
        tag.getStyleClass().add("tag");
        return tag;
    }

    private Label criarLabelPlaceholder(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("banner-image-text");
        label.setWrapText(true);
        return label;
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

    private Label criarItemLateral(Jogo jogo) {
        Label item = new Label(jogo.getTitulo());
        item.getStyleClass().add("sidebar-item");
        item.setWrapText(true);
        item.setMaxWidth(Double.MAX_VALUE);
        item.setOnMouseClicked(e -> mostrarCardDoJogo(jogo));
        return item;
    }

    private Label criarSubtituloLateral(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("sidebar-subtitle");
        return label;
    }
}
