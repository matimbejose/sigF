package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Fabricante;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FabricanteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FabricanteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FabricanteService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
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
public class FabricanteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FabricanteService fabricanteService;

    @Inject
    private CadastroControle cadastroControle;

    private Fabricante fabricanteSelecionado;

    private LazyDataModel<Fabricante> model;

    private FabricanteFiltro filtro = new FabricanteFiltro();

    private FabricanteDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, fabricanteService);
    }

    @Override
    public void cleanCache() {
        this.fabricanteSelecionado = new Fabricante();
    }

    public String doStartAdd() {
        cleanCache();
        return "/restrito/fabricante/cadastroFabricante.xhtml";
    }

    public String doStartAlterar() {
        return "cadastroFabricante.xhtml";
    }

    public String doFinishAdd() {
        try {
            fabricanteSelecionado.setAtivo("S");
            fabricanteService.salvar(fabricanteSelecionado);
            createFacesSuccessMessage("Fabricante salvo com sucesso!");
            return "/restrito/fabricante/listaFabricante.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/fabricante/cadastroFabricante.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/fabricante/listaAuditoriaFabricante.xhtml";
    }

    public String doFinishExcluir() {
        try {
            fabricanteSelecionado.setAtivo("N");
            fabricanteService.salvar(fabricanteSelecionado);
            createFacesSuccessMessage("Fabricante excluído com sucesso");
            return "/restrito/fabricante/listaFabricante.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage("Não foi possível excluir o fabricante.");
            return "/restrito/fabricante/listaFabricante.xhtml";
        }
    }

    public List<Object> getDadosAuditoria() {
        return fabricanteService.getDadosAuditoriaByID(fabricanteSelecionado);
    }

    public List<Fabricante> getFabricantes() {
        return fabricanteService.listar();
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(fabricante -> fabricanteService.importDto((FabricanteDTO) fabricante, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("FABRICANTE_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Fabricante",
                    fabricanteService.hasAny(),
                    true,
                    this::doStartAdd,
                    this::doFinishImport,
                    FabricanteDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new FabricanteDTO();
    }

}
