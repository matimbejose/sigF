package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancariaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private Integer idBanco;

    private String nomeBanco;

    private Integer idPlanoConta;

    private String descricao;

    private String agencia;

    private String conta;

    private String tipoConta;

    private String tipoContaDescricao;

    private String situacao;

    private String situacaoDescricao;

    private Double saldoInicial;

    private Date dataSaldo;

    private Date dataCancelamento;

    private String digitoAgencia;

    private String digitoConta;

    private String operacao;

    private Integer carteira;

    private String cedente;

    private Banco banco;

}
