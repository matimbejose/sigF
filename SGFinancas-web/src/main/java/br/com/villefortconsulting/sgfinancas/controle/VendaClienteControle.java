package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.util.DataUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class VendaClienteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private VendaService vendaService;

    @EJB
    private RelatorioService relatorioService;

    private Cliente clienteSelecionado;

    private Date dataInicio;

    private Date dataFim;

    public void preecherData() {
        dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFim = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public void gerarRelatorioVendasPorCliente() {
        try {
            gerarPdf(relatorioService.gerarRelatorioProdutosPorCliente(clienteSelecionado, empresaService.getEmpresa(), dataInicio, dataFim), "Produtos por cliente.pdf");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        }
    }

    public List<Cliente> getClientes() {
        return vendaService.listaCliente(empresaService.getEmpresa());
    }

}
