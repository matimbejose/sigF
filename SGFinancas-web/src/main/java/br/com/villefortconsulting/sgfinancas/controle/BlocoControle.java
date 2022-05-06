package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Bloco;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.BlocoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.BlocoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
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
public class BlocoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BlocoService blocoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Bloco blocoSelecionado;

    private LazyDataModel<Bloco> model;

    private BlocoFiltro filtro = new BlocoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, blocoService::quantidadeRegistrosFiltrados, blocoService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_BANCO.getChave()).getDescricao());
        return "cadastroBloco.xhtml";
    }

    public List<Bloco> getBlocos() {
        return blocoService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setBlocoSelecionado(new Bloco());
        return "cadastroBloco.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroBloco.xhtml";
    }

    public String doFinishAdd() {
        blocoService.salvar(blocoSelecionado);

        createFacesSuccessMessage("Bloco salvo com sucesso!");
        return "listaBloco.xhtml";
    }

    public String doFinishExcluir() {
        blocoService.remover(blocoSelecionado);
        return "listaBloco.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return blocoService.getDadosAuditoriaByID(blocoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaBloco.xhtml";
    }

}
