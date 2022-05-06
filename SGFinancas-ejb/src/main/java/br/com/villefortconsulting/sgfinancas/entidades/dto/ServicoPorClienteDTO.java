package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoPorClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //Lista de produtos vendidos pelo vendedor Selecionado
    private String descricao;

    private Double valorVenda;

    private Double custoServico;

    private String nomeCliente;

    public ServicoPorClienteDTO(String nomeCliente, String descricao, Double valorVenda, Double custoServico) {
        this.descricao = descricao;
        this.valorVenda = valorVenda;
        this.custoServico = custoServico;
        this.nomeCliente = nomeCliente;

    }

}
