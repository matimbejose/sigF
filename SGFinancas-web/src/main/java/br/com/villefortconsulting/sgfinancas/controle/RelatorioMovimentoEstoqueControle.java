package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import java.io.IOException;
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
import net.sf.jasperreports.engine.JRException;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMovimentoEstoqueControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private ProdutoService produtoService;

    private Produto produtoSelecionado;

    private Date dataInicio;

    private Date dataFim;

    public void preecherData() {
        dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFim = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public void gerarRelatorioMovimentoEstoque() {
        try {
            gerarPdf(relatorioService.gerarRelatorioMovimentoEstoque(produtoSelecionado, empresaService.getEmpresa(), dataInicio, dataFim), "Movimento de estoque");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (RelatorioException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (IOException | JRException ex) {
            Logger.getLogger(RelatorioMovimentoEstoqueControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Erro ao Visualizar relatório!");
        }
    }

    public List<Produto> produtos() {
        return produtoService.listarProdutosPorEmpresa(empresaService.getEmpresa());
    }

}
