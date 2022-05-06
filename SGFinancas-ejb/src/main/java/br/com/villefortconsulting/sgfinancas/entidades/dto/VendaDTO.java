package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mes;

    private String nomeMes;

    private Double valorProduto;

    private Double valorServico;

}
