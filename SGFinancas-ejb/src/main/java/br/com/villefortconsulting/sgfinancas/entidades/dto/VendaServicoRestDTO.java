package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaServicoRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private ServicoDTO servico;

    private Double custo;

    private Double valorVenda;

    private Double desconto;

    private Double pontos;

    private String detalhesItem;

    private Double quantidade;

}
