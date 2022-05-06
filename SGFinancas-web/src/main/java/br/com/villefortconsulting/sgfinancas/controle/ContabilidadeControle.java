package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.ContabilidadePlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContabilidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ContabilidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContabilidadeControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ContabilidadeService contabilidadeService;

    @EJB
    private CidadeService cidadeService;

    @Inject
    private CadastroControle cadastroControle;

    private Contabilidade contabilidadeSelecionada;

    private String descricao;

    private String grupoContabil;

    private UF ufSelecionado;

    private transient Part partLogo;

    private transient Part part;

    private List<ContabilidadePlanoConta> lancamentos = new LinkedList<>();

    private LazyDataModel<Contabilidade> model;

    private ContabilidadeFiltro filtro = new ContabilidadeFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                contabilidadeService::quantidadeRegistrosFiltrados,
                contabilidadeService::getListaFiltrada,
                filter -> filter.setUsuario(getUsuarioLogado()));
    }

    private void carregarUF() {
        if (contabilidadeSelecionada.getIdCidade() != null) {
            ufSelecionado = contabilidadeSelecionada.getIdCidade().getIdUF();
        } else {
            ufSelecionado = null;
        }
    }

    public String doFinishAdd() {

        try {

            // associando logo a contabilidade
            contabilidadeSelecionada.setLogo(contabilidadeService.getLogo(partLogo, contabilidadeSelecionada));

            // salva contabilidade
            contabilidadeSelecionada = contabilidadeService.salvar(contabilidadeSelecionada);

            if (lancamentos != null && !lancamentos.isEmpty()) {
                contabilidadeService.salvarLancamentos(lancamentos, contabilidadeSelecionada);
            }

            createFacesSuccessMessage("Contabilidade salva com sucesso.");

            return "listaContabilidade.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroContabilidade.xhtml";
        }
    }

    public String doStartAdd() {

        this.contabilidadeSelecionada = new Contabilidade();

        this.ufSelecionado = null;

        this.lancamentos = null;

        return "/restrito/contabilidade/cadastroContabilidade.xhtml";
    }

    public String doStartAlterar() {

        carregarUF();

        lancamentos = null;

        return "cadastroContabilidade.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaContabilidade.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return contabilidadeService.getDadosAuditoriaByID(contabilidadeSelecionada);
    }

    public void buscarEnderecoPorCep() {
        if (contabilidadeSelecionada.getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(contabilidadeSelecionada.getCep());

            ufSelecionado = cepDTO.getUf();
            contabilidadeSelecionada.setIdCidade(cepDTO.getCidade());
            contabilidadeSelecionada.setEndereco(cepDTO.getEndereco());
            contabilidadeSelecionada.setBairro(cepDTO.getBairro());
        }
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public StreamedContent getLogo() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (contabilidadeSelecionada != null && contabilidadeSelecionada.getLogo() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(contabilidadeSelecionada.getLogo()), "image/jpeg");
        }
        return null;
    }

    public void atualizaLista() {
        lancamentos = obterContabilidadePlanoConta();
    }

    public List<ContabilidadePlanoConta> obterListaContabilidadePlanoConta() {
        return lancamentos;
    }

    public List<Contabilidade> getContabilidades() {
        return contabilidadeService.listarContabilidade();
    }

    public List<ContabilidadePlanoConta> obterContabilidadePlanoConta() {
        if (contabilidadeSelecionada != null) {
            lancamentos = contabilidadeService.obterContabilidadePlanoConta(contabilidadeSelecionada);
        }

        if (StringUtils.isNotEmpty(grupoContabil) && !grupoContabil.equals("T")) {
            lancamentos.removeIf(pc -> !pc.getIdPlanoContaPadrao().getCodigo().substring(0, 1).equals(grupoContabil));
        }

        if (StringUtils.isNotEmpty(descricao)) {
            lancamentos.removeIf(pc -> !pc.getIdPlanoContaPadrao().getDescricao().toUpperCase().contains(descricao.toUpperCase())
                    && !pc.getIdPlanoContaPadrao().getCodigo().toUpperCase().contains(descricao.toUpperCase()));
        }

        return lancamentos;
    }

    public void atualizaValor(ContabilidadePlanoConta contabilidade) {
        if (StringUtils.isEmpty(contabilidade.getCodigo())) {
            contabilidade.setCodigo("-");
        }
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("CONTABILIDADE_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Contabilidade",
                    contabilidadeService.hasAny(),
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
