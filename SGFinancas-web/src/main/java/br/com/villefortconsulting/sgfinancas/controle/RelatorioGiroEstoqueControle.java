package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.DataUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioGiroEstoqueControle extends BasicControl implements Serializable {

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
    
    private String tipoRelatorio;

    public void preecherData() {
        dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFim = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public void gerarRelatorioGiroEstoque() {
            try {
                gerarPdf(relatorioService.gerarRelatorioGiroEstoque(produtoSelecionado, empresaService.getEmpresa(), dataInicio, dataFim), "Giro de estoque");
                createFacesSuccessMessage("Relatório gerado com sucesso!");
            } catch (Exception ex) {
                createFacesErrorMessage(ex.getMessage());
            }
    }

    public List<Produto> produtos() {
        return produtoService.listarProdutosPorEmpresa(empresaService.getEmpresa());
    }

}
