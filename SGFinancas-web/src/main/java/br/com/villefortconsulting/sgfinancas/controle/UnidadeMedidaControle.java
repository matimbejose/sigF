package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UnidadeMedidaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UnidadeMedidaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.UnidadeMedidaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
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
public class UnidadeMedidaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UnidadeMedidaService service;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @Inject
    private CadastroControle cadastroControle;

    private UnidadeMedida unidadeMedidaSelecionada;

    private LazyDataModel<UnidadeMedida> model;

    private UnidadeMedidaFiltro filtro = new UnidadeMedidaFiltro();

    private UnidadeMedidaDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public void cria() {
        service.populaUnidadeMedida();
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_UNIDADE_MEDIDA.getChave()).getDescricao());
        return "/restrito/unidadeMedida/cadastroUnidadeMedida.xhtml";
    }

    public List<UnidadeMedida> getUnidadeMedidas() {
        return service.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setUnidadeMedidaSelecionada(new UnidadeMedida());
        return "/restrito/unidadeMedida/cadastroUnidadeMedida.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/unidadeMedida/cadastroUnidadeMedida.xhtml";
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(unidade -> service.importDto((UnidadeMedidaDTO) unidade, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    public String doFinishAdd() {
        try {
            UnidadeMedida unidadeMedidaCadastrado = service.findByDescricao(unidadeMedidaSelecionada.getDescricao());
            if (Objects.nonNull(unidadeMedidaCadastrado)
                    && (!unidadeMedidaCadastrado.getId().equals(unidadeMedidaSelecionada.getId()))) {
                createFacesErrorMessage("Existe uma unidade de medida cadastrada para a descrição informada.");
                return "/restrito/unidadeMedida/cadastroUnidadeMedida.xhtml";
            }
            service.salvar(unidadeMedidaSelecionada);
        } catch (Exception ex) {
            return "/restrito/unidadeMedida/listaUnidadeMedida.xhtml";
        }

        createFacesSuccessMessage("UnidadeMedida salvo com sucesso!");
        return "/restrito/unidadeMedida/listaUnidadeMedida.xhtml";
    }

    public String doFinishExcluir() {
        service.remover(unidadeMedidaSelecionada);
        return "/restrito/unidadeMedida/listaUnidadeMedida.xhtml";
    }

    public String doShowAuditoria() {
        return "/restrito/unidadeMedida/listaAuditoriaUnidadeMedida.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(unidadeMedidaSelecionada);
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("UNIDADE_MEDIDA_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Unidade de medida",
                    service.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    UnidadeMedidaDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new UnidadeMedidaDTO();
    }

}
