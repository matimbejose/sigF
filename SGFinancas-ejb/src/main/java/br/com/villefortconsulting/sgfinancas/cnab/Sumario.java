package br.com.villefortconsulting.sgfinancas.cnab;

import java.math.BigDecimal;
import org.jrimum.texgit.Record;

public class Sumario {

    private Record trailler;

    public Sumario(Record trailler) {
        if (trailler != null) {
            this.trailler = trailler;
        } else {
            throw new IllegalArgumentException("Trailler nulo!");
        }
    }

    public Integer getQuantidadeDeTitulosEmCobranca() {
        return trailler.getValue("QuantidadeDeTitulosEmCobranca");
    }

    public BigDecimal getValorTotalEmCobranca() {
        return trailler.getValue("VlrTotalCobSimples");
    }

    public Integer getQtdEntradaConfirmadaC02() {
        return trailler.getValue("QtdEntradaConfirmadaC02");
    }

    public BigDecimal getValEntradaConfirmadaC02() {
        return trailler.getValue("ValEntradaConfirmadaC02");
    }

    public BigDecimal getValTotLiquidacaoC06() {
        return trailler.getValue("ValTotLiquidacaoC06");
    }

    public Integer getQtdLiquidacaoC06() {
        return trailler.getValue("QtdLiquidacaoC06");
    }

    public BigDecimal getValLiquidacaoC06() {
        return trailler.getValue("ValLiquidacaoC06");
    }

    public Integer getQtdBaixaC09C10() {
        return trailler.getValue("QtdBaixaC09C10");
    }

    public BigDecimal getValBaixaC09C10() {
        return trailler.getValue("ValBaixaC09C10");
    }

    public Integer getQtdAbatimentoCanceladoC13() {
        return trailler.getValue("QtdAbatimentoCanceladoC13");
    }

    public BigDecimal getValAbatimentoCanceladoC13() {
        return trailler.getValue("ValAbatimentoCanceladoC13");
    }

    public Integer getQtdVencimentoAlteradoC14() {
        return trailler.getValue("QtdVencimentoAlteradoC14");
    }

    public BigDecimal getValVencimentoAlteradoC14() {
        return trailler.getValue("ValVencimentoAlteradoC14");
    }

    public Integer getQtdAbatimentoConcedidoC12() {
        return trailler.getValue("QtdAbatimentoConcedidoC12");
    }

    public BigDecimal getValAbatimentoConcedidoC12() {
        return trailler.getValue("ValAbatimentoConcedidoC12");
    }

    public Integer getQtdConfirmacaoInstProtestoC19() {
        return trailler.getValue("QtdConfirmacaoInstProtestoC19");
    }

    public BigDecimal getValConfirmacaoInstProtestoC19() {
        return trailler.getValue("ValConfirmacaoInstProtestoC19");
    }

}
