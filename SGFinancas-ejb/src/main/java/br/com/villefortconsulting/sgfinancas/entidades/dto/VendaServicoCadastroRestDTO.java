package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaServicoCadastroRestDTO implements Serializable {

    public VendaServicoCadastroRestDTO(Servico servico) {
        this.servico = servico.getId();
        this.custo = servico.getCustoServico();
        this.valorVenda = servico.getValorVenda();
        this.quantidade = 1;
    }

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer servico;

    private Double custo;

    private Double valorVenda;

    private Double desconto;

    private Double pontos;

    private String detalhesItem;

    private Integer quantidade;

}
