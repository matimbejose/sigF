package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AnaliseContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
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
public class RelatorioAnalisePagamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private PlanoContaService planoContaService;

    private AnaliseContaFiltro filtro = new AnaliseContaFiltro();

    public void preecherData() {
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    public void cleanFiltro() {
        if (filtro == null) {
            filtro = new AnaliseContaFiltro();
        }
        preecherData();
    }

    public void baixarExcel(int ano, int mes, String codigoPlanoConta, CentroCusto centroCusto) {
        try {
            AnaliseContaFiltro filter = new AnaliseContaFiltro();
            filter.getData().setMin(DataUtil.converterStringParaDate("01/" + mes + "/" + ano));
            filter.getData().setMax(DataUtil.getUltimoDiaDoMes(filter.getData().getMin()));
            if(centroCusto!= null){
                filter.setCentroCusto(centroCusto);
            }
            filter.setPlanoConta(planoContaService.obterPlanoContaPorCodigo(codigoPlanoConta, empresaService.getEmpresa().getTenatID()));
            filter.setSituacaoConta(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
            gerarExcel(relatorioService.gerarExcelRelatorioAnalisePagamento(filter), "AnalisePagamento");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public void gerarRelatorioAnalisePagamento() {
        try {
            switch (filtro.getTipoRelatorio()) {
                case "excel":
                    gerarExcel(relatorioService.gerarExcelRelatorioAnalisePagamento(filtro), "AnalisePagamento");
                    break;
                case "pdf_analitico":
                    gerarPdf(relatorioService.gerarRelatorioAnalisePagamento(empresaService.getEmpresa(), filtro), "AnalisePagamento");
                    break;
                case "pdf_sintetico":
                    gerarPdf(relatorioService.gerarRelatorioAnalisePagamentoSintetico(empresaService.getEmpresa(), filtro), "AnalisePagamento");
                    break;
                default:
                    createFacesErrorMessage("Tipo de relatório inválido.");
                    return;
            }
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
        }
    }

}
