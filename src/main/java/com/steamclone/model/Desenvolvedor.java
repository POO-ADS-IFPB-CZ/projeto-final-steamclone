package com.steamclone.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Especialização de Pessoa com informações de vínculo laboral.
 */
public class Desenvolvedor extends Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String matricula;
    private String cargo;
    private double salario;
    private boolean ativo;
    private Desenvolvedora desenvolvedora;

    public Desenvolvedor(String nome, String cpf, String email, LocalDate dataNascimento,
                         String matricula, String cargo, double salario) {
        super(nome, cpf, email, dataNascimento);
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
        this.ativo = true;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Desenvolvedora getDesenvolvedora() {
        return desenvolvedora;
    }

    public void setDesenvolvedora(Desenvolvedora desenvolvedora) {
        this.desenvolvedora = desenvolvedora;
        if (desenvolvedora != null) {
            desenvolvedora.adicionarDesenvolvedor(this);
        }
    }

    @Override
    public String getTipo() {
        return "Desenvolvedor";
    }

    @Override
    public String toString() {
        return super.toString() + " - " + cargo + " (R$ " + String.format("%.2f", salario) + ")";
    }
}
