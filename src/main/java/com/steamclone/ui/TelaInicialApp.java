package com.steamclone.ui;

import com.steamclone.model.Jogo;
import com.steamclone.repository.LojaRepository;
import com.steamclone.service.ConsultaService;
import com.steamclone.ui.GerenciamentoView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela inicial da loja — front-end em JavaFX.
 */
public class TelaInicialApp extends Application {

    private final LojaRepository repository;
    private final ConsultaService consultaService;

    private TextField buscaField;
    private Button cartButton;
    private Label resultadoBuscaLabel;
    private FlowPane catalogoGrid;
    private BorderPane rootLayout;
    private Label userLabel;
    private ImageView bannerImageView;

    private String currentNickname = "TSUKI_11";
    private boolean bibliotecaAtiva = false;
    private final List<Jogo> carrinho = new ArrayList<>();
    private int wishlistCount = 15;
    private Button wishlistButton;

    public TelaInicialApp() {
        this.repository = LojaRepository.carregar();
        this.consultaService = new ConsultaService(repository);
    }

    @Override
    public void start(Stage stage) {
        rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("root");
        rootLayout.setTop(criarTopo());
        rootLayout.setCenter(criarConteudoLoja());

        Scene scene = new Scene(rootLayout, 1200, 760);
        scene.getStylesheets().add(getClass().getResource("steam-theme.css").toExternalForm());

        stage.setTitle("Steam Clone - Loja de Jogos");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }

