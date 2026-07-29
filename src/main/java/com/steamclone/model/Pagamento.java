package com.steamclone.model;

import com.steamclone.model.enums.MetodoPagamento;
import com.steamclone.model.enums.StatusPagamento;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Informações financeiras da transação associada ao pedido.
 */
public class Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPagamento;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private LocalDate dataPagamento;
    private Pedido pedido;

    public Pagamento(int idPagamento, MetodoPagamento metodo, StatusPagamento status,
                     LocalDate dataPagamento) {
        this.idPagamento = idPagamento;
        this.metodo = metodo;
        this.status = status;
        this.dataPagamento = dataPagamento;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Pedido getPedido() {
        return pedido;
    }

    void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "Pagamento #" + idPagamento + " - " + metodo + " (" + status + ")";
    }
}
