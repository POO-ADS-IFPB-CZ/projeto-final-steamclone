package com.steamclone.ui;

import com.steamclone.model.Cliente;
import com.steamclone.model.Jogo;
import com.steamclone.model.Pedido;
import com.steamclone.model.Pagamento;
import com.steamclone.repository.LojaRepository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

public class GerenciamentoView {

    private final LojaRepository repository;
    private final Consumer<Void> onSave;
    private final ListView<Cliente> clientesLista = new ListView<>();
    private final ListView<Jogo> jogosLista = new ListView<>();
    private final ListView<Pedido> pedidosLista = new ListView<>();
    private final ListView<Pagamento> pagamentosLista = new ListView<>();

    public GerenciamentoView(LojaRepository repository, Consumer<Void> onSave) {
        this.repository = repository;
        this.onSave = onSave;
        atualizarListas();
    }

    public ScrollPane criarPainel() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));

        Label titulo = new Label("Gerenciamento de Entidades");
        titulo.getStyleClass().add("section-title");

        HBox caixas = new HBox(16,
                criarSecaoClientes(),
                criarSecaoJogos(),
                criarSecaoPedidos(),
                criarSecaoPagamentos());
        caixas.setAlignment(Pos.TOP_LEFT);

        root.getChildren().addAll(titulo, caixas);
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        return scroll;
    }

    private VBox criarSecaoClientes() {
        Label titulo = new Label("Clientes");
        titulo.getStyleClass().add("section-subtitle");

        Button adicionar = new Button("Adicionar");
        adicionar.setOnAction(e -> adicionarCliente());
        Button remover = new Button("Remover");
        remover.setOnAction(e -> removerCliente());
        Button editar = new Button("Editar");
        editar.setOnAction(e -> editarCliente());

        HBox botoes = new HBox(6, adicionar, editar, remover);
        clientesLista.setPrefSize(250, 320);

        return new VBox(8, titulo, botoes, clientesLista);
    }

    private VBox criarSecaoJogos() {
        Label titulo = new Label("Jogos");
        titulo.getStyleClass().add("section-subtitle");

        Button adicionar = new Button("Adicionar");
        adicionar.setOnAction(e -> adicionarJogo());
        Button remover = new Button("Remover");
        remover.setOnAction(e -> removerJogo());
        Button editar = new Button("Editar");
        editar.setOnAction(e -> editarJogo());

        HBox botoes = new HBox(6, adicionar, editar, remover);
        jogosLista.setPrefSize(250, 320);

        return new VBox(8, titulo, botoes, jogosLista);
    }

    private VBox criarSecaoPedidos() {
        Label titulo = new Label("Pedidos");
        titulo.getStyleClass().add("section-subtitle");

        Button remover = new Button("Remover");
        remover.setOnAction(e -> removerPedido());
        pedidosLista.setPrefSize(250, 320);

        return new VBox(8, titulo, remover, pedidosLista);
    }

    private VBox criarSecaoPagamentos() {
        Label titulo = new Label("Pagamentos");
        titulo.getStyleClass().add("section-subtitle");

        Button remover = new Button("Remover");
        remover.setOnAction(e -> removerPagamento());
        pagamentosLista.setPrefSize(250, 320);

        return new VBox(8, titulo, remover, pagamentosLista);
    }

    private void atualizarListas() {
        clientesLista.getItems().setAll(repository.getClientes().stream().sorted(Comparator.comparing(Cliente::getNome)).toList());
        jogosLista.getItems().setAll(repository.getJogos().stream().sorted(Comparator.comparing(Jogo::getTitulo)).toList());
        pedidosLista.getItems().setAll(repository.getPedidos().stream().sorted(Comparator.comparingInt(Pedido::getIdPedido)).toList());
        pagamentosLista.getItems().setAll(repository.getPagamentos().stream().sorted(Comparator.comparingInt(Pagamento::getIdPagamento)).toList());
    }

    private void adicionarCliente() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Cliente");
        dialog.setHeaderText("Criar cliente");
        dialog.setContentText("Nome do cliente:");
        dialog.showAndWait().ifPresent(nome -> {
            if (nome.isBlank()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Nome obrigatório.");
                return;
            }
            Cliente cliente = new Cliente(nome, "000.000.000-00", nome.toLowerCase().replace(" ", "" ) + "@email.com", LocalDate.now().minusYears(20), nome.toLowerCase().replace(" ", ""), LocalDate.now());
            repository.adicionarCliente(cliente);
            repository.salvar();
            atualizarListas();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Cliente criado com sucesso.");
        });
    }

    private void editarCliente() {
        Cliente selecionado = clientesLista.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um cliente para editar.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(selecionado.getNome());
        dialog.setTitle("Editar Cliente");
        dialog.setHeaderText("Alterar nome do cliente");
        dialog.setContentText("Nome:");
        dialog.showAndWait().ifPresent(nome -> {
            selecionado.setNome(nome);
            repository.atualizarCliente(selecionado);
            repository.salvar();
            atualizarListas();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Cliente atualizado.");
        });
    }

    private void removerCliente() {
        Cliente selecionado = clientesLista.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um cliente para remover.");
            return;
        }
        repository.removerCliente(selecionado);
        repository.salvar();
        atualizarListas();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Cliente removido.");
    }

    private void adicionarJogo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Jogo");
        dialog.setHeaderText("Criar jogo");
        dialog.setContentText("Título do jogo:");
        dialog.showAndWait().ifPresent(titulo -> {
            if (titulo.isBlank()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Título obrigatório.");
                return;
            }
            Jogo jogo = new Jogo(repository.getJogos().size() + 1, titulo, 0.0, LocalDate.now());
            repository.adicionarJogo(jogo);
            repository.salvar();
            atualizarListas();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Jogo criado com sucesso.");
        });
    }

    private void editarJogo() {
        Jogo selecionado = jogosLista.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um jogo para editar.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selecionado.getPreco()));
        dialog.setTitle("Editar Jogo");
        dialog.setHeaderText("Alterar preço do jogo");
        dialog.setContentText("Preço:");
        dialog.showAndWait().ifPresent(precoTexto -> {
            try {
                double preco = Double.parseDouble(precoTexto);
                selecionado.setPreco(preco);
                repository.atualizarJogo(selecionado);
                repository.salvar();
                atualizarListas();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Jogo atualizado.");
            } catch (NumberFormatException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Preço inválido.");
            }
        });
    }

    private void removerJogo() {
        Jogo selecionado = jogosLista.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um jogo para remover.");
            return;
        }
        repository.removerJogo(selecionado);
        repository.salvar();
        atualizarListas();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Jogo removido.");
    }

    private void removerPedido() {
        Pedido selecionado = pedidosLista.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um pedido para remover.");
            return;
        }
        repository.removerPedido(selecionado);
        repository.salvar();
        atualizarListas();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Pedido removido.");
    }

    private void removerPagamento() {
        Pagamento selecionado = pagamentosLista.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um pagamento para remover.");
            return;
        }
        repository.removerPagamento(selecionado);
        repository.salvar();
        atualizarListas();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Pagamento removido.");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo, mensagem);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
