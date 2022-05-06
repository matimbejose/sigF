package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSaldoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private Double saldo;

    private Double saldoAnterior;

    public ClienteSaldoDTO(String nome, Double saldo) {
        this.nome = nome;
        this.saldo = saldo;
    }

}
