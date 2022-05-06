package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import java.io.Serializable;
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
public class RelatorioPosicaoEstoqueControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private ProdutoService produtoService;

    private Produto produtoSelecionado;

    private String tipoRelatorio;

    public void gerarRelatorioPosicaoEstoque() {
        try {
            gerarPdf(relatorioService.gerarRelatorioPosicaoEstoque(produtoSelecionado, empresaService.getEmpresa(), tipoRelatorio), "Posição de estoque atual.pdf");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (RelatorioException ex) {
            Logger.getLogger(RelatorioGiroEstoqueControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(RelatorioGiroEstoqueControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Erro ao Visualizar relatório!");
        }
    }

    public List<Produto> produtos() {
        return produtoService.listarProdutosPorEmpresa(empresaService.getEmpresa());
    }

}
