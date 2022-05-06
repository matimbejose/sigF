package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaItemOrdemServicoRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nomeItem;

    private String observacao;

    private Double valor;

    private Double desconto;

}
