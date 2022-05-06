package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroPlanoContaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private PlanoContaDTO contaBancaria;

    private PlanoContaDTO cliente;

    private PlanoContaDTO fornecedor;

    private PlanoContaDTO produto;

    private PlanoContaDTO servico;

    private PlanoContaDTO transportadora;

    private PlanoContaDTO planoContaPadraoVenda;

    private PlanoContaDTO planoContaPadraoCompra;

}
