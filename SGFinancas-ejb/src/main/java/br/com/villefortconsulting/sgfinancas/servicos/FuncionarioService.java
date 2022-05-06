package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.exception.SenhaIncorretaException;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioFerias;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FuncionarioMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AbonoPontoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncionarioFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncionarioServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FuncionarioRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContratacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.operator.In;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionarioService extends BasicService<Funcionario, FuncionarioRepositorio, FuncionarioFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private PerfilService perfilService;

    @EJB
    private PermissaoService permissaoService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private GrupoService grupoService;

    @EJB
    private FuncionarioServicoService funcionarioServicoService;

    @Inject
    private FuncionarioMapper funcionarioMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new FuncionarioRepositorio(em, adHocTenant);
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) throws SenhaIncorretaException, LoginDuplicadoException, UsuarioException, EmailException {
        return salvarFuncionario(funcionario, null);
    }

    public Funcionario salvarFuncionario(Funcionario funcionario, Usuario usuario) throws SenhaIncorretaException, LoginDuplicadoException, UsuarioException, EmailException {
        funcionario.getFuncionarioAtendimentoList().forEach(fa -> fa.setTenatID(adHocTenant.getTenantID()));
        funcionario.getFuncionarioServicoList().forEach(fs -> fs.setTenatID(adHocTenant.getTenantID()));
        funcionario.setFuncionarioAtendimentoList(funcionario.getFuncionarioAtendimentoList().stream()
                .filter(fa -> fa.getHoraInicial() != null && fa.getHoraFinal() != null)
                .collect(Collectors.toList()));
        funcionario.setAtivo(funcionario.getDataDemissao() == null ? "S" : "N");
        if (funcionario.getId() == null) {
            validarFuncionario(funcionario);
            funcionario.setTenatID(adHocTenant.getTenantID());
            adicionar(funcionario);
            if (usuario == null || !usuario.getEmail().equals(funcionario.getEmail())) {
                addUsuarioFuncionario(funcionario);
            } else if (usuario.getIdFuncionario() == null) {
                usuario.setIdFuncionario(funcionario);
                usuarioService.salvar(usuario);
            } else {
                throw new UsuarioException("O usuário informado já está vinculado a um funcionário.", null);
            }
            return funcionario;
        }

        return alterar(funcionario);
    }

    private void validarFuncionario(Funcionario funcionario) {
        List<String> erros = new ArrayList<>();
        if (repositorio.possuiFuncionarioComCpf(funcionario.getCpf())) {
            erros.add("Cpf já informado anteriormente");
        }

        if ((StringUtils.isNotEmpty(funcionario.getMatricula())) && repositorio
                .possuiFuncionarioComMatricula(funcionario.getMatricula())) {
            erros.add("Matrícula já informada anteriormente");
        }

        if (repositorio.possuiFuncionarioComEmail(funcionario.getEmail())) {
            erros.add("Email já informado anteriormente");
        }

        try {
            usuarioService.validarLogin(funcionario.getEmail());
        } catch (LoginDuplicadoException ex) {
            erros.add(ex.getMessage());
        }
        if (!erros.isEmpty()) {
            throw new CadastroException("Ocorreram os seguintes erros ao salvar o funcionario: " + StringUtil.listaParaTexto(erros), null);
        }
    }

    public void addUsuarioFuncionario(Funcionario funcionario) throws SenhaIncorretaException, LoginDuplicadoException, EmailException, UsuarioException {
        //busca o perfil de acordo com o tipo funcionário
        Perfil perfil = perfilService.getPerfil(EnumTipoUsuario.FUNCIONARIO.getTipo());

        //busca empresa de acordo com o tenatID do funcionario
        Empresa empresa = empresaService.getEmpresPorTenatID(funcionario.getTenatID());

        Usuario usuario = new Usuario();

        usuario.setIdFuncionario(funcionario);
        usuario.setNome(funcionario.getNome());
        usuario.setEmail(funcionario.getEmail());
        usuario.setLogin(funcionario.getEmail());
        usuario.setAdministrator(false);
        usuario.setContaBloqueada("N");
        usuario.setContaExpirada("N");
        usuario.setIdPerfil(perfil);

        //adiciona as permissões do usuário de acordo com o perfil
        usuario.setUsuarioPermissaoList(addPermissaoUsuario(usuario, funcionario.getTenatID()));

        //adiciona a lista de usuário grupo empresa
        usuario.setUsuarioGrupoEmpresaList(addUsuarioGrupoEmpresa(usuario, empresa));

        //Salva o usuário criando uma senha aleatória para o mesmo.
        String senha = StringUtil.gerarStringAleatoria(10);
        usuarioService.salvarUsuario(usuario, senha, empresa);

        //Envia e-mail para o funcionário com a senha do mesmo.
        usuarioService.enviarSenhaPorEmailFuncionario(usuario, senha);

    }

    public List<UsuarioPermissao> addPermissaoUsuario(Usuario usuario, String tenatID) {

        List<UsuarioPermissao> listUsuarioPermissao = new LinkedList<>();
        List<Permissao> listPermissao = permissaoService.getPermissoes(usuario.getIdPerfil());

        for (int i = 0; i < listPermissao.size(); i++) {
            UsuarioPermissao usuarioPerm = new UsuarioPermissao();
            usuarioPerm.setIdUsuario(usuario);
            usuarioPerm.setIdPermissao(listPermissao.get(i));
            usuarioPerm.setTenatID(tenatID);
            listUsuarioPermissao.add(usuarioPerm);
        }
        return listUsuarioPermissao;
    }

    public List<UsuarioGrupoEmpresa> addUsuarioGrupoEmpresa(Usuario usuario, Empresa empresa) {

        List<UsuarioGrupoEmpresa> listUsuarioGrupoEmpresa = new LinkedList<>();

        //Busca o grupo no banco
        Grupo grupo = grupoService.getGrupoPorTipo(EnumTipoUsuario.FUNCIONARIO.getTipo());

        GrupoEmpresa grupoEmpresa = grupoService.getGrupoEmpresa(grupo, empresa);

        UsuarioGrupoEmpresa usrGrpEmp = new UsuarioGrupoEmpresa();
        usrGrpEmp.setIdGrupoEmpresa(grupoEmpresa);
        usrGrpEmp.setIdUsuario(usuario);

        listUsuarioGrupoEmpresa.add(usrGrpEmp);

        return listUsuarioGrupoEmpresa;
    }

    public Funcionario buscarMatricula(String matricula) {
        if (matricula != null) {
            return buscarFuncionarioPorMatricula(matricula);
        }
        return null;
    }

    public void alterarSenhaFuncionario(Funcionario funcionario, String novaSenha, String repetirNovaSenha) throws SenhaIncorretaException {
        if (!novaSenha.equals(repetirNovaSenha)) {
            throw new SenhaIncorretaException("Senhas não conferem.");
        } else {
            salvarValidarSenha(funcionario, novaSenha);
        }
    }

    public void criaSenhaFuncionario(Funcionario funcionario, String senha, String repetirSenha) throws SenhaIncorretaException {
        if (!senha.equals(repetirSenha)) {
            throw new SenhaIncorretaException("Senhas não conferem.");
        } else {
            salvarValidarSenha(funcionario, senha);
        }
    }

    public void salvarValidarSenha(Funcionario funcionario, String password) throws SenhaIncorretaException {
        validarSenha(password);
        funcionario.setSenha(StringUtil.criptografarMD5(password));
        alterar(funcionario);
    }

    public boolean validarSenha(String senha) throws SenhaIncorretaException {
        if (senha == null || senha.length() < 4) {
            throw new SenhaIncorretaException("Você deve especificar uma senha contendo letras e números com tamanho mínimo de 4 caracteres.");
        }
        return true;
    }

    @Override
    public void remover(Funcionario funcionario) {
        if (funcionario.getIdPlanoConta() != null) {
            planoContaPadraoService.remover(funcionario.getIdPlanoConta());
        }

        funcionario.setAtivo("N");
        usuarioService.bloquearUsuarioPorFuncionario(funcionario);
        super.salvar(funcionario);

    }

    public List<Funcionario> listarFuncionarioPorPerfil(Usuario usuario) {
        if (usuario.getIdPerfil().getEhSuporte() || usuario.getIdPerfil().getEhUsuarioMaster()) {
            return repositorio.listar();
        } else {
            return repositorio.listar(usuario);
        }
    }

    public List<Funcionario> listarFuncionarioAtivosVendedores() {
        return repositorio.listarFuncionariosAtivosVendedores();
    }

    public List<Funcionario> listarSemUsuario() {
        return repositorio.listarSemUsuario();
    }

    public List<Funcionario> listarFuncionariosAtivos(Date competencia) {
        return repositorio.listarFuncionariosAtivos(competencia);
    }

    public List<Funcionario> listarFuncionariosPorServico(Servico servico) {
        if (servico == null) {
            return repositorio.listar();
        }
        FuncionarioServicoFiltro filtro = new FuncionarioServicoFiltro();
        filtro.getExample().setIdServico(servico);
        return funcionarioServicoService.getListaFiltrada(filtro).stream()
                .map(FuncionarioServico::getIdFuncionario)
                .collect(Collectors.toList());
    }

    @Override
    public Criteria getModel(FuncionarioFiltro filter) {
        Criteria criteria = super.getModel(filter);

        if (filter.getAtivo() != null && !filter.getAtivo().equals("A")) {
            addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        }
        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "cpf", filter.getCpf(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "matricula", filter.getMatricula(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "cargo", filter.getCargo(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "tenatID", filter.getTenantID());
        if (!filter.getTipoContratacao().isEmpty()) {
            addRestrictionTo(criteria, "tipoContratacao", In.fromList(filter.getTipoContratacao()));
        }

        return criteria;
    }

    public List<Funcionario> getListaFiltradaSemTenant(FuncionarioFiltro filter) {
        return getModel(filter).list();
    }

    public FuncionarioFerias salvarFuncionarioFerias(FuncionarioFerias funcionarioFerias) {
        if (funcionarioFerias.getId() == null) {
            return repositorio.adicionarFuncionarioFerias(funcionarioFerias);
        } else {
            return repositorio.alterarFuncionarioFerias(funcionarioFerias);
        }
    }

    public FuncionarioFerias adicionarFuncionarioFerias(FuncionarioFerias funcionarioFerias) {
        return repositorio.adicionarFuncionarioFerias(funcionarioFerias);
    }

    public FuncionarioFerias alterarFuncionarioFerias(FuncionarioFerias funcionarioFerias) {
        return repositorio.alterarFuncionarioFerias(funcionarioFerias);
    }

    public void removerFuncionarioFerias(FuncionarioFerias funcionarioFerias) {
        repositorio.removerFuncionarioFerias(funcionarioFerias);
    }

    public FuncionarioFerias buscarFuncionarioFerias(int id) {
        return repositorio.buscarFuncionarioFerias(id);
    }

    public List<FuncionarioFerias> listarFuncionarioFerias() {
        return repositorio.listarFuncionarioFerias();
    }

    public List<Object> getDadosAuditoriaFuncionarioFeriasByID(FuncionarioFerias funcionarioFerias) {
        return repositorio.getDadosAuditoria(FuncionarioFerias.class, funcionarioFerias.getId());
    }

    public int quantidadeRegistrosFuncionarioFeriasFiltrados(AbonoPontoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getFuncionarioFeriasModel(filtro));
    }

    public List<FuncionarioFerias> getListaFuncionarioFeriasFiltrada(AbonoPontoFiltro filtro) {
        return repositorio.getListaFiltrada(getFuncionarioFeriasModel(filtro), filtro);
    }

    public Criteria getFuncionarioFeriasModel(AbonoPontoFiltro filtro) {

        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(FuncionarioFerias.class);

        if (filtro.getFuncionario() != null) {
            criteria.add(Restrictions.eq("idFuncionario", filtro.getFuncionario()));
        }

        if (filtro.getCompetencia() != null) {
            Criterion di1 = Restrictions.ge("dataInicio", DataUtil.getPrimeiroDiaDoMes(filtro.getCompetencia()));
            Criterion di2 = Restrictions.le("dataInicio", DataUtil.getUltimoDiaDoMes(filtro.getCompetencia()));

            Criterion df1 = Restrictions.ge("dataFim", DataUtil.getPrimeiroDiaDoMes(filtro.getCompetencia()));
            Criterion df2 = Restrictions.le("dataFim", DataUtil.getUltimoDiaDoMes(filtro.getCompetencia()));

            Criterion cdi = Restrictions.and(di1, di2);
            Criterion cdf = Restrictions.and(df1, df2);

            criteria.add(Restrictions.or(cdi, cdf));
        }

        return criteria;
    }

    public byte[] getFoto(Part part, Funcionario funcionario) throws FileException {
        if (part != null) {
            return FileUtil.convertPartToBytes(part);
        }
        if (funcionario.getFoto() != null) {
            return funcionario.getFoto();
        }
        return new byte[]{};
    }

    public Funcionario buscarFuncionarioPorMatricula(String matricula) {
        return repositorio.buscarFuncionarioPorMatricula(matricula);
    }

    public boolean verificaFuncionarioEstaFerias(Funcionario funcionario, Date data) {
        return repositorio.verificaFuncionatioEstaFerias(funcionario, data);
    }

    public List<FuncionarioServico> listarFuncionarioServicoByIdFuncionario(Funcionario funcionario) {
        return repositorio.listarFuncionarioServicoByIdFuncionario(funcionario);
    }

    public List<FuncionarioAtendimento> listarFuncionarioAtendimentoByIdFuncionario(Funcionario funcionario) {
        return repositorio.listarFuncionarioAtendimentoByIdFuncionario(funcionario);
    }

    public Funcionario importDto(FuncionarioDTO funcionarioDTO) {
        try {
            Funcionario funcionario = funcionarioMapper.toEntity(funcionarioDTO);
            funcionario.setTipoContratacao(EnumTipoContratacao.getTipo(funcionarioDTO.getTipoContratacao()));
            if (!StringUtils.isEmpty(funcionarioDTO.getHoraEspecial()) || !StringUtils.isEmpty(funcionarioDTO.getHoraEspecialFinal())) {
                funcionario.setTemHorarioEspecial("S");
            } else {
                funcionario.setTemHorarioEspecial("N");
            }
            funcionario.setEhOrcamentista("Sim".equals(funcionarioDTO.getEhOrcamentista()) ? "S" : "N");
            if (funcionarioDTO.getEhOrcamentista() == null) {
                funcionario.setEhOrcamentista("S");
            }

            String cpf = StringUtil.removerMascara(funcionarioDTO.getCpf());
            if (cpf.length() < 11) {
                cpf = StringUtil.adicionarCaracterEsquerda(cpf, "0", 11);
                funcionario.setCpf(CpfCnpjUtil.mascararCpf(cpf));
            }
            if (!CpfCnpjUtil.validarCPF(cpf)) {
                throw new CadastroException("CPF inválido!", null);
            }
            return salvarFuncionario(funcionario);
        } catch (LoginDuplicadoException | SenhaIncorretaException | EmailException | UsuarioException e) {
            throw new CadastroException(e.getMessage(), e);
        }
    }

}
