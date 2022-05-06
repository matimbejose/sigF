package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControleFinanceiroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomePlanoConta;

    private String nomeCliente;

    private String valorStr;

    private Integer ano;

    private Integer mes;

    private Double valor;

    private boolean pago;

    public ControleFinanceiroDTO(String nomePlanoConta, String nomeCliente, Date data, Double valor, String situacao) {
        this.nomePlanoConta = nomePlanoConta;
        this.nomeCliente = nomeCliente;
        this.valorStr = "R$ " + NumeroUtil.formatarCasasDecimais(valor, 2, false);
        this.valor = valor;
        this.ano = DataUtil.getAno(data);
        this.mes = DataUtil.getMes(data);
        this.pago = EnumSituacaoConta.PAGA.getSituacao().equals(situacao);
    }

}
