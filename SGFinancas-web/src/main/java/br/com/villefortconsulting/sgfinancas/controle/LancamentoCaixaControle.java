package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class LancamentoCaixaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private PlanoContaService planoContaService;

    private PlanoConta planoContaSelecionado;

    private Date dataInicio;

    private Date dataFim;

    private ContaBancaria contaBancaria;

    private CentroCusto centroCusto;

    public void preecherData() {
        dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFim = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public void gerarRelatorioLancamentoCaixa() {
        try {
            gerarPdf(relatorioService.gerarRelatorioLancamentoCaixa(empresaService.getEmpresa(), planoContaSelecionado, dataInicio, dataFim, contaBancaria, centroCusto), "LancamentoNoCaixa.pdf");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(LancamentoCaixaControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public List<PlanoConta> getPlanoContas() {
        return planoContaService.listarPlanoContaPorEmpresa();
    }

}
