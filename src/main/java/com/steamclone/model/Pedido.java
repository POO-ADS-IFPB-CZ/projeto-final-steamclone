package com.steamclone.model;

import com.steamclone.model.enums.StatusPedido;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Transação de compra de jogos realizada pelo cliente.
 */
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPedido;
    private LocalDate dataPedido;
    private double valorTotal;
    private StatusPedido status;
    private Cliente cliente;
    private Pagamento pagamento;
    private final List<ItemPedido> itens = new ArrayList<>();

    public Pedido(int idPedido, LocalDate dataPedido, StatusPedido status) {
        this.idPedido = idPedido;
        this.dataPedido = dataPedido;
        this.status = status;
        this.valorTotal = 0.0;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        if (pagamento != null) {
            pagamento.setPedido(this);
        }
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(Jogo jogo, int quantidade) {
        itens.add(new ItemPedido(jogo, quantidade));
        recalcularValorTotal();
    }

    private void recalcularValorTotal() {
        valorTotal = itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + status + " - R$ " + String.format("%.2f", valorTotal);
    }
}
