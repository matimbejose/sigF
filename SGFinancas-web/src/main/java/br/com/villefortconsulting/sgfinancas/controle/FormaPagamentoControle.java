package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormaPagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormaPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
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
public class FormaPagamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @Inject
    private CadastroControle cadastroControle;

    private FormaPagamento formaPagamentoSelecionado;

    private LazyDataModel<FormaPagamento> model;

    private FormaPagamentoFiltro filtro = new FormaPagamentoFiltro();

    private FormaPagamentoDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, formaPagamentoService::quantidadeRegistrosFiltrados, formaPagamentoService::getListaFiltrada);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CLIENTE.getChave()).getDescricao());
        return "/restrito/formaPagamento/cadastroFormaPagamento.xhtml";
    }

    @Override
    public void cleanCache() {
        this.formaPagamentoSelecionado = new FormaPagamento();
    }

    public List<FormaPagamento> getFormasPagamento() {
        return formaPagamentoService.listarFormaDePagamentoAtivo();
    }

    public String doStartAdd() {
        cleanCache();
        return "/restrito/formaPagamento/cadastroFormaPagamento.xhtml";
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(prod -> formaPagamentoService.importDto((FormaPagamentoDTO) prod, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    public String doStartAlterar() {
        return "cadastroFormaPagamento.xhtml";
    }

    public String doFinishAdd() {
        try {
            // salvando formaPagamento
            FormaPagamento formaPagamentoCadastrado = formaPagamentoService.findByDescricao(formaPagamentoSelecionado.getDescricao());
            if (Objects.nonNull(formaPagamentoCadastrado)
                    && (!formaPagamentoCadastrado.getId().equals(formaPagamentoSelecionado.getId()))) {
                createFacesErrorMessage("Existe uma forma de pagamento cadastrada para a descrição informada.");
                return "/restrito/formaPagamento/cadastroFormaPagamento.xhtml";
            }
            formaPagamentoService.salvar(formaPagamentoSelecionado);
            createFacesSuccessMessage("Forma pagamento salva com sucesso!");
            return "/restrito/formaPagamento/listaFormaPagamento.xhtml";

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/formaPagamento/cadastroFormaPagamento.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/formaPagamento/listaAuditoriaFormaPagamento.xhtml";
    }

    public String doFinishExcluir() {
        try {
            formaPagamentoSelecionado.setAtivo("N");
            formaPagamentoService.salvar(formaPagamentoSelecionado);
            createFacesSuccessMessage("Forma pagamento excluída com sucesso");
            return "/restrito/formaPagamento/listaFormaPagamento.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage("Forma de pagamento já utilizada em outras transações");
            return "/restrito/formaPagamento/listaFormaPagamento.xhtml";
        }
    }

    public List<Object> getDadosAuditoria() {
        return formaPagamentoService.getDadosAuditoriaByID(formaPagamentoSelecionado);
    }

    public String getEnumMeioPagamentoLabel(String codigo) {
        EnumMeioDePagamento emp = EnumMeioDePagamento.retornaEnumSelecionado(codigo);
        return emp == null ? "Não vinculado" : emp.getDescricao();
    }

    public EnumMeioDePagamento[] getEnumMeioDePagamentoList() {
        return EnumMeioDePagamento.values();
    }

    public boolean isCartao() {
        return formaPagamentoSelecionado.getCodigoNfe() != null
                && (formaPagamentoSelecionado.getCodigoNfe().equals(EnumMeioDePagamento.CARTAO_DE_CREDITO.getCodigo())
                || formaPagamentoSelecionado.getCodigoNfe().equals(EnumMeioDePagamento.CARTAO_DE_DEBITO.getCodigo()));
    }

    public List<FormaPagamento> listarFormas() {
        return formaPagamentoService.listarFormaDePagamentoAtivo();
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("FORMA_PAGAMENTO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Forma de pagamento",
                    formaPagamentoService.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    FormaPagamentoDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new FormaPagamentoDTO();
    }

}
