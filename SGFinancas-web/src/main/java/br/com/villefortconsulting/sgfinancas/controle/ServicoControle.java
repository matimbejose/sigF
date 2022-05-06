package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.ServicoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.NcmService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ServicoService servicoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private NcmService ncmService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @EJB
    private ServicoProdutoService servicoProdutoService;

    @EJB
    private FuncionarioServicoService funcionarioservicoService;

    @Inject
    private CadastroControle cadastroControle;

    @Inject
    private DocumentoMapper documentoMapper;

    @Inject
    private VendaControle vendaControle;

    private Servico servicoSelecionado;

    private Ncm ncmSelecionado;

    private ProdutoCategoria produtoCategoriaSelecionado;

    private LazyDataModel<Servico> model;

    private ServicoFiltro filtro = new ServicoFiltro();

    private String mostraImportacao = "N";

    private ServicoProduto servicoProdutoSelecionado;

    private transient UploadedFile part;

    private ServicoDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, servicoService::quantidadeRegistrosFiltrados, servicoService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_PRODUTO.getChave()).getDescricao());
        return "/restrito/servico/cadastroServico.xhtml";
    }

    @Override
    public void cleanCache() {
        part = null;
    }

    public List<Ncm> buscarNcm(String descricao) {
        return ncmService.conferirCodigo(descricao);
    }

    public void esconderImportacao() {
        mostraImportacao = "N";
    }

    public String doStartAdd() {
        cleanCache();
        servicoSelecionado = new Servico();
        servicoSelecionado.setFuncionarioServicoList(new ArrayList<>());
        servicoSelecionado.setServicoProdutoList(new ArrayList<>());
        return "/restrito/servico/cadastroServico.xhtml";
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(prod -> servicoService.importDto((ServicoDTO) prod, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    public String doStartAlterar() {
        cleanCache();

        servicoSelecionado.setFuncionarioServicoList(funcionarioservicoService.getListaByServico(servicoSelecionado));
        servicoSelecionado.setServicoProdutoList(servicoProdutoService.getListaByServico(servicoSelecionado));
        if (servicoSelecionado.getIdDocumento() != null && servicoSelecionado.getIdDocumento().getId() != null) {
            servicoSelecionado.getIdDocumento().setDocumentoAnexoList(documentoAnexoService.listByDocumento(servicoSelecionado.getIdDocumento()));
        }

        return "/restrito/servico/cadastroServico.xhtml";
    }

    public String doFinishAdd() {
        try {
            if (servicoSelecionado.getIdDocumento() != null) {
                servicoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                        .forEach(doc -> doc.setIdDocumento(servicoSelecionado.getIdDocumento()));
                servicoSelecionado.setIdDocumento(documentoService.alterar(servicoSelecionado.getIdDocumento()));
            }

            servicoService.salvar(servicoSelecionado);

            vendaControle.setItensVendaDisponiveis(null);

            createFacesSuccessMessage("Serviço salvo com sucesso!");
            return "/restrito/servico/listaServico.xhtml";

        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/servico/cadastroServico.xhtml";
        }
    }

    public String doFinishExcluir() {
        try {
            servicoSelecionado.setAtivo("N");
            servicoSelecionado.setFuncionarioServicoList(servicoService.listarFuncionarioServico(servicoSelecionado));
            servicoSelecionado.setServicoProdutoList(servicoService.listarServicoProduto(servicoSelecionado));
            servicoService.salvar(servicoSelecionado);
            createFacesSuccessMessage("Serviço excluído com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "/restrito/servico/listaServico.xhtml";
    }

    public String doShowAuditoria() {
        return "/restrito/servico/listaAuditoriaServico.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return servicoService.getDadosAuditoriaByID(servicoSelecionado);
    }

    public List<AnexoDTO> listaAnexos() {
        if (servicoSelecionado.getIdDocumento() == null) {
            servicoSelecionado.setIdDocumento(new Documento());
            servicoSelecionado.getIdDocumento().setDocumentoAnexoList(new ArrayList<>());
            servicoSelecionado.getIdDocumento().setTenatID(getUsuarioLogado().getTenat());
        }
        List<AnexoDTO> lista = servicoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
        lista.add(new AnexoDTO());
        return lista;
    }

    public void setPart(FileUploadEvent event) {
        try {
            documentoAnexoService.criarDocumentoAnexo(servicoSelecionado.getIdDocumento(), getUsuarioLogado(), event);
        } catch (FileException ex) {
            Logger.getLogger(ProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerAnexo(AnexoDTO adto) {
        for (DocumentoAnexo item : servicoSelecionado.getIdDocumento().getDocumentoAnexoList()) {
            if (adto.getId() != null) {
                if (adto.getId().equals(item.getId())) {
                    servicoSelecionado.getIdDocumento().getDocumentoAnexoList().remove(item);
                    return;
                }
            } else if (adto.getConteudo().equals(item.getConteudoArquivo64())) {
                servicoSelecionado.getIdDocumento().getDocumentoAnexoList().remove(item);
                return;
            }
        }
    }

    public void doRemoveProduto() {
        servicoSelecionado.getServicoProdutoList().remove(servicoProdutoSelecionado);
    }

    public void doAdicionarProduto() {
        servicoSelecionado.getServicoProdutoList().add(new ServicoProduto());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("SERVICO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Serviço",
                    servicoService.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    ServicoDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new ServicoDTO();
    }

    public List<Servico> getServicos() {
        return servicoService.listar();
    }

}
