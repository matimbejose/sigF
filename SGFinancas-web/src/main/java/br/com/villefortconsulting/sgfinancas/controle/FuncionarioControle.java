package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncionarioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioAtendimentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FuncionarioService funcionarioService;

    @EJB
    private FuncionarioAtendimentoService funcionarioAtendimentoService;

    @EJB
    private FuncionarioServicoService funcionarioServicoService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private UFService ufService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ServicoService servicoService;

    @EJB
    private UsuarioService usuarioService;

    @Inject
    private CadastroControle cadastroControle;

    private Funcionario funcionarioSelecionado;

    private LazyDataModel<Funcionario> model;

    private UF ufSelecionado;

    private transient Part partFoto;

    private transient Part part;

    private String matricula;

    private String mostraImportacao = "N";

    private String senha;

    private String repetirSenha;

    private String senhaCadastro;

    private String novaSenha;

    private String repetirNovaSenha;

    private FuncionarioFiltro filtro = new FuncionarioFiltro();

    private Servico servicoSelecionado;

    private FuncionarioServico funcionarioServicoSelecionado;

    private FuncionarioDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, funcionarioService::quantidadeRegistrosFiltrados, funcionarioService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_FUNCIONARIO.getChave()).getDescricao());
        return "cadastroFuncionario.xhtml";
    }

    @Override
    public void cleanCache() {
        funcionarioSelecionado = new Funcionario();
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarioService.listar();
    }

    public List<Funcionario> listarFuncionarioPorPerfil() {
        return funcionarioService.listarFuncionarioPorPerfil(getUsuarioLogado());
    }

    public List<Funcionario> getFuncionariosSemUsuario() {
        return funcionarioService.listarSemUsuario();
    }

    public void esconderImportacao() {
        mostraImportacao = "N";
    }

    public void buscarFuncionario() {
        funcionarioSelecionado = funcionarioService.buscarMatricula(matricula);
    }

    public void verificarMatricula() {
        funcionarioSelecionado = funcionarioService.buscarFuncionarioPorMatricula(matricula);
    }

    private void initFuncionarioAtendimentoList() {
        if (funcionarioSelecionado.getFuncionarioAtendimentoList() == null || funcionarioSelecionado
                .getFuncionarioAtendimentoList().isEmpty()) {
            funcionarioSelecionado.setFuncionarioAtendimentoList(new ArrayList<>());
        }
        getNumeroDias().forEach(i -> {
            boolean hasThisDay = funcionarioSelecionado.getFuncionarioAtendimentoList().stream().anyMatch(fa -> fa.getDiaSemana().equals(i));
            if (!hasThisDay) {
                FuncionarioAtendimento fa = new FuncionarioAtendimento();
                fa.setDiaSemana(i);
                fa.setIdFuncionario(funcionarioSelecionado);
                funcionarioSelecionado.getFuncionarioAtendimentoList().add(fa);
            }
        });
        Collections.sort(funcionarioSelecionado.getFuncionarioAtendimentoList(), (a, b) -> a.getDiaSemana() - b.getDiaSemana());
    }

    public String doStartAdd() {
        cleanCache();
        initFuncionarioAtendimentoList();
        funcionarioSelecionado.setTemHorarioEspecial("N");
        return "/restrito/funcionario/cadastroFuncionario.xhtml";
    }

    public String doStartAlterar() {
        funcionarioSelecionado.setFuncionarioAtendimentoList(funcionarioAtendimentoService.getListaByFuncionario(funcionarioSelecionado));
        funcionarioSelecionado.setFuncionarioServicoList(funcionarioServicoService.getListaByFuncionario(funcionarioSelecionado));
        initFuncionarioAtendimentoList();
        return "cadastroFuncionario.xhtml";
    }

    public String doStartCriaSenha() {
        return "criarSenha.xhtml";
    }

    public String doStartAlterarSenha() {
        return "alterarSenha.xhtml";
    }

    public String doFinishAdd() {
        try {
            funcionarioSelecionado.setFoto(funcionarioService.getFoto(partFoto, funcionarioSelecionado));
            funcionarioService.salvarFuncionario(funcionarioSelecionado, getUsuarioLogado());
            createFacesSuccessMessage("Funcionário salvo com sucesso! ");
            return "listaFuncionario.xhtml";

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroFuncionario.xhtml";
        }
    }

    public String doFinishExcluir() {
        try {
            funcionarioService.remover(funcionarioSelecionado);

            createFacesSuccessMessage("Funcionário excluído com sucesso!");
            return "listaFuncionario.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroFuncionario.xhtml";
        }
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

    public String doShowAuditoria() {
        return "listaAuditoriaFuncionario.xhtml";
    }

    public String doAlterarSenha() {
        try {
            funcionarioService.alterarSenhaFuncionario(funcionarioSelecionado, novaSenha, repetirNovaSenha);
            createFacesSuccessMessage("Senha alterada com sucesso.");
            return "listaFuncionario.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "alterarSenha.xhtml";
        }
    }

    public String doCriaSenha() {
        try {
            funcionarioService.criaSenhaFuncionario(funcionarioSelecionado, senha, repetirSenha);
            createFacesSuccessMessage("Senha criada com sucesso.");
            return "listaFuncionario.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "alterarSenha.xhtml";
        }
    }

    public StreamedContent getFoto() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (funcionarioSelecionado != null && funcionarioSelecionado.getFoto() != null) {
            return new DefaultStreamedContent(
                    new ByteArrayInputStream(funcionarioSelecionado.getFoto()), "image/jpeg");
        }
        return null;
    }

    public void buscarEnderecoPorCep() {
        if (funcionarioSelecionado.getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(funcionarioSelecionado.getCep());

            ufSelecionado = cepDTO.getUf();
            funcionarioSelecionado.setIdCidade(cepDTO.getCidade());
            funcionarioSelecionado.setEndereco(cepDTO.getEndereco());
            funcionarioSelecionado.setBairro(cepDTO.getBairro());
        }
    }

    public List<Object> getDadosAuditoria() {
        return funcionarioService.getDadosAuditoriaByID(funcionarioSelecionado);
    }

    public List<Integer> getNumeroDias() {
        return Arrays.asList(0, 1, 2, 3, 4, 5, 6);
    }

    public List<String> getDiasSemana() {
        return DataUtil.obterNomesDiaSemana();
    }

    public FuncionarioAtendimento getFuncionarioAtendimentoDoDia(Integer dia) {
        return funcionarioSelecionado.getFuncionarioAtendimentoList().stream()
                .filter(fa -> fa.getDiaSemana().equals(dia))
                .findAny().orElse(new FuncionarioAtendimento());
    }

    public List<Servico> getServicosDisponiveis() {
        return servicoService.listar().stream()
                .filter(item -> funcionarioSelecionado.getFuncionarioServicoList().stream().noneMatch(fs -> fs.getIdServico().equals(item)))
                .collect(Collectors.toList());
    }

    public void doAdicionarTodosServicos() {
        getServicosDisponiveis().forEach(item -> {
            servicoSelecionado = item;
            doAdicionarServico();
        });
    }

    public void doAdicionarServico() {
        FuncionarioServico fs = new FuncionarioServico();
        fs.setIdFuncionario(funcionarioSelecionado);
        fs.setIdServico(servicoSelecionado);
        funcionarioSelecionado.getFuncionarioServicoList().add(fs);
    }

    public void doRemoverTodosServicos() {
        funcionarioSelecionado.getFuncionarioServicoList().clear();
    }

    public void doRemoverServico() {
        funcionarioSelecionado.getFuncionarioServicoList().remove(funcionarioServicoSelecionado);
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(prod -> funcionarioService.importDto((FuncionarioDTO) prod))
                .collect(Collectors.toList());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("FUNCIONARIO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Funcionário",
                    funcionarioService.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    FuncionarioDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new FuncionarioDTO();
    }

    public void reativarFuncionario() throws LoginDuplicadoException {
        funcionarioSelecionado.setAtivo("S");
        try {
            usuarioService.desbloquearUsuarioPorLogin(funcionarioSelecionado);
            funcionarioService.salvarFuncionario(funcionarioSelecionado, getUsuarioLogado());
            createFacesSuccessMessage("Funcionario/Usuario reativados com sucesso!");
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
        }

    }

}
