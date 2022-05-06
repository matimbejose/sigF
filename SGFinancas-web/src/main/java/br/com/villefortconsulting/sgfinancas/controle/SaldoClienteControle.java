package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteMovimentacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.IOException;
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
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoClienteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteMovimentacaoService clienteMovimentacaoService;

    @EJB
    private RelatorioService relatorioService;

    private MinMax<Date> data;

    private Cliente clienteSelecionado;

    private String tipoRelatorio = "ANALITICO_PDF";

    private CentroCusto centroSelecionado;

    public void preencherData() {
        if (data == null) {
            data = new MinMax<>();
        }
        data.setMin(DataUtil.getPrimeiroDiaDoAnoCorrente());
        data.setMax(new Date());
    }

    public void cleanFiltro() {
        data = new MinMax<>();
    }

    public StreamedContent gerarRelatorioSaldoSintetico() {
        try {
            switch (tipoRelatorio) {
                case "ANALITICO_PDF":
                    gerarPdf(relatorioService.gerarRelatorioClienteMovimentacaoAnalitico(clienteSelecionado, data, centroSelecionado), "Relatório de movimentações analítico");
                    break;
                case "ANALITICO_EXCEL":
                    gerarExcel(relatorioService.gerarRelatorioAnaliticoSaldoCliente(clienteSelecionado, data, centroSelecionado), "Relatório de movimentações analítico");
                    break;
                case "SINTETICO":
                    gerarExcel(relatorioService.gerarRelatorioSineticoSaldoCliente(clienteMovimentacaoService.listaSaldo(data, centroSelecionado), clienteMovimentacaoService.getSaldoAnterior(data.getMin()), data), "Movimentações");
                    break;
                default:
                    throw new CadastroException("Tipo de relatório inválido", null);
            }

            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | IOException | JRException e) {
            Logger.getLogger(RelatorioService.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        } catch (RelatorioException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return null;
    }

}
