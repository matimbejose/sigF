package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaPagarReceberDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mes;

    private String nomeMes;

    private Double valorPagar;

    private Double valorReceber;

    private Double necessidadeCaixa;

}
