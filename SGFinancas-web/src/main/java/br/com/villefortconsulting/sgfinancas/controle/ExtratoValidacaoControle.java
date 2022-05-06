package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.ExtratoContaCorrenteService;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtratoValidacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ExtratoContaCorrenteService extratoContaCorrenteService;

    @EJB
    private ContaService contaService;

    private List<ExtratoContaCorrente> listaExtratosDuplicados;

    private List<ContaParcela> listaParcelasPagasSemExtrato;

    private List<ContaParcela> listaParcelasNaoPagasComExtrato;

    public String doStartAtualizarContasComExtratoDuplicado() {
        listaExtratosDuplicados = extratoContaCorrenteService.verificarRegistrosDuplicados();

        if (listaExtratosDuplicados != null && !listaExtratosDuplicados.isEmpty()) {
            return "listaExtratoDuplicado.xhtml";
        }
        createFacesErrorMessage(" A empresa não possui extratos duplicados");
        return "listaExtratoContaCorrente.xhtml";
    }

    public String doFinishAtualizarContasComExtratoDuplicado() {
        listaExtratosDuplicados.stream().forEach(extratoContaCorrente -> extratoContaCorrenteService.cancelarOperacaoExtrato(extratoContaCorrente.getIdContaParcela()));

        createFacesSuccessMessage(" Extratos duplicados excluídos com sucesso!");
        return "listaExtratoContaCorrente.xhtml";
    }

    public String doStartAtualizarParcelasPagasSemExtrato() {
        listaParcelasPagasSemExtrato = contaService.listarParcelasPagasSemExtrato();

        if (listaParcelasPagasSemExtrato != null && !listaParcelasPagasSemExtrato.isEmpty()) {
            return "listaParcelasPagasSemExtrato.xhtml";
        }
        createFacesErrorMessage(" A empresa não possui parcelas pagas sem extrato");
        return "listaExtratoContaCorrente.xhtml";
    }

    public String doFinishAtualizarParcelasPagasSemExtrato() {
        listaParcelasPagasSemExtrato.stream().forEach(parcela -> extratoContaCorrenteService.lancarOperacaoExtrato(parcela));

        createFacesSuccessMessage(" Extrato atualizado com sucesso! ");
        return "listaExtratoContaCorrente.xhtml";
    }

    public String doStartAtualizarParcelasNaoPagasComExtrato() {
        listaParcelasNaoPagasComExtrato = contaService.listarParcelasNaoPagasComExtrato();

        if (listaParcelasNaoPagasComExtrato != null && !listaParcelasNaoPagasComExtrato.isEmpty()) {
            return "listaParcelasNaoPagasComExtrato.xhtml";
        }
        createFacesErrorMessage(" A empresa não possui parcelas não pagas com extrato");
        return "listaExtratoContaCorrente.xhtml";
    }

    public String doFinishAtualizarParcelasNaoPagasComExtrato() {
        listaParcelasNaoPagasComExtrato.stream().forEach(parcela -> extratoContaCorrenteService.cancelarOperacaoExtrato(parcela));

        createFacesSuccessMessage(" Extrato atualizado com sucesso! ");
        return "listaExtratoContaCorrente.xhtml";
    }

    public String atualizarSaldoContaBancaria(ContaBancaria contaBancaria) {
        int numRegAtualizados = extratoContaCorrenteService.atualizarSaldoContaBancaria(contaBancaria);

        if (numRegAtualizados == 0) {
            createFacesErrorMessage(" A conta bancária nao possui extratos desatualizados ");
        } else {
            createFacesSuccessMessage(" Extrato atualizado com sucesso. Número de registros atualizados: " + numRegAtualizados);
        }
        return "listaExtratoContaCorrente.xhtml";
    }

    public String buscarSituacao(String situacao) {
        return EnumSituacaoConta.getDescricao(situacao);
    }

    public String mostraOrigem(Conta conta) {
        return EnumTipoConta.getDescricao(conta.getTipoConta());
    }

}
