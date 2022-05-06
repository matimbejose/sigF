package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServicoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private ClienteDTO cliente;

    private String observacao;

    private FuncionarioDTO funcionarioEntrada;

    private FuncionarioDTO funcionarioSaida;

    private Double valor;

    private String formaPagamento;

    private List<StatusOrdemServicoDTO> statusOrdemDeServico = new LinkedList<>();

    private List<ItensOrdemServicoDTO> itensOrdemDeServico = new LinkedList<>();

    private ContaRestDTO conta;

    private Date horario;

    private Date dataInicio;

    private Date dataTermino;

}
