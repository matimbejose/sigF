package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TipoPagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TipoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.TipoPagamentoService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
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
public class TipoPagamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TipoPagamentoService tipoPagamentoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @Inject
    private CadastroControle cadastroControle;

    private TipoPagamento tipoPagamentoSelecionado;

    private LazyDataModel<TipoPagamento> model;

    private TipoPagamentoFiltro filtro = new TipoPagamentoFiltro();

    private TipoPagamentoDTO dtoCadastro = new TipoPagamentoDTO();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, tipoPagamentoService::quantidadeRegistrosFiltrados, tipoPagamentoService::getListaFiltrada);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_TIPOPAGAMENTO.getChave()).getDescricao());
        return "cadastroTipoPagamento.xhtml";
    }

    public List<TipoPagamento> getTipoPagamentos() {
        return tipoPagamentoService.listarTiposAtivos();
    }

    public String doStartAdd() {
        cleanCache();
        setTipoPagamentoSelecionado(new TipoPagamento());
        return "cadastroTipoPagamento.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroTipoPagamento.xhtml";
    }

    public String doFinishAdd() {
        try {
            tipoPagamentoService.salvar(tipoPagamentoSelecionado);
            createFacesSuccessMessage("Tipo de pagamento salvo com sucesso!");
            return "listaTipoPagamento.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroTipoPagamento.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "listaAuditoriaTipoPagamento.xhtml";
    }

    public String doFinishExcluir() {
        try {
            tipoPagamentoSelecionado.setAtivo("N");
            tipoPagamentoService.salvar(tipoPagamentoSelecionado);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaTipoPagamento.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return tipoPagamentoService.getDadosAuditoriaByID(tipoPagamentoSelecionado);
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(tp -> tipoPagamentoService.importDto((TipoPagamentoDTO) tp))
                .collect(Collectors.toList());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("TIPO_PAGAMENTO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Tipo de pagamento",
                    tipoPagamentoService.hasAny(),
                    true,
                    this::doStartAdd,
                    this::doFinishImport,
                    TipoPagamentoDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new TipoPagamentoDTO();
    }

}
