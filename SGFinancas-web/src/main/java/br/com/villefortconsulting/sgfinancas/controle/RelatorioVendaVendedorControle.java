package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
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
public class RelatorioVendaVendedorControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private VendaService vendaService;

    @EJB
    private RelatorioService relatorioService;

    private Usuario usuarioSelecionado;

    private Date dataInicio;

    private Date dataFim;

    public void preecherData() {
        dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFim = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public void gerarRelatorioVendasPorVendedor() {
        try {
            gerarPdf(relatorioService.gerarRelatorioVendasVendedor(usuarioSelecionado, empresaService.getEmpresa(), dataInicio, dataFim), "VendasPorVendedor.pdf");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(RelatorioVendaVendedorControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public List<Usuario> getVendedores() {
        return vendaService.listarUsuario(empresaService.getEmpresa());
    }

}