    private VBox criarTopo() {
        HBox linha = new HBox(14);
        linha.getStyleClass().add("top-bar");
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setPadding(new Insets(10, 18, 10, 18));

        Label logo = new Label("STEAM CLONE");
        logo.getStyleClass().add("logo-text");

        Button lojaBtn = criarBotaoNav("LOJA");
        lojaBtn.setOnAction(e -> mostrarLoja());
        Button bibliotecaBtn = criarBotaoNav("BIBLIOTECA");
        bibliotecaBtn.setOnAction(e -> exibirBiblioteca());
        Button gerenciarBtn = criarBotaoNav("GERENCIAR");
        gerenciarBtn.setOnAction(e -> exibirGerenciamento());

        HBox navEsquerda = new HBox(4, logo, espaco(18), lojaBtn, bibliotecaBtn, gerenciarBtn);
        navEsquerda.setAlignment(Pos.CENTER_LEFT);

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        buscaField = new TextField();
        buscaField.setPromptText("Buscar na loja");
        buscaField.getStyleClass().add("search-field");
        buscaField.setPrefWidth(260);
        buscaField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                filtrarJogos();
            }
        });

        Button botaoBuscar = criarBotaoIcone("Buscar");
        botaoBuscar.setOnAction(e -> filtrarJogos());
        Button botaoLimpar = criarBotaoIcone("Limpar");
        botaoLimpar.setOnAction(e -> limparBusca());

        HBox buscaBox = new HBox(6, buscaField, botaoBuscar, botaoLimpar);
        buscaBox.setAlignment(Pos.CENTER);

        wishlistButton = criarBotaoIcone("Lista de desejos " + wishlistCount);
        cartButton = criarBotaoIcone("Carrinho 0");
        cartButton.setOnAction(e -> exibirCarrinho());

        userLabel = new Label(currentNickname);
        userLabel.getStyleClass().add("user-label");
        userLabel.setOnMouseClicked(e -> alterarNickname());

        HBox navDireita = new HBox(10, wishlistButton, cartButton, userLabel);
        navDireita.setAlignment(Pos.CENTER_RIGHT);

        linha.getChildren().addAll(navEsquerda, espacador, buscaBox, espaco(20), navDireita);

        return new VBox(linha);
    }

    private Region espaco(double largura) {
        Region r = new Region();
        r.setMinWidth(largura);
        return r;
    }

    private Button criarBotaoNav(String texto) {
        Button botao = new Button(texto);
        botao.getStyleClass().add("nav-button");
        return botao;
    }

    private Button criarBotaoIcone(String texto) {
        Button botao = new Button(texto);
        botao.getStyleClass().add("top-icon-button");
        return botao;
    }

    private void alterarNickname() {
        TextInputDialog dialog = new TextInputDialog(currentNickname);
        dialog.setTitle("Alterar nickname");
        dialog.setHeaderText(null);
        dialog.setContentText("Digite seu nickname:");
        dialog.showAndWait().ifPresent(novo -> {
            if (!novo.isBlank()) {
                currentNickname = novo.trim();
                userLabel.setText(currentNickname);
                if (bibliotecaAtiva) {
                    exibirBiblioteca();
                }
            }
        });
    }

    private ScrollPane criarConteudoLoja() {
        VBox corpo = new VBox(24);
        corpo.setPadding(new Insets(22));
        corpo.getChildren().addAll(criarBannerPrincipal(), criarSessaoDestaque(), criarCatalogo());

        ScrollPane scroll = new ScrollPane(corpo);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        return scroll;
    }

    private HBox criarBannerPrincipal() {
        HBox banner = new HBox(24);
        banner.getStyleClass().add("banner");
        banner.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("FESTIVAL FERROVIÁRIO DO STEAM");
        titulo.getStyleClass().add("banner-title");
        titulo.setWrapText(true);

        Label descricao = new Label("Descontos, demos e muito mais");
        descricao.getStyleClass().add("banner-subtitle");

        Label data = new Label("Até 27 de julho às 14h (BRT)");
        data.getStyleClass().add("banner-date");

        Button botaoOfertas = new Button("Ver ofertas do festival");
        botaoOfertas.getStyleClass().add("btn-primary");

        VBox textoBox = new VBox(10, titulo, descricao, data, espaco(4), botaoOfertas);
        textoBox.setMaxWidth(560);
        HBox.setHgrow(textoBox, Priority.ALWAYS);

        StackPane imagem = new StackPane();
        imagem.getStyleClass().add("banner-image-placeholder");
        imagem.setPrefSize(380, 210);
        imagem.setMinSize(380, 210);
        bannerImageView = criarImagemResource("/images/sky1.jpg", 380, 210);
        if (bannerImageView != null) {
            imagem.getChildren().add(bannerImageView);
        } else {
            imagem.getChildren().add(criarLabelPlaceholder("IMAGEM DE CAPA"));
        }

        banner.getChildren().addAll(textoBox, imagem);
        return banner;
    }

    private Label criarLabelPlaceholder(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("banner-image-text");
        return label;
    }

    private VBox criarSessaoDestaque() {
        Label titulo = new Label("Destaques e recomendados");
        titulo.getStyleClass().add("section-title");

        FlowPane cards = new FlowPane(16, 16);
        List<Jogo> jogos = repository.getJogos();
        for (int i = 0; i < Math.min(3, jogos.size()); i++) {
            cards.getChildren().add(criarCardDestaque(jogos.get(i), i));
        }

        HBox promocoes = criarSessaoPromocoes();

        return new VBox(14, titulo, cards, promocoes);
    }

    private HBox criarSessaoPromocoes() {
        return new HBox(16,
                criarCartaoPromocional("OFERTA DO FIM DE SEMANA", "Até 40% de desconto"),
                criarCartaoPromocional("FIM DE SEMANA GRÁTIS", "Jogue sem custo"),
                criarCartaoPromocional("OFERTA DO DIA", "Novos títulos em promoção"));
    }

    private VBox criarCartaoPromocional(String titulo, String descricao) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTitulo.setTextFill(Color.WHITE);

        Label lblDescricao = new Label(descricao);
        lblDescricao.getStyleClass().add("card-subtitle");

        Button botaoVer = new Button("Ver mais");
        botaoVer.getStyleClass().add("btn-secondary");
        botaoVer.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION, "Promoção: " + titulo).showAndWait());

        VBox card = new VBox(10, lblTitulo, lblDescricao, botaoVer);
        card.getStyleClass().add("promo-card");
        card.setPrefSize(300, 140);
        return card;
    }

    private VBox criarCardDestaque(Jogo jogo, int index) {
        StackPane imagem = new StackPane();
        imagem.getStyleClass().add("card-image-placeholder");
        imagem.setPrefSize(280, 130);
        String caminhoImagem = jogo.getImagemCapa() != null ? jogo.getImagemCapa() : "/images/capsule_616x353.jpg";
        ImageView cardImage = criarImagemResource(caminhoImagem, 280, 130);
        if (cardImage != null) {
            imagem.getChildren().add(cardImage);
        } else {
            imagem.getChildren().add(criarLabelPlaceholder(jogo.getTitulo()));
        }

        Label badge = new Label(index == 0 ? "POPULAR" : index == 1 ? "RECOMENDADO" : "NOVO");
        badge.getStyleClass().add("badge");

        Label titulo = new Label(jogo.getTitulo());
        titulo.getStyleClass().add("card-title");
        titulo.setWrapText(true);

        String nomeDev = jogo.getDesenvolvedora() != null ? jogo.getDesenvolvedora().getNome() : "Desenvolvedora desconhecida";
        String plataforma = jogo.getPlataformas().isEmpty() ? "PC" : jogo.getPlataformas().get(0).getNome();
        Label descricao = new Label(nomeDev + " | " + plataforma);
        descricao.getStyleClass().add("card-subtitle");

        Label preco = new Label(formatarPreco(jogo));
        preco.getStyleClass().add("price-tag");

        VBox card = new VBox(10, imagem, badge, titulo, descricao, preco);
        card.getStyleClass().add("game-card");
        card.setPrefWidth(300);
        card.setOnMouseClicked(e -> {
            atualizarBannerDoJogo(jogo);
            exibirDetalhesJogo(jogo);
        });
        return card;
    }

    private VBox criarCatalogo() {
        Label titulo = new Label("Catálogo de jogos");
        titulo.getStyleClass().add("section-title");

        resultadoBuscaLabel = new Label("Mostrando todos os jogos");
        resultadoBuscaLabel.getStyleClass().add("result-label");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox cabecalho = new HBox(titulo, espacador, resultadoBuscaLabel);
        cabecalho.setAlignment(Pos.CENTER_LEFT);

        catalogoGrid = new FlowPane(16, 16);
        atualizarCatalogo(repository.getJogos());

        return new VBox(14, cabecalho, catalogoGrid);
    }

    private VBox criarCardCatalogo(Jogo jogo) {
        Label nome = new Label(jogo.getTitulo());
        nome.getStyleClass().add("card-title");
        nome.setWrapText(true);

        Label preco = new Label(formatarPreco(jogo));
        preco.getStyleClass().add("price-tag");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);
        HBox topoCard = new HBox(nome, espacador, preco);
        topoCard.setAlignment(Pos.CENTER_LEFT);

        String nomeDev = jogo.getDesenvolvedora() != null ? jogo.getDesenvolvedora().getNome() : "Desenvolvedora desconhecida";
        String plataformas = jogo.getPlataformas().isEmpty() ? "PC" : String.join(", ", jogo.getPlataformas().stream().map(p -> p.getNome()).toList());

        Label lblDev = new Label(nomeDev);
        lblDev.getStyleClass().add("card-subtitle");
        Label lblPlataformas = new Label(plataformas);
        lblPlataformas.getStyleClass().add("card-subtitle");

        Button verDetalhes = new Button("Detalhes");
        verDetalhes.getStyleClass().add("btn-secondary");
        verDetalhes.setOnAction(e -> exibirDetalhesJogo(jogo));

        Button adicionar = new Button("Adicionar ao carrinho");
        adicionar.getStyleClass().add("btn-success");
        adicionar.setOnAction(e -> adicionarAoCarrinho(jogo));

        HBox botoes = new HBox(8, verDetalhes, adicionar);

        VBox card = new VBox(8, topoCard, lblDev, lblPlataformas, botoes);
        card.getStyleClass().add("game-card");
        card.setPrefWidth(320);
        return card;
    }

    private void atualizarCatalogo(List<Jogo> jogos) {
        catalogoGrid.getChildren().clear();
        if (jogos.isEmpty()) {
            Label vazio = new Label("Nenhum jogo encontrado.");
            vazio.getStyleClass().add("empty-label");
            catalogoGrid.getChildren().add(vazio);
        } else {
            for (Jogo jogo : jogos) {
                catalogoGrid.getChildren().add(criarCardCatalogo(jogo));
            }
        }
        if (resultadoBuscaLabel != null) {
            resultadoBuscaLabel.setText(jogos.size() + " jogo(s) encontrados");
        }
    }

    private void filtrarJogos() {
        String termo = buscaField.getText() == null ? "" : buscaField.getText().trim();
        if (termo.isBlank()) {
            atualizarCatalogo(repository.getJogos());
            return;
        }
        String filtro = termo.toLowerCase();
        List<Jogo> resultados = repository.getJogos().stream()
                .filter(jogo -> jogo.getTitulo().toLowerCase().contains(filtro)
                        || (jogo.getDesenvolvedora() != null && jogo.getDesenvolvedora().getNome().toLowerCase().contains(filtro))
                        || jogo.getPlataformas().stream().anyMatch(p -> p.getNome().toLowerCase().contains(filtro)))
                .toList();
        atualizarCatalogo(resultados);
    }

    private void limparBusca() {
        buscaField.clear();
        atualizarCatalogo(repository.getJogos());
    }

    private void mostrarLoja() {
        rootLayout.setCenter(criarConteudoLoja());
        bibliotecaAtiva = false;
    }

    private void exibirGerenciamento() {
        GerenciamentoView gerenciamento = new GerenciamentoView(repository, v -> salvarDados());
        rootLayout.setCenter(gerenciamento.criarPainel());
        bibliotecaAtiva = false;
    }

    private void exibirBiblioteca() {
        BibliotecaView biblioteca = new BibliotecaView(repository, currentNickname);
        rootLayout.setCenter(biblioteca.criarPainel());
        bibliotecaAtiva = true;
    }

    private void salvarDados() {
        if (repository.salvar()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dados salvos com sucesso.");
            alert.setTitle("Salvar");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível salvar os dados.");
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private void exibirDetalhesJogo(Jogo jogo) {
        String desenvolvedora = jogo.getDesenvolvedora() != null ? jogo.getDesenvolvedora().getNome() : "Desenvolvedora desconhecida";
        String plataformas = jogo.getPlataformas().isEmpty() ? "PC" : String.join(", ", jogo.getPlataformas().stream().map(p -> p.getNome()).toList());
        String avaliacoes = jogo.getAvaliacoes().isEmpty() ? "Sem avaliações" : String.format("%.1f/10 (%d avaliações)", jogo.getMediaNotas(), jogo.getAvaliacoes().size());
        String mensagem = String.format(
                "Desenvolvedora: %s%nPlataformas: %s%nPreço: %s%nLançamento: %s%nAvaliações: %s",
                desenvolvedora, plataformas, formatarPreco(jogo),
                jogo.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                avaliacoes
        );
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes do jogo");
        alert.setHeaderText(jogo.getTitulo());
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void adicionarAoCarrinho(Jogo jogo) {
        carrinho.add(jogo);
        cartButton.setText("Carrinho " + carrinho.size());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, jogo.getTitulo() + " adicionado ao carrinho.");
        alert.setTitle("Carrinho");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void exibirCarrinho() {
        if (carrinho.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Seu carrinho está vazio.").showAndWait();
            return;
        }
        StringBuilder detalhe = new StringBuilder();
        double total = 0.0;
        for (Jogo jogo : carrinho) {
            detalhe.append("- ").append(jogo.getTitulo()).append(" (").append(formatarPreco(jogo)).append(")\n");
            total += jogo.getPreco();
        }
        detalhe.append(String.format("%nTotal: R$ %.2f", total));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Carrinho");
        alert.setHeaderText(null);
        alert.setContentText(detalhe.toString());
        alert.showAndWait();
    }

    private String formatarPreco(Jogo jogo) {
        return jogo.getPreco() <= 0.0 ? "Grátis" : String.format("R$ %.2f", jogo.getPreco());
    }

    private ImageView criarImagemResource(String caminho, double largura, double altura) {
        try {
            Image imagem = carregarImagem(caminho);
            if (imagem == null) {
                return null;
            }
            ImageView view = new ImageView(imagem);
            view.setFitWidth(largura);
            view.setFitHeight(altura);
            view.setPreserveRatio(true);
            view.setSmooth(true);
            return view;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Image carregarImagem(String caminho) {
        if (getClass().getResource(caminho) == null) {
            return null;
        }
        return new Image(getClass().getResource(caminho).toExternalForm(), false);
    }

    private void atualizarBannerDoJogo(Jogo jogo) {
        if (bannerImageView == null) {
            return;
        }
        String caminho = jogo.getImagemCapa() != null ? jogo.getImagemCapa() : "/images/header.jpg";
        Image imagem = carregarImagem(caminho);
        if (imagem != null) {
            bannerImageView.setImage(imagem);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
