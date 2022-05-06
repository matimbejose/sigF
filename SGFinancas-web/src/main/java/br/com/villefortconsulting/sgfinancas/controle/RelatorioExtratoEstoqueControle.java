package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
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
public class RelatorioExtratoEstoqueControle extends BasicControl implements Serializable {

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

    public void gerarRelatorioExtratoEstoque() {
        try {
            gerarPdf(relatorioService.gerarRelatorioExtratoEstoque(produtoSelecionado, empresaService.getEmpresa(), dataInicio, dataFim), "Extrato de estoque");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(RelatorioExtratoEstoqueControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Erro ao Visualizar relatório!");
        }
    }

    public List<Produto> produtos() {
        return produtoService.listarProdutosPorEmpresa(empresaService.getEmpresa());
    }

}
