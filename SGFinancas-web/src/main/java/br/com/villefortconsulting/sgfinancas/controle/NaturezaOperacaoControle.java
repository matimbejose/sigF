package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.NaturezaOperacao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NaturezaOperacaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.NaturezaOperacaoService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
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

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaturezaOperacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private NaturezaOperacaoService service;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private NaturezaOperacao naturezaOperacaoSelecionada;

    private LazyDataModel<NaturezaOperacao> model;

    private NaturezaOperacaoFiltro filtro = new NaturezaOperacaoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_NATUREZA_OPERACAO.getChave()).getDescricao());
        return "cadastroNaturezaOperacao.xhtml";
    }

    public List<NaturezaOperacao> getNaturezaOperacaos() {
        return service.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setNaturezaOperacaoSelecionada(new NaturezaOperacao());
        return "cadastroNaturezaOperacao.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroNaturezaOperacao.xhtml";
    }

    public String doFinishAdd() {
        if (existsViolationsForJSF(naturezaOperacaoSelecionada)) {
            return "listaNaturezaOperacao.xhtml";
        }

        service.salvar(naturezaOperacaoSelecionada);

        createFacesSuccessMessage("Natureza de Operação salva com sucesso!");
        return "listaNaturezaOperacao.xhtml";
    }

    public String doFinishExcluir() {
        service.remover(naturezaOperacaoSelecionada);
        return "listaNaturezaOperacao.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaNaturezaOperacao.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(naturezaOperacaoSelecionada);
    }

}
