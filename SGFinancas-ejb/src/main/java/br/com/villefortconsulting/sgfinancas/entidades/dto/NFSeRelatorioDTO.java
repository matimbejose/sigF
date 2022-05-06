package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.report.Cell;
import br.com.villefortconsulting.util.report.Row;
import br.com.villefortconsulting.util.report.Style;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFSeRelatorioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String numero = "";

    private String status = "";

    private String dataEmissao = "";

    private String dataVencimento = "";

    private String tomador = "";

    private String convenio = "";

    private Double valor = 0d;

    private String situacao = "";

    private Double pISS = 0d;

    private Double pIR = 0d;

    private Double pPIS = 0d;

    private Double pCOFINS = 0d;

    private Double pCSLL = 0d;

    public Double getIssRetido() {
        return valor * pISS;
    }

    public Double getIssTotal() {
        return valor * pISS;
    }

    public Double getIssRecolher() {
        return getIssRetido() - getIssTotal();
    }

    public Double getIrpjRetido() {
        return valor * pIR;
    }

    public Double getIrpjTotal() {
        return valor * pIR;
    }

    public Double getIrpjRecolher() {
        return getIrpjRetido() - getIrpjTotal();
    }

    public Double getPisRetido() {
        return valor * pPIS;
    }

    public Double getPisTotal() {
        return valor * pPIS;
    }

    public Double getPisRecolher() {
        return getPisRetido() - getPisTotal();
    }

    public Double getCofinsRetido() {
        return valor * pCOFINS;
    }

    public Double getCofinsTotal() {
        return valor * pCOFINS;
    }

    public Double getCofinsPagar() {
        return getCofinsRetido() - getCofinsTotal();
    }

    public Double getCsllRetido() {
        return valor * pCSLL;
    }

    public Double getCsllTotal() {
        return valor * pCSLL;
    }

    public Double getCsllPagar() {
        return getCsllRetido() - getCsllTotal();
    }

    public Double getImpostosTotal() {
        return getIrpjTotal() + getPisTotal() + getCofinsTotal() + getCsllTotal();
    }

    public Double getImpostosTotalRs() {
        return getIssTotal() + getIrpjTotal() + getPisTotal() + getCofinsTotal() + getCsllTotal();
    }

    public Double getImpostosTotalP() {
        return getImpostosTotalRs() / valor;
    }

    public Double getPagar() {
        return getIssRetido() + getIssTotal() + getIssRecolher() + getIrpjRetido() + getIrpjTotal() + getIrpjRecolher() + getPisRetido()
                + getPisTotal() + getPisRecolher() + getCofinsRetido() + getCofinsTotal() + getCofinsPagar() + getCsllRetido();
    }

    public Double getReceber() {
        return valor - getPagar();
    }

    public Double getLiquido() {
        return valor - getImpostosTotalRs();
    }

    public void setImpostos(Cliente cliente) {
        pISS = NumeroUtil.somar(cliente.getValorISS(), 0d) / 100;
        pIR = NumeroUtil.somar(cliente.getValorIR(), 0d) / 100;
        pPIS = NumeroUtil.somar(cliente.getValorPIS(), 0d) / 100;
        pCOFINS = NumeroUtil.somar(cliente.getValorCOFINS(), 0d) / 100;
        pCSLL = NumeroUtil.somar(cliente.getValorCSLL(), 0d) / 100;
    }

    public void setSituacao(ContaParcela cp) {
        switch (EnumSituacaoConta.retornaEnumSelecionado(cp.getSituacao())) {
            case NAO_PAGA:
                situacao = "Em aberto";
                break;
            case PAGA_PARCIALMENTE:
                if (cp.getDataPagamento().compareTo(DataUtil.getHoje()) > 0) {
                    situacao = "Vencido";
                } else {
                    situacao = "Pago parcialmente";
                }
                break;
            case PAGA:
                situacao = "Pago";
                break;
            default:
                break;
        }

    }

    public Row getReportRow(Style valor, Style percent) {
        return new Row(
                new Cell(getNumero()), new Cell(getStatus()), new Cell(getDataEmissao()), new Cell(getDataVencimento()), new Cell(getTomador()), new Cell(getConvenio()), new Cell(getValor(), valor),
                new Cell(getIssRetido(), valor), new Cell(getIssTotal(), valor), new Cell(getIssRecolher(), valor),// ISS
                new Cell(getIrpjRetido(), valor), new Cell(getIrpjTotal(), valor), new Cell(getIrpjRecolher(), valor),// IRPJ
                new Cell(getPisRetido(), valor), new Cell(getPisTotal(), valor), new Cell(getPisRecolher(), valor),// PIS
                new Cell(getCofinsRetido(), valor), new Cell(getCofinsTotal(), valor), new Cell(getCofinsPagar(), valor),// COFINS
                new Cell(getCsllRetido(), valor), new Cell(getCsllTotal(), valor), new Cell(getCsllPagar(), valor),// CSLL
                new Cell(getImpostosTotal(), valor), new Cell(getImpostosTotalRs(), valor), new Cell(getImpostosTotalP() / 100, percent),// Impostos
                new Cell(getPagar(), valor), new Cell(getReceber(), valor), new Cell(getLiquido(), valor), new Cell(getSituacao())
        );
    }

}
