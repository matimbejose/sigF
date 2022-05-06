package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer venda;

    private Integer cliente;

    private Integer funcionario;

    private List<Integer> servicos;

    private Date horario;

    private String observacao;

    private String observacaoCliente;

    private Double desconto;

    private String formaPagamento;

    private Integer contaBancaria;

    private Integer planoConta;

    private String origem;

}
