package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorContato;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FornecedorDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FornecedorFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
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
import org.springframework.util.StringUtils;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FornecedorService fornecedorService;

    @EJB
    private UFService ufService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @Inject
    private CadastroControle cadastroControle;

    private Fornecedor fornecedorSelecionado;

    private FornecedorContato contatoSelecionado;

    private FornecedorContato fornecedorContatoSelecionado;

    private List<FornecedorContato> listFornecedorContato;

    private LazyDataModel<Fornecedor> model;

    private LazyDataModel<FornecedorContato> modelFornecedorContato;

    private FornecedorFiltro filtro = new FornecedorFiltro();

    private UF ufSelecionado;

    private FornecedorDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, fornecedorService::quantidadeRegistrosFiltrados, fornecedorService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CLIENTE.getChave()).getDescricao());
        return "/restrito/fornecedor/cadastroFornecedor.xhtml";
    }

    @Override
    public void cleanCache() {
        this.ufSelecionado = null;
        this.listFornecedorContato = new LinkedList<>();
        this.fornecedorSelecionado = new Fornecedor();
        cleanContato();
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedorService.listar();
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
        if (fornecedorSelecionado.getEndereco().getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(fornecedorSelecionado.getEndereco().getCep());

            ufSelecionado = cepDTO.getUf();
            fornecedorSelecionado.getEndereco().setIdCidade(cepDTO.getCidade());
            fornecedorSelecionado.getEndereco().setEndereco(cepDTO.getEndereco());
            fornecedorSelecionado.getEndereco().setBairro(cepDTO.getBairro());
        }
    }

    public void cleanContato() {
        contatoSelecionado = new FornecedorContato();
        contatoSelecionado.setIdFornecedor(fornecedorSelecionado);
        contatoSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
    }

    public void addContato() {

        // Verifica se preencheu o nome do contato
        if (StringUtils.isEmpty(contatoSelecionado.getNome())) {
            return;
        }

        // Verifica se existe contato com mesmo nome, se sim edita
        if (listFornecedorContato.contains(contatoSelecionado)) {
            listFornecedorContato.set(listFornecedorContato.indexOf(contatoSelecionado), contatoSelecionado);
        } else {
            listFornecedorContato.add(contatoSelecionado);
        }

        cleanContato();
    }

    public void alterarContato() {
        contatoSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
    }

    public void removeContato() {
        listFornecedorContato.remove(contatoSelecionado);
    }

    public String doStartAdd() {
        cleanCache();
        return "/restrito/fornecedor/cadastroFornecedor.xhtml";
    }

    public String doStartAlterar() {
        listFornecedorContato = fornecedorService.listarFornecedorContato(fornecedorSelecionado);
        listFornecedorContato.stream().forEach(fornecedorContato -> fornecedorContato.setControle(StringUtil.gerarStringAleatoria(8)));
        if (fornecedorSelecionado.getEndereco() == null) {
            fornecedorSelecionado.setEndereco(new Endereco());
        }
        cleanContato();
        return "/restrito/fornecedor/cadastroFornecedor.xhtml";
    }

    public String doFinishAdd() {

        Fornecedor fornecedorCadastrado = fornecedorService.findByRazaoSocialCpfCnpj(fornecedorSelecionado.getRazaoSocial(), fornecedorSelecionado.getCpfCnpj());
        if (Objects.nonNull(fornecedorCadastrado)
                && (!fornecedorCadastrado.getId().equals(fornecedorSelecionado.getId()))) {
            createFacesErrorMessage(fornecedorSelecionado.getTipoPessoa().equals("PF") ? "Existe um fornecedor cadastrado para a razão social e CPF informado." : "Existe um fornecedor cadastrado para a razão social e CNPJ informado.");
            return "/restrito/fornecedor/cadastroFornecedor.xhtml";
        }

        // atribuindo tenant aos contatos
        fornecedorService.aadTenatContato(listFornecedorContato);
        // atribuindo contato aos fornecedores
        fornecedorSelecionado.setFornecedorContatoList(listFornecedorContato);
        try {
            // salvando fornecedor
            fornecedorService.salvar(fornecedorSelecionado);
            createFacesSuccessMessage("Fornecedor salvo com sucesso!");
            return "/restrito/fornecedor/listaFornecedor.xhtml";

        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/fornecedor/cadastroFornecedor.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/fornecedor/listaAuditoriaFornecedor.xhtml";
    }

    public String doFinishExcluir() {

        try {
            fornecedorService.remover(fornecedorSelecionado);
            createFacesSuccessMessage("Fornecedor removido com sucesso");
            return "/restrito/fornecedor/listaFornecedor.xhtml";
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/fornecedor/listaFornecedor.xhtml";
        }

    }

    public List<Object> getDadosAuditoria() {
        return fornecedorService.getDadosAuditoriaByID(fornecedorSelecionado);
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) throws MessageListException {
        List<String> erros = new ArrayList<>();
        List<EntityId> lista = obj.stream()
                .map(fornecedor -> {
                    try {
                        return fornecedorService.importDto((FornecedorDTO) fornecedor, getUsuarioLogado().getTenat());
                    } catch (CadastroException ex) {
                        erros.add(ex.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        MessageListException.throwIfNotEmpty(erros);
        return lista;
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("FORNECEDOR_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Fornecedor",
                    fornecedorService.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    FornecedorDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new FornecedorDTO();
    }

}
