package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoContaLancamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaLancamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceteVerificacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PlanoContaLancamentoService planoContaLancamentoService;

    @EJB
    private RelatorioService relatorioService;

    private LazyDataModel<PlanoContaLancamentoContabil> model;

    private PlanoContaLancamentoFiltro filtro = new PlanoContaLancamentoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, planoContaLancamentoService::quantidadeRegistrosFiltrados, planoContaLancamentoService::getListaFiltrada);
    }

    public void gerarBalanceteVerificacao() throws IOException {
        Date dataInicio;
        Date dataFim;

        dataInicio = DataUtil.converterStringParaDate(filtro.getDataInicio());
        dataFim = DataUtil.converterStringParaDate(filtro.getDataFim());

        try {
            gerarPdf(relatorioService.gerarRelatorioLancamentoContabil(empresaService.getEmpresa(), dataInicio, dataFim), "BalanceteVerificacao.pdf");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException e) {
            createFacesErrorMessage("Erro ao Visualizar relatório!");
        }
    }

}
