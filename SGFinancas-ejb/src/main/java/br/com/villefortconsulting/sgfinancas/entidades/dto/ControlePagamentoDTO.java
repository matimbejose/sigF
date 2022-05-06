package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class ControlePagamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String nome;

    @Getter
    private final List<ControlePagamentoItemDTO> itens;

    public ControlePagamentoDTO() {
        this.itens = new ArrayList<>();
    }

    public ControlePagamentoDTO(String nome) {
        this.nome = nome;
        this.itens = new ArrayList<>();
    }

    public ControlePagamentoDTO(String nome, List<ControlePagamentoItemDTO> itens) {
        this.nome = nome;
        this.itens = new ArrayList<>();
        this.itens.addAll(itens);
    }

    public ControlePagamentoDTO(String nome, List<ControlePagamentoItemDTO> extrato, List<ControlePagamentoItemDTO> contas) {
        this.nome = nome;
        this.itens = new ArrayList<>();
        this.itens.addAll(extrato);
        this.itens.addAll(contas);
    }

    public Double getValorTotal() {
        return itens.stream()
                .filter(ControlePagamentoItemDTO::isLancamento)
                .map(ControlePagamentoItemDTO::getValor)
                .reduce(0d, (a, b) -> a + b);
    }

    public Double getValorPago() {
        return itens.stream()
                .filter(dto -> !dto.isLancamento())
                .map(ControlePagamentoItemDTO::getValor)
                .reduce(0d, (a, b) -> a + b);
    }

    public Double getValorAPagar() {
        return itens.stream()
                .map(ControlePagamentoItemDTO::getValorFinal)
                .reduce(0d, (a, b) -> a + b);
    }

    public ControlePagamentoDTO sort() {
        Collections.sort(itens);
        return this;
    }

}
