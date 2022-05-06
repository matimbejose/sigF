package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnaliseRecebimentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String situacao;

    private Date dataVencimento;

    private Date dataRecebimento;

    private Double valor;

    private Double valorRecebido;

    private String cliente;

}
