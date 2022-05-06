package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ExtratoContaCorrenteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ExtratoContaCorrenteService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoExtrato;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtratoContaCorrenteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ExtratoContaCorrenteService extratoContaCorrenteService;

    @EJB
    private RelatorioService relatorioService;

    private ExtratoContaCorrente extratoContaCorrenteSelecionado;

    private LazyDataModel<ExtratoContaCorrente> model;

    private ExtratoContaCorrenteFiltro filtro = new ExtratoContaCorrenteFiltro();

    @PostConstruct
    public void postConstruct() {

    }

    public String buscarTipo(String tipo) {
        return EnumTipoExtrato.getDescricao(tipo);
    }

    public List<Object> getDadosAuditoria() {
        return extratoContaCorrenteService.getDadosAuditoriaByID(extratoContaCorrenteSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaExtratoContaCorrente.xhtml";
    }

    public void relatorioSintetico() {
        try {
            gerarExcel(relatorioService.extratoContaSintetico(filtro), "Relatório de extrato de conta");
        } catch (RelatorioException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (IOException ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o relatório");
        }
    }

    public void relatorioAnalitico() {
        try {
            gerarExcel(relatorioService.extratoContaAnaltico(filtro), "Relatório de extrato de conta");
        } catch (RelatorioException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (IOException ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o relatório");
        }
    }

}
