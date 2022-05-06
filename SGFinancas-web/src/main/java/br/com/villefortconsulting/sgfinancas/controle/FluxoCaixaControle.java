package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaPagarReceberDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FluxoCaixaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FluxoCaixaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AnalisesService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
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
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.TreeNode;

@Named
@SessionScoped
@NoArgsConstructor
@AllArgsConstructor
public class FluxoCaixaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AnalisesService analisesService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private EmpresaService empresaService;

    @Getter
    @Setter
    private FluxoCaixaFiltro filtro = new FluxoCaixaFiltro();

    @Getter
    private List<String> meses = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "tot");

    @Getter
    private List<FluxoCaixaDTO> listaFluxoCaixa;

    @Getter
    private List<ContaPagarReceberDTO> listaNecessidadeCaixa;

    @Getter
    @Setter
    private CentroCusto centroCustoSelecionado;

    private transient TreeNode fluxoCaixa;

    public void init() {
        if ((listaFluxoCaixa == null || listaFluxoCaixa.isEmpty()) && filtro.getAno() == null) {
            filtro.setAno(DataUtil.getAnoCorrente());
        }
    }

    public TreeNode getFluxoCaixa() {
        if (fluxoCaixa == null) {
            obterFluxoCaixa();
        }
        return fluxoCaixa;
    }

    public void obterFluxoCaixa() {
        if (filtro.getAno() == null) {
            return;
        }

        Date dataInicial = DataUtil.converterStringParaDate("01/01/" + filtro.getAno());
        Date dataFinal = DataUtil.converterStringParaDate("31/12/" + filtro.getAno());

        listaFluxoCaixa = analisesService.obterFluxoCaixa(dataInicial, dataFinal, false, centroCustoSelecionado);
        fluxoCaixa = analisesService.getFluxoCaixa(filtro.getAno(), listaFluxoCaixa, centroCustoSelecionado);
        listaNecessidadeCaixa = analisesService.obterAnaliseNecessidadeCaixa(dataInicial, dataFinal, false);
    }

    public String obtervalorCaixa(String tipo, String mes) {
        if (listaNecessidadeCaixa == null || listaNecessidadeCaixa.isEmpty()) {
            return "-";
        }
        return listaNecessidadeCaixa.stream()
                .filter(p -> p.getMes().equals(mes)).findFirst()
                .map(dto -> {
                    switch (tipo) {
                        case "r":
                            return dto.getValorReceber();
                        case "d":
                            return dto.getValorPagar();
                        case "c":
                            return dto.getNecessidadeCaixa();
                        default:
                            return null;
                    }
                })
                .map(val -> NumeroUtil.converterValorParaMonetario(val, 2))
                .orElse("-");
    }

    public void gerarRelatorioFluxoCaixa() {
        try {
            gerarPdf(relatorioService.gerarRelatorioFluxoCaixa(listaFluxoCaixa, listaNecessidadeCaixa, filtro.getAno()), getNomeReport());
        } catch (RelatorioException | IOException | JRException ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
        }
    }

    public void gerarRelatorioFluxoCaixaExcel() {
        try {
            obterFluxoCaixa();
            gerarExcel(relatorioService.gerarRelatorioFluxoCaixaExcel(fluxoCaixa, filtro.getAno(), centroCustoSelecionado), getNomeReport());
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
        }
    }

    private String getNomeReport() {
        return "Fluxo de caixa " + filtro.getAno() + " - " + empresaService.getEmpresa().getDescricao();
    }

    public String[] getHeaderText() {
        return new String[]{"FLUXO DE CAIXA", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez", "Total"};
    }

}
