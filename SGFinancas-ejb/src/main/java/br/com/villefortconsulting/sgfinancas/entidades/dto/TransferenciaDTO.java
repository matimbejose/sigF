package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ContaBancaria idContaOrigem;

    private ContaBancaria idContaDestino;

    private Date dataTransferencia;

    private Double valorTransferencia;

    private String observacao;

}
