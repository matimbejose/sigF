package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcumuladoTipoMovimentacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RelatorioService relatorioService;

    private MinMax<Date> data;

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

    public StreamedContent gerarRelatorio() {
        try {
            gerarExcel(relatorioService.gerarRelatorioAcumuladoPorTipoMovimentacao(data), "Relatório total acumulado por tipo movimentacao");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (RelatorioException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return null;
    }

}
