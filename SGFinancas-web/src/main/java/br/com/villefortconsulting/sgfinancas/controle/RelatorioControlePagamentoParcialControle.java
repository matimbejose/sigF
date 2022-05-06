package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.operator.MinMax;
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
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioControlePagamentoParcialControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private PlanoContaService planoContaService;

    private Fornecedor fornecedorSelecionado;

    private MinMax<Date> data;

    public void cleanFiltro() {
        if (data == null) {
            data = new MinMax<>();
        }
    }

    public StreamedContent gerarRelatorio() {
        try {
            gerarExcel(relatorioService.gerarRelatorioSaldoDevedorPorFornecedor(fornecedorSelecionado, data), "Relatório controle de pagamentos parciais");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

}
