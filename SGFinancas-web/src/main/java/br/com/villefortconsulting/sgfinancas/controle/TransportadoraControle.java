package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Transportadora;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TransportadoraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TransportadoraFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.TransportadoraService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
public class TransportadoraControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TransportadoraService transportadoraService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private UFService ufService;

    @EJB
    private CidadeService cidadeService;

    @Inject
    private CadastroControle cadastroControle;

    private Transportadora transportadoraSelecionado;

    private LazyDataModel<Transportadora> model;

    private TransportadoraFiltro filtro = new TransportadoraFiltro();

    private UF ufSelecionado;

    private TransportadoraDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, transportadoraService::quantidadeRegistrosFiltrados, transportadoraService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_TRANSPORTADORA.getChave()).getDescricao());
        return "/restrito/transportadora/cadastroTransportadora.xhtml";
    }

    @Override
    public void cleanCache() {
        transportadoraSelecionado = new Transportadora();
        this.ufSelecionado = null;
    }

    public List<Transportadora> getTransportadoras() {
        return transportadoraService.listar();
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public void buscarEnderecoPorCep() {
        if (transportadoraSelecionado.getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(transportadoraSelecionado.getCep());

            ufSelecionado = cepDTO.getUf();
            transportadoraSelecionado.setIdCidade(cepDTO.getCidade());
            transportadoraSelecionado.setEndereco(cepDTO.getEndereco());
            transportadoraSelecionado.setBairro(cepDTO.getBairro());
        }
    }

    public String doStartAdd() {
        cleanCache();
        return "/restrito/transportadora/cadastroTransportadora.xhtml";
    }

    public String doStartAlterar() {
        return "/restrito/transportadora/cadastroTransportadora.xhtml";
    }

    public void validarEmail() {
        if (!EmailUtil.validarEmail(transportadoraSelecionado.getEmail(), false)) {
            createFacesErrorMessage("Email inválido");
            transportadoraSelecionado.setEmail(null);
        }
    }

    public String doFinishAdd() {
        try {
            Transportadora transportadoraCadastrado = transportadoraService.findByCnpjDescricao(transportadoraSelecionado.getCnpj(), transportadoraSelecionado.getDescricao());
            if (Objects.nonNull(transportadoraCadastrado)
                    && (!transportadoraCadastrado.getId().equals(transportadoraSelecionado.getId()))) {
                createFacesErrorMessage("Existe uma transportadora cadastrada para o cnpj e descrição informada.");
                return "/restrito/transportadora/cadastroTransportadora.xhtml";
            }
            transportadoraService.salvar(transportadoraSelecionado);
            createFacesSuccessMessage("Transportadora salva com sucesso!");
            return "/restrito/transportadora/listaTransportadora.xhtml";
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/transportadora/cadastroTransportadora.xhtml";
        }
    }

    public String doFinishExcluir() {
        try {
            transportadoraService.remover(transportadoraSelecionado);
            createFacesSuccessMessage("Transportadora excluída com sucesso.");
            return "/restrito/transportadora/listaTransportadora.xhtml";

        } catch (Exception e) {

            createFacesErrorMessage(e.getMessage());
            return "/restrito/transportadora/listaTransportadora.xhtml";

        }
    }

    public List<Object> getDadosAuditoria() {
        return transportadoraService.getDadosAuditoriaByID(transportadoraSelecionado);
    }

    public String doShowAuditoria() {
        return "/restrito/transportadora/listaAuditoriaTransportadora.xhtml";
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("TRANSPORTADORA_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Transportadora",
                    transportadoraService.hasAny(),
                    false,
                    this::doStartAdd,
                    null);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

}
