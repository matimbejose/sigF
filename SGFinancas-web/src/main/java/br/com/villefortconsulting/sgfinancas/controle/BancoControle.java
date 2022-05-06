package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.BancoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.BancoService;
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
public class BancoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BancoService bancoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Banco bancoSelecionado;

    private LazyDataModel<Banco> model;

    private BancoFiltro filtro = new BancoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, bancoService::quantidadeRegistrosFiltrados, bancoService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_BANCO.getChave()).getDescricao());
        return "cadastroBanco.xhtml";
    }

    public List<Banco> getBancos() {
        return bancoService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setBancoSelecionado(new Banco());
        return "cadastroBanco.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroBanco.xhtml";
    }

    public String doFinishAdd() {

        if (existsViolationsForJSF(bancoSelecionado)) {
            return "listaBanco.xhtml";
        }

        bancoService.salvar(bancoSelecionado);

        createFacesSuccessMessage("Banco salvo com sucesso!");
        return "listaBanco.xhtml";
    }

    public String doFinishExcluir() {
        bancoService.remover(bancoSelecionado);
        return "listaBanco.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return bancoService.getDadosAuditoriaByID(bancoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaBanco.xhtml";
    }

}
