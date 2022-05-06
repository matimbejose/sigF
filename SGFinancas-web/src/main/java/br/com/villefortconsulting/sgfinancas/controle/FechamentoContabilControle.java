package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FechamentoContabilDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FechamentoContabilFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
public class FechamentoContabilControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaService contaService;

    @EJB
    private EmpresaService empresaService;

    private FechamentoContabilFiltro filtro = new FechamentoContabilFiltro();

    private List<ContaParcela> parcelasSelecionadas = new LinkedList<>();

    private Integer mes;

    private Integer ano;

    private String fechada;

    private Date dataInicio;

    private Date dataFim;

    public String getPrimeiraDataDeFechamento() {
        return contaService.getPrimeiraDataDeFechamento();
    }

    public List<FechamentoContabilDTO> listarFechamentoContabil() {
        return contaService.listarFechamentoContabil(dataInicio, dataFim);
    }

    public String doStartFecharParcela() {

        parcelasSelecionadas = contaService.listarParcelasMes(mes, ano, fechada);

        return "listaParcelaFechamentoContabil.xhtml";
    }

    public String doFinishFecharParcela() {
        parcelasSelecionadas.stream().forEach(p -> p.setFechada("S"));
        contaService.atualizarParcelas(parcelasSelecionadas);

        createFacesSuccessMessage("Fechamento realizado com sucesso!");
        return "listaFechamentoContabil.xhtml";
    }

    public String doFinishEstornarParcela() {
        parcelasSelecionadas.stream().forEach(p -> p.setFechada("N"));
        contaService.atualizarParcelas(parcelasSelecionadas);

        createFacesSuccessMessage("Estorno realizado com sucesso!");
        return "listaFechamentoContabil.xhtml";
    }

    public StreamedContent gerarRelatorioFechamentoContabil() {
        try {
            Empresa empresa = empresaService.getEmpresa();
            String[][] excel = contaService.criarArquivoExcelFechamentoContabil(parcelasSelecionadas);
            return FileUtil.createAndDownloadArquivoExcel(FileUtil.DIRETORIO_UPLOAD, "Fechamento contábil - " + empresa.getDescricao() + ".xls", excel);
        } catch (IOException e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public String doShowAuditoria() {
        return "listaAuditoriaFechamentoContabil.xhtml";
    }

    public String buscarMes(Integer mes) {
        return DataUtil.obterNomeMes(mes.toString());
    }

}
