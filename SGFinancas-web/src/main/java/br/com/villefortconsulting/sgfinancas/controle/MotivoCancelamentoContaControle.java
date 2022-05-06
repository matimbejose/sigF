package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.MotivoCancelamentoConta;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MotivoCancelamentoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.MotivoCancelamentoContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.MotivoCancelamentoContaService;
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
public class MotivoCancelamentoContaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MotivoCancelamentoContaService motivoCancelamentoContaService;

    @Inject
    private CadastroControle cadastroControle;

    private MotivoCancelamentoConta motivoCancelamentoContaSelecionado;

    private LazyDataModel<MotivoCancelamentoConta> model;

    private MotivoCancelamentoContaFiltro filtro = new MotivoCancelamentoContaFiltro();

    private MotivoCancelamentoContaDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, motivoCancelamentoContaService::quantidadeRegistrosFiltrados, motivoCancelamentoContaService::getListaFiltrada);
    }

    public List<MotivoCancelamentoConta> getMotivoCancelamentoContas() {
        return motivoCancelamentoContaService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setMotivoCancelamentoContaSelecionado(new MotivoCancelamentoConta());
        return "cadastroMotivoCancelamentoConta.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroMotivoCancelamentoConta.xhtml";
    }

    public String doFinishAdd() {
        motivoCancelamentoContaSelecionado.setAtivo("S");

        MotivoCancelamentoConta motivoCadastrado = motivoCancelamentoContaService.findByDescricao(motivoCancelamentoContaSelecionado.getDescricao());
        if (Objects.nonNull(motivoCadastrado)
                && (!motivoCadastrado.getId().equals(motivoCancelamentoContaSelecionado.getId()))) {
            createFacesErrorMessage("Existe um motivo de cancelamento de conta cadastrado para a descrição informada.");
            return "cadastroMotivoCancelamentoConta.xhtml";
        }
        motivoCancelamentoContaService.salvar(motivoCancelamentoContaSelecionado);
        createFacesSuccessMessage("Motivo salvo com sucesso!");
        return "listaMotivoCancelamentoConta.xhtml";

    }

    public String doFinishExcluir() {
        motivoCancelamentoContaSelecionado.setAtivo("N");
        motivoCancelamentoContaService.salvar(motivoCancelamentoContaSelecionado);
        return "listaMotivoCancelamentoConta.xhtml";
    }

    public List<MotivoCancelamentoConta> getMotivosCancelamento() {
        return motivoCancelamentoContaService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return motivoCancelamentoContaService.getDadosAuditoriaByID(motivoCancelamentoContaSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaMotivoCancelamentoConta.xhtml";
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(mcc -> motivoCancelamentoContaService.importDto((MotivoCancelamentoContaDTO) mcc, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        return new StatusCadastroDTO(
                "Motivo de cancelamento de conta",
                motivoCancelamentoContaService.hasAny(),
                false,
                this::doStartAdd,
                this::doFinishImport,
                MotivoCancelamentoContaDTO.class);
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new MotivoCancelamentoContaDTO();
    }

}
