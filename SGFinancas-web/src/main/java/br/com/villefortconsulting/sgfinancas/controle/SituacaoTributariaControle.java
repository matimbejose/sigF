package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.SituacaoTributaria;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SituacaoTributariaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.SituacaoTributariaService;
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
public class SituacaoTributariaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SituacaoTributariaService situacaoTributariaService;

    private SituacaoTributaria situacaoTributariaSelecionado;

    private LazyDataModel<SituacaoTributaria> model;

    private SituacaoTributariaFiltro filtro = new SituacaoTributariaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, situacaoTributariaService::quantidadeRegistrosFiltrados, situacaoTributariaService::getListaFiltrada);
    }

    public List<SituacaoTributaria> getSituacaoTributarias() {
        return situacaoTributariaService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setSituacaoTributariaSelecionado(new SituacaoTributaria());
        return "cadastroSituacaoTributaria.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroSituacaoTributaria.xhtml";
    }

    public String doFinishAdd() {

        if (existsViolationsForJSF(situacaoTributariaSelecionado)) {
            return "listaSituacaoTributaria.xhtml";
        }

        situacaoTributariaService.salvar(situacaoTributariaSelecionado);

        createFacesSuccessMessage("SituacaoTributaria salva com sucesso!");
        return "listaSituacaoTributaria.xhtml";
    }

    public String doFinishExcluir() {
        situacaoTributariaService.remover(situacaoTributariaSelecionado);
        return "listaSituacaoTributaria.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return situacaoTributariaService.getDadosAuditoriaByID(situacaoTributariaSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaSituacaoTributaria.xhtml";
    }

}
