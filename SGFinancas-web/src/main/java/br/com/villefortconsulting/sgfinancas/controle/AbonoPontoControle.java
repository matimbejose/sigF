package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioFerias;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AbonoPontoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumMotivoAbono;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class AbonoPontoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private FuncionarioService funcionarioService;

    private FuncionarioFerias funcionarioFeriasSelecionado;

    private LazyDataModel<FuncionarioFerias> model;

    private AbonoPontoFiltro filtro = new AbonoPontoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, funcionarioService::quantidadeRegistrosFuncionarioFeriasFiltrados, funcionarioService::getListaFuncionarioFeriasFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_TURMA.getChave()).getDescricao());
        return "cadastroAbonoPonto.xhtml";
    }

    public Set<Map.Entry<String, String>> getMotivos() {
        return EnumMotivoAbono.listarMotivosAbono();
    }

    public String buscarMotivo(String motivo) {
        return EnumMotivoAbono.getDescricao(motivo);
    }

    public String doStartAdd() {

        funcionarioFeriasSelecionado = new FuncionarioFerias();

        return "cadastroAbonoPonto.xhtml";
    }

    public String doStartAlterar() {
        return "cadastroAbonoPonto.xhtml";
    }

    public String doFinishAdd() {

        try {
            funcionarioService.salvarFuncionarioFerias(funcionarioFeriasSelecionado);

            createFacesSuccessMessage("Abono salvo com sucesso!");
            return "listaAbonoPonto.xhtml";

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroAbonoPonto.xhtml";
        }
    }

    public String doFinishExcluir() {
        try {
            funcionarioService.removerFuncionarioFerias(funcionarioFeriasSelecionado);

            createFacesSuccessMessage("Abono excluído com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaAbonoPonto.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return funcionarioService.getDadosAuditoriaFuncionarioFeriasByID(funcionarioFeriasSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaAbonoPonto.xhtml";
    }

}
