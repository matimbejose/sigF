package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoContabilDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dataLancamento;

    private String contaCredito;

    private String contaDebito;

    private Double valor;

    private String situacao;

}
