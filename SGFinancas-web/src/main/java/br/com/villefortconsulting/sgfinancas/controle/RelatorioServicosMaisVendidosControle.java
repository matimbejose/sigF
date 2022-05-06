package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioServicosMaisVendidosControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    private Date dataInicio;

    private Date dataFim;

    public void preecherData() {
        dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFim = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public void gerarRelatorioServicosMaisVendidos() {
        try {
            gerarPdf(relatorioService.gerarRelatorioServicosMaisVendidos(empresaService.getEmpresa(), dataInicio, dataFim), "Serviços mais vendidos.pdf");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (RelatorioException re) {
            createFacesErrorMessage(re.getMessage());
        } catch (IOException | JRException ex) {
            Logger.getLogger(RelatorioServicosMaisVendidosControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Erro ao Visualizar relatório!");
        }
    }

    @Override
    public String dataHoje() {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        return out.format(DataUtil.getHoje());
    }

}
