package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CentroCustoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CentroCustoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
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
public class CentroCustoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CentroCustoService centroCustoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private UFService ufService;

    @Inject
    private CadastroControle cadastroControle;

    private CentroCusto centroCustoSelecionado;

    private LazyDataModel<CentroCusto> model;

    private CentroCustoFiltro filtro = new CentroCustoFiltro();

    private CentroCustoDTO dtoCadastro;

    private UF ufSelecionado;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, centroCustoService::quantidadeRegistrosFiltrados, centroCustoService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CENTRO_CUSTO.getChave()).getDescricao());
        return "/restrito/centroCusto/cadastroCentroCusto.xhtml";
    }

    public List<CentroCusto> getCentroCustos() {
        return centroCustoService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setCentroCustoSelecionado(new CentroCusto());
        return "/restrito/centroCusto/cadastroCentroCusto.xhtml";
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(prod -> centroCustoService.importDto((CentroCustoDTO) prod, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/centroCusto/cadastroCentroCusto.xhtml";
    }

    public String doFinishAdd() {
        try {
            centroCustoService.salvar(centroCustoSelecionado);

            createFacesSuccessMessage("Centro de custo salvo com sucesso!");
            return "/restrito/centroCusto/listaCentroCusto.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/centroCusto/cadastroCentroCusto.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/centroCusto/listaAuditoriaCentroCusto.xhtml";
    }

    public String doFinishExcluir() {
        centroCustoSelecionado.setAtivo("N");
        centroCustoService.salvar(centroCustoSelecionado);
        createFacesSuccessMessage("Centro de custo desativado com sucesso.");
        return "/restrito/centroCusto/listaCentroCusto.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return centroCustoService.getDadosAuditoriaByID(centroCustoSelecionado);
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("CENTRO_CUSTO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Centro de custo",
                    centroCustoService.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    CentroCustoDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new CentroCustoDTO();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public void buscarEnderecoPorCep() {
        if (centroCustoSelecionado.getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(centroCustoSelecionado.getCep());

            ufSelecionado = cepDTO.getUf();
            centroCustoSelecionado.setIdCidade(cidadeService.buscar(cepDTO.getDescricaoCidade()));
            centroCustoSelecionado.setLogradouro(cepDTO.getEndereco());
            centroCustoSelecionado.setBairro(cepDTO.getBairro());
        }
    }

    public void buscarEnderecoPorCepDTO() {
        if (dtoCadastro.getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(dtoCadastro.getCep());

            ufSelecionado = cepDTO.getUf();
            dtoCadastro.setCidade(cepDTO.getDescricaoCidade());
            dtoCadastro.setLogradouro(cepDTO.getEndereco());
            dtoCadastro.setBairro(cepDTO.getBairro());
        }
    }

}
