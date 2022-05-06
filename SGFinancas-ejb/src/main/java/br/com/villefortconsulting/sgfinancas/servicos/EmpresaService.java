package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.exception.SenhaIncorretaException;
import br.com.villefortconsulting.sgfinancas.entidades.Classificacao;
import br.com.villefortconsulting.sgfinancas.entidades.ClassificacaoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaCnae;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaContatoCliente;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaUsuarioAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.TipoSocialEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AcessoPorEmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AtividadeEmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClassificacaoCnaeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CredenciamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.HubDoDesenvolvedorWrapperEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaContatoClienteFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.Certificado;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ClassificacaoRepositorio;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CnaeRepositorio;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.EmpresaRepositorio;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TipoSocialEmpresaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumPorteEmpresa;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEmpresa;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoGrupo;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoSistema;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.operator.In;
import br.com.villefortconsulting.util.operator.MinMax;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.http.ResponseEntity;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpresaService extends BasicService<Empresa, EmpresaRepositorio, EmpresaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private GrupoService grupoService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @EJB
    private ContabilidadeService contabilidadeService;

    @EJB
    private CnaeService cnaeService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private CategoriaEmpresaService categoriaEmpresaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ParametroGeralService parametroGeralService;

    @EJB
    private EmpresaContratoClienteService empresaContratoClienteService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @EJB
    private EmailService emailService;

    @EJB
    private PerfilService perfilService;

    @EJB
    private SmsService smsService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    private transient ClassificacaoRepositorio classificacaoRepositorio;

    private transient CnaeRepositorio cnaeRepositorio;

    private transient TipoSocialEmpresaRepositorio tipoSocialEmpresaRepositorio;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new EmpresaRepositorio(em, adHocTenant);
        classificacaoRepositorio = new ClassificacaoRepositorio(em);
        cnaeRepositorio = new CnaeRepositorio(em);
        tipoSocialEmpresaRepositorio = new TipoSocialEmpresaRepositorio(em);
    }

    public Empresa getEmpresa() {
        return repositorio.getEmpresaPorTenat(adHocTenant.getTenantID());
    }

    public List<Empresa> listaEmpresaCadastradaPorPeriodo(Date dataInicio, Date dataFim) {
        return repositorio.listaEmpresaCadastradaPorPeriodo(dataInicio, dataFim);
    }

    public boolean existeCerticadoInformado() {
        Empresa empresa = getEmpresa();
        return !(empresa.isPJ() && empresa.getIdDocumento() == null);
    }

    public boolean existeCnpjCadastrado(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        return repositorio.existeCnpjCadastrado(cnpj);
    }

    public Empresa buscarPorCnpj(String cnpj) {
        return repositorio.buscarPorCnpj(cnpj);
    }

    public boolean precisaAtualizarDados() {
        Empresa empresa = getEmpresa();

        return empresa != null && empresa.isPJ() && (empresa.getEndereco().getIdCidade() == null
                || StringUtils.isEmpty(empresa.getIndicadorInscricaoEstadual())
                || StringUtils.isEmpty(empresa.getInscricaoEstadual())
                || StringUtils.isEmpty(empresa.getRegimeTributario())
                || StringUtils.isEmpty(empresa.getEndereco().getEndereco())
                || StringUtils.isEmpty(empresa.getEndereco().getBairro())
                || StringUtils.isEmpty(empresa.getEndereco().getCep()));
    }

    public boolean empresaPertenceAoSimplesNacional() {
        Empresa empresa = repositorio.getEmpresPorTenatID();
        return EnumRegimeTributario.SIMPLES_NACIONAL_EMPRE_PEQ_PORT.getForma().equals(empresa.getRegimeTributario()) || EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma().equals(empresa.getRegimeTributario());
    }

    public boolean empresaPertenceAoSimplesNacional(Empresa empresa) {
        return EnumRegimeTributario.SIMPLES_NACIONAL_EMPRE_PEQ_PORT.getForma().equals(empresa.getRegimeTributario()) || EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma().equals(empresa.getRegimeTributario());
    }

    public List<Empresa> getEmpresas() {
        return repositorio.getEmpresas();
    }

    public Empresa getEmpresaByID(Integer id) {
        return repositorio.getEmpresabyID(id);
    }

    public List<EmpresaUsuarioAcesso> listarEmpresaUsuarioAcesso(Empresa empresa) {
        return repositorio.listarEmpresaUsuarioAcesso(empresa);
    }

    public List<Classificacao> listarClassificacoes(Empresa empresa) {
        return classificacaoRepositorio.listarClassificacao(empresa);
    }

    public List<ClassificacaoEmpresa> listarClassificacaoEmpresa(Empresa empresa) {
        return classificacaoRepositorio.listarClassificacaoEmpresa(empresa);
    }

    public Empresa getEmpresPorTenatID() {
        return repositorio.getEmpresPorTenatID();
    }

    public Empresa getEmpresPorTenatID(String tenatID) {
        return repositorio.getEmpresPorTenatID(tenatID);
    }

    @Override
    public Criteria getModel(EmpresaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        if (filter.getAgendaHabilitada() != null) {
            criteria.add(Subqueries.propertyIn("tenatID", DetachedCriteria
                    .forClass(ParametroSistema.class)
                    .setProjection(Projections.property("tenatID"))
                    .add(Restrictions.eq("habilitaAgenda", "S"))
            ));
        }

        if (filter.getCodIBGE() != null) {
            criteria.createAlias("endereco.idCidade", "idCidade");
            criteria.add(Restrictions.eq("idCidade.codigoIBGE", filter.getCodIBGE() + ""));
        }

        return criteria;
    }

    public List<Empresa> getListaFiltradaSemTenant(EmpresaFiltro filter) {
        return getModel(filter).list();
    }

    private static ArrayList<Criterion> getCriterionsEmpresaCredenciada(EmpresaFiltro filtro) {
        ArrayList<Criterion> criterions = new ArrayList<>();

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            criterions.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(filtro.getAtivo())) {
            criterions.add(Restrictions.eq("ativo", filtro.getAtivo()));
        }

        return criterions;
    }

    public int quantidadeRegistrosFiltradosEmpresaCredenciada(EmpresaFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getCriterionsEmpresaCredenciada(filtro), EmpresaRepositorio.NAO_USA_TENANT);
    }

    public List<Empresa> getListaFiltradaEmpresaCredenciada(EmpresaFiltro filtro) {
        return repositorio.getListaFiltrada(getCriterionsEmpresaCredenciada(filtro), filtro, EmpresaRepositorio.NAO_USA_TENANT);
    }

    public int quantidadeRegistrosFiltradosEmpresaContatoCliente(EmpresaContatoClienteFiltro filtro) {
        ArrayList<Criterion> criterions = new ArrayList<>();

        if (filtro.getEmpresa() != null) {
            criterions.add(Restrictions.eq("idEmpresa", filtro.getEmpresa()));
        }

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            criterions.add(Restrictions.ilike("nomeContato", filtro.getDescricao(), MatchMode.ANYWHERE));
        }

        return empresaContratoClienteService.getQuantidadeRegistrosFiltradosEmpresaContatoCliente(criterions, EmpresaRepositorio.NAO_USA_TENANT);
    }

    public List<EmpresaContatoCliente> getListaFiltradaEmpresaContatoCliente(EmpresaContatoClienteFiltro filtro) {
        ArrayList<Criterion> criterions = new ArrayList<>();

        if (filtro.getEmpresa() != null) {
            criterions.add(Restrictions.eq("idEmpresa", filtro.getEmpresa()));
        }

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            criterions.add(Restrictions.ilike("nomeContato", filtro.getDescricao(), MatchMode.ANYWHERE));
        }

        return empresaContratoClienteService.getListaFiltradaEmpresaContatoCliente(criterions, filtro, EmpresaRepositorio.NAO_USA_TENANT);
    }

    public List<Object> getDadosAuditoriaByIDEmpresaContatoCliente(EmpresaContatoCliente empresa) {
        return repositorio.getDadosAuditoriaByID(EmpresaContatoCliente.class, empresa.getId());
    }

    public EmpresaCnae preencherEmpresaCnae(Empresa empresa, Cnae cnae) {
        if (cnae != null) {
            EmpresaCnae empresaCnae = new EmpresaCnae();
            empresaCnae.setIdCnae(cnae);
            empresaCnae.setIdEmpresa(empresa);
            return empresaCnae;
        }
        return null;
    }

    public Cnae preencherCnaePrincipal(List<ClassificacaoCnaeDTO> cc) {
        try {
            return cc.get(0).getCnae();
        } catch (Exception ex) {
            throw new IllegalStateException("Favor informar o CNAE principal", ex);
        }
    }

    public List<EmpresaCnae> preencherCnaeSecundario(Empresa empresa, List<ClassificacaoCnaeDTO> cc, List<EmpresaCnae> listEmpresaCnae) {
        return cc.stream()
                .filter(dto -> dto.getCnae() != null)
                .map(dto -> new EmpresaCnae(dto.getCnae(), empresa))
                .map(e -> {
                    if (listEmpresaCnae.contains(e)) {
                        return listEmpresaCnae.get(listEmpresaCnae.indexOf(e));
                    }
                    return e;
                })
                .collect(Collectors.toList());
    }

    public void salvarEmpresa(Empresa empresa, Part part, Usuario usuarioLogado) {
        // anexar certificado
        empresa.setIdDocumento(anexarCertificado(empresa, part, usuarioLogado));

        if (empresa.getId() == null) {
            adicionarEmpresa(empresa, usuarioLogado);
            usuarioService.concederAcessoUsuariosSuporte(empresa);
        } else {
            alterar(empresa);
        }

        contabilidadeService.atualizarAcessoUsuariosContabilidade(empresa);
    }

    private Documento anexarCertificado(Empresa empresa, Part part, Usuario usuarioLogado) {
        if (!"A1".equals(empresa.getTipoCertificadoDigital())) {
            return null;
        }
        if (part == null) {
            return empresa.getIdDocumento();
        }
        try {
            return documentoService.criarDocumento(usuarioLogado, "CERT_" + empresa.getCnpj(), part);
        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Certificado buscarCertificadoEmpresa(Empresa empresa) throws NfeException {
        if (empresa.getIdDocumento() == null) {
            throw new br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException("Informe o certificado antes de prosseguir!", null);
        }

        DocumentoAnexo anexo = documentoAnexoService.buscarUltimoAnexoDocumento(empresa.getIdDocumento());

        if (anexo == null) {
            throw new br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException("Informe o certificado antes de prosseguir!", null);
        }

        // TODO refatorar isso, a única necessidade desse código era para que existisse um arquivo quando o conteúdo dele era salvo no banco
        File file;
        try {
            byte[] content = anexo.readFromFile();
            if (content == null) {
                throw new br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException("Erro ao abrir o certificado!", null);
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(content);

            file = File.createTempFile("tempfile", ".pfx");
            try (OutputStream out = new FileOutputStream(file)) {
                int read;
                byte[] bytes = new byte[1024];

                while ((read = bis.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            }
        } catch (Exception ex) {
            throw new br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException("Erro ao usar o certificado!", ex);
        }

        return CertificadoUtil.certificadoPfx(file.getAbsolutePath(), empresa.getSenhaCertificado());
    }

    public Empresa adicionarEmpresa(Empresa empresa, Usuario usuarioLogado) {
        // Adicionar grupo padroes a empresa
        grupoService.getGruposPadroes()
                .forEach(empresa::addGrupo);

        empresa = adicionar(empresa);

        loginAcessoService.adicionarLoginAcesso(empresa, usuarioLogado);

        planoContaService.salvarPlanoContaModeloPadrao(empresa);

        unidadeMedidaService.populaUnidadeMedida(empresa.getTenatID());

        parametroSistemaService.populaParametroSistema(empresa.getTenatID());

        return empresa;
    }

    public String getTipo() {
        return repositorio.getTipo();
    }

    public String getDescricaoTipoEmpresa() {
        return EnumTipoEmpresa.PRIVADO.getTipo().equals(getTipo()) ? "empresa" : "prefeitura";
    }

    public String getDescricaoTipoFuncionario() {
        return EnumTipoEmpresa.PRIVADO.getTipo().equals(getTipo()) ? "funcionário" : "servidor";
    }

    public String getDescricaoTipoFuncionarioPlural() {
        return EnumTipoEmpresa.PRIVADO.getTipo().equals(getTipo()) ? "funcionários" : "servidores";
    }

    public ClassificacaoEmpresa getClassificaoEmpresa(int id) {
        return classificacaoRepositorio.getClassificaoEmpresa(id);
    }

    public List<ClassificacaoEmpresa> getClassificaoEmpresas() {
        return classificacaoRepositorio.getClassificaoEmpresas();
    }

    public List<Classificacao> getClassificoes() {
        return classificacaoRepositorio.getClassificoes();
    }

    public ClassificacaoEmpresa setClassificaoEmpresa(ClassificacaoEmpresa obj) {
        return classificacaoRepositorio.setClassificaoEmpresa(obj);
    }

    public void removeClassificacaoEmpresa(ClassificacaoEmpresa obj) {
        classificacaoRepositorio.removeClassificacaoEmpresa(obj);
    }

    public ClassificacaoEmpresa addClassificaoEmpresa(ClassificacaoEmpresa obj) {
        return classificacaoRepositorio.addClassificaoEmpresa(obj);
    }

    public Empresa getEmpresaPorTenat(String tenat) {
        return repositorio.getEmpresaPorTenat(tenat);
    }

    public List<EmpresaCnae> listarEmpresaCnae(Empresa empresa) {
        return cnaeRepositorio.listarEmpresaCnae(empresa);
    }

    public byte[] getLogo(Part part, Empresa empresa) throws FileException {
        if (part != null) {
            return FileUtil.convertPartToBytes(part);
        }
        return empresa.getLogo();
    }

    public EmpresaContatoCliente salvarEmpresaContatoCliente(EmpresaContatoCliente empresaContatoClienteSelecionado) {
        if (empresaContatoClienteSelecionado.getId() == null) {
            return repositorio.addEmpresaContatoCliente(empresaContatoClienteSelecionado);
        } else {
            return repositorio.setEmpresaContatoCliente(empresaContatoClienteSelecionado);
        }
    }

    public EmpresaContatoCliente addEmpresaContatoCliente(EmpresaContatoCliente obj) {
        return repositorio.addEmpresaContatoCliente(obj);
    }

    public EmpresaContatoCliente setEmpresaContatoCliente(EmpresaContatoCliente obj) {
        return repositorio.setEmpresaContatoCliente(obj);
    }

    public void removeEmpresaContatoCliente(EmpresaContatoCliente obj) {
        repositorio.removeEmpresaContatoCliente(obj);
    }

    public Long getQtdEmpresasExpiradas() {
        return repositorio.getQtdEmpresasExpiradas();
    }

    public Long getQtdClientesContatoHoje() {
        return repositorio.getQtdClientesContatoHoje();
    }

    public EmpresaDTO buscarDadosEmpresaPorCNPJ(String cnpj) {
        try {
            ResponseEntity<String> status = MicroServiceUtil.doGet(MicroServiceUtil.MicroServicos.BUSCA_CPF_CNPJ, "cnpj/" + cnpj.replaceAll("\\D", ""));

            ObjectMapper mapper = new ObjectMapper();
            HubDoDesenvolvedorWrapperEmpresa resp = mapper.readValue(status.getBody(), HubDoDesenvolvedorWrapperEmpresa.class);
            if (resp.getResult() == null) {
                return new EmpresaDTO("NOK");
            }
            resp.getResult().setStatus(resp.getRetorno());
            EmpresaDTO dto = resp.getResult();
            dto.setCodCidadeIBGE(Integer.parseInt(cidadeService.getEnderecoPorCep(dto.getCep()).getCidade().getCodigoIBGE()));
            return dto;
        } catch (Exception ex) {
            Logger.getLogger(EmpresaService.class.getName()).log(Level.SEVERE, null, ex);
            return new EmpresaDTO("NOK");
        }
    }

    public Empresa buscarDadosEmpresaPorCNPJ(String cnpj, Empresa empresa) {
        return buscarDadosEmpresaPorCNPJ(cnpj, empresa, new ArrayList<>());
    }

    public Empresa buscarDadosEmpresaPorCNPJ(String cnpj, Empresa empresa, List<EmpresaCnae> listEmpresaCnae) {
        if (!CpfCnpjUtil.validarCNPJ(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        EmpresaDTO dadosEmpresa = buscarDadosEmpresaPorCNPJ(cnpj);

        if (!dadosEmpresa.getStatus().equals("OK")) {
            throw new NotFoundException("Não foi possível buscar o CNPJ");
        }
        List<ClassificacaoCnaeDTO> listCnaePrincipal = new ArrayList<>();
        List<ClassificacaoCnaeDTO> listCnaeSecundario = new ArrayList<>();

        AtividadeEmpresaDTO atividadePrincipal = dadosEmpresa.getAtividadePrincipal();
        List<AtividadeEmpresaDTO> atividadesSecundarias = dadosEmpresa.getAtividadesSecundarias();

        Cnae cnaePrincipal = cnaeService.buscarCnaePorCodigo(atividadePrincipal.getCode(), atividadePrincipal.getText());

        empresa.getEndereco().setComplemento(dadosEmpresa.getComplemento());
        empresa.getEndereco().setNumero(dadosEmpresa.getNumero());
        empresa.getEndereco().setIdCidade(cidadeService.buscar(dadosEmpresa.getMunicipio()));
        empresa.getEndereco().setBairro(dadosEmpresa.getBairro());
        empresa.getEndereco().setEndereco(dadosEmpresa.getLogradouro());
        empresa.setDescricao(dadosEmpresa.getNome());
        empresa.setEmail(dadosEmpresa.getEmail());
        empresa.setAtivo("S");
        empresa.getEndereco().setCep(dadosEmpresa.getCep());
        empresa.setNomeFantasia(dadosEmpresa.getFantasia());
        empresa.setFone(dadosEmpresa.getTelefone());
        empresa.setDataConstituicao(DataUtil.converterStringParaDate(dadosEmpresa.getAbertura()));

        switch (dadosEmpresa.getPorte()) {
            case "MICRO EMPRESA":
            case "ME":
                if (dadosEmpresa.getNaturezaJuridica().startsWith("213-5")) {
                    empresa.setPorte(EnumPorteEmpresa.MEI.getSigla());
                } else {
                    empresa.setPorte(EnumPorteEmpresa.ME.getSigla());
                }
                empresa.setRegimeTributario(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma());
                break;
            case "DEMAIS":
                if (dadosEmpresa.getNaturezaJuridica().startsWith("206-2")) {
                    empresa.setPorte(EnumPorteEmpresa.SOCIEDADE_EMPRESARIA_LIMITADA.getSigla());
                } else if (dadosEmpresa.getNaturezaJuridica().startsWith("224-0")) {
                    empresa.setPorte(EnumPorteEmpresa.SOCIEDADE_SIMPLES_LIMITADA.getSigla());
                } else if (dadosEmpresa.getNaturezaJuridica().startsWith("230-5")) {
                    empresa.setPorte(EnumPorteEmpresa.EIRELI.getSigla());
                } else if (dadosEmpresa.getNaturezaJuridica().startsWith("399-9")) {
                    empresa.setPorte(EnumPorteEmpresa.ASSOCIACAO_PRIVADA.getSigla());
                } else if (dadosEmpresa.getNaturezaJuridica().startsWith("204-6")
                        || dadosEmpresa.getNaturezaJuridica().startsWith("205-4")) {
                    empresa.setPorte(EnumPorteEmpresa.ASSOCIACAO_PRIVADA.getSigla());
                }
                empresa.setRegimeTributario(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma());
                break;
            case "EMPRESA DE PEQUENO PORTE":
                empresa.setRegimeTributario(EnumRegimeTributario.SIMPLES_NACIONAL_EMPRE_PEQ_PORT.getForma());
                empresa.setPorte(EnumPorteEmpresa.EPP.getSigla());
                break;
            default:
                break;
        }

        if (!dadosEmpresa.getQsa().isEmpty()) {
            empresa.setResponsavel(dadosEmpresa.getQsa().get(0).getNome());
        }

        if (cnaePrincipal != null) {
            ClassificacaoCnaeDTO ccPrincipal = new ClassificacaoCnaeDTO(cnaePrincipal.getIdClassificacao(), cnaePrincipal);
            listCnaePrincipal.add(ccPrincipal);
        } else {
            listCnaePrincipal.add(new ClassificacaoCnaeDTO(new Classificacao(), new Cnae()));
        }

        listCnaeSecundario.addAll(atividadesSecundarias.stream()
                .map(atividade -> atividade.getCode().equals("00.00-0-00") ? null : cnaeService.buscarCnaePorCodigo(atividade.getCode(), atividade.getText()))
                .filter(Objects::nonNull)
                .map(cnae -> new ClassificacaoCnaeDTO(cnae.getIdClassificacao(), cnae)).collect(Collectors.toList()));

        empresa.setIdCnae(preencherCnaePrincipal(listCnaePrincipal));

        if (!listCnaeSecundario.isEmpty()) {
            empresa.setListEmpresaCnae(preencherCnaeSecundario(empresa, listCnaeSecundario, listEmpresaCnae));
        }
        return empresa;
    }

    public Empresa cadastrarEmpresa(EmpresaCadastroDTO empresaCadastroDTO) {
        CredenciamentoDTO credenciamentoDTO = new CredenciamentoDTO();
        credenciamentoDTO.setLogin(empresaCadastroDTO.getEmailResponsavel());
        credenciamentoDTO.setNome(empresaCadastroDTO.getNomeResponsavel());
        credenciamentoDTO.setSenha(empresaCadastroDTO.getSenhaResponsavel());
        String cpfCnpj = empresaCadastroDTO.getCpfCnpj() != null ? CpfCnpjUtil.removerMascaraCnpj(empresaCadastroDTO.getCpfCnpj()) : "";
        switch (cpfCnpj.length()) {
            case 11:
                if (!CpfCnpjUtil.validarCPF(cpfCnpj)) {
                    throw new CadastroException("CPF inválido.", null);
                }
                break;
            case 14:
                if (!CpfCnpjUtil.validarCNPJ(cpfCnpj)) {
                    throw new CadastroException("CNPJ inválido.", null);
                }
                break;
            case 0:
                break;
            default:
                throw new CadastroException("Documento inválido", null);
        }
        if (existeCnpjCadastrado(cpfCnpj)) {
            throw new CadastroException("Empresa já cadastrada no sistema, não será possivel finalizar o cadastro.", null);
        }
        empresaCadastroDTO.setCpfCnpj(cpfCnpj);
        Empresa empresa = new Empresa();
        empresa.setTipoEmpresa("EM");
        empresa.setTipoUso(empresaCadastroDTO.getTipoUso());
        empresa.setCnpj(cpfCnpj);
        empresa.setFone(empresaCadastroDTO.getTelefone() == null ? empresaCadastroDTO.getCelular() : empresaCadastroDTO.getTelefone());
        empresa.setEmail(empresaCadastroDTO.getEmailResponsavel());
        empresa.setCelular(empresaCadastroDTO.getCelular());
        empresa.setDataConstituicao(empresaCadastroDTO.getDataNascimento());
        empresa.setOndeConheceu(empresaCadastroDTO.getOndeConheceu());
        empresa.setPrazoUsarSemComprar(parametroGeralService.getParametro().getPrazoUsarSemComprar());
        if (empresaCadastroDTO.getIdCategoriaEmpresa() != null) {
            empresa.setIdCategoriaEmpresa(categoriaEmpresaService.buscar(empresaCadastroDTO.getIdCategoriaEmpresa()));
        }
        Usuario usuario;
        switch (cpfCnpj.length()) {
            case 14:
                empresa = buscarDadosEmpresaPorCNPJ(cpfCnpj, empresa);
                break;
            case 11:
                empresa.setNomeFantasia(empresaCadastroDTO.getNomeResponsavel());
                empresa.setDescricao(empresaCadastroDTO.getNomeResponsavel());
                empresa.setDataConstituicao(empresaCadastroDTO.getDataNascimento());
                empresa.getEndereco().setCep(empresaCadastroDTO.getCep());
                if (empresaCadastroDTO.getCodCidade() != null) {
                    empresa.getEndereco().setIdCidade(cidadeService.buscarPeloCodigoIBGE(empresaCadastroDTO.getCodCidade()));
                } else if (empresaCadastroDTO.getCep() != null) {
                    empresa.getEndereco().setIdCidade(cidadeService.getEnderecoPorCep(empresaCadastroDTO.getCep()).getCidade());
                }
                empresa.getEndereco().setEndereco(empresaCadastroDTO.getEndereco());
                empresa.getEndereco().setNumero(empresaCadastroDTO.getNumero());
                empresa.getEndereco().setComplemento(empresaCadastroDTO.getComplemento());
                empresa.getEndereco().setBairro(empresaCadastroDTO.getBairro());
                break;
            default:
                empresa.setNomeFantasia(empresaCadastroDTO.getNomeResponsavel());
                empresa.setDescricao(empresaCadastroDTO.getNomeResponsavel());
                break;
        }
        try {
            usuario = usuarioService.salvarUsuario(credenciamentoDTO, empresa);
            usuario.setTenat(empresa.getTenatID());

            usuarioService.concederAcessoUsuariosSuporte(empresa);
            loginAcessoService.autenticaUsuario(usuario, usuario.getSenha());
        } catch (LoginDuplicadoException | SenhaIncorretaException | EmailException | UsuarioException | IllegalArgumentException | IllegalStateException ex) {
            throw new CadastroException(ex.getMessage(), ex);
        } catch (Exception ex) {
            Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            throw new CadastroException("Ocorreu um erro ao cadastrar a empresa.", null);
        }
        try {
            if (SystemPreferencesUtil.getProp("ambiente").equalsIgnoreCase("producao")) {
                if (empresaCadastroDTO.getCelular() != null) {
                    smsService.send("Bem vindo. Baixe o App do SGFinancas pela Google Play ou pela App Store!", empresaCadastroDTO.getCelular());
                }
                EmailDTO emailDTO = EmailUtil.getEmailAvisoCadastro(usuario, empresa);
                emailService.enviarEmailMS(emailDTO, EmailService.ENVIO_OBRIGATORIO_SIM);
            }
        } catch (EmailException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return empresa;
    }

    public EnumTipoUsoSistema getEnumTipoUsoSistema() {
        return getEmpresa().getEnumTipoUsoSistema();
    }

    public Empresa buscaCredorParaAdiantamento(Double valorTotal) {
        List<Empresa> credores = repositorio.buscaCredorParaAdiantamento(valorTotal);

        Collections.sort(credores, (a, b) -> a.getTaxaRemunecacaoPlataforma().compareTo(b.getTaxaRemunecacaoPlataforma()));
        if (credores.isEmpty()) {
            return null;
        }
        Double taxa = credores.get(0).getTaxaRemunecacaoPlataforma();
        credores = credores.stream()
                .filter(item -> !item.getTaxaRemunecacaoPlataforma().equals(taxa))
                .collect(Collectors.toList());
        if (credores.isEmpty()) {
            return null;
        }
        Collections.sort(credores, (a, b) -> a.getTaxaCorte().compareTo(b.getTaxaCorte()));
        return credores.get(0);
    }

    public void salvarCredor(Empresa credor, String login, String senha, Usuario usuarioLogado) throws UsuarioException {
        try {
            if (credor.getId() == null) {
                credor.setTipo("");
                credor.setIndicadorInscricaoEstadual("NC");
                credor.setTipoConta("P");
                credor.setPrimeiroLogin("N");
                if (credor.getRegimeTributario() == null) {
                    credor.setRegimeTributario(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma());
                }
                adicionar(credor);
                List<GrupoEmpresa> listGrupoEmpresa = usuarioService.adicionarGrupoEmpresa(credor);

                Usuario user = new Usuario();
                user.setLogin(login);
                user.setNome(credor.getNomeFantasia());
                user.setSenha(StringUtil.criptografarMD5(senha));
                user.setEmail(credor.getEmail());
                user.setContaExpirada("N");
                user.setContaBloqueada("N");
                user.setCadCredenciamento("N");
                user.setIdPerfil(perfilService.getPerfil(EnumTipoUsuario.ANTECIPADOR.getTipo()));
                user.setAdministrator(false);
                user.setUsuarioGrupoEmpresaList(new ArrayList<>());

                listGrupoEmpresa.stream().filter(p -> p.getIdGrupo().getTipo().equals(EnumTipoGrupo.ANTECIPADOR.getTipo())).forEach(user::addGrupoEmpresa);
                usuarioService.adicionarUsuario(user, credor, senha);
            } else {
                salvar(credor);
                Usuario user = usuarioLogado;
                if (!Boolean.TRUE.equals(user.getIdPerfil().getEhCredor())) {
                    user = loginAcessoService.getCredorByEmpresa(credor);
                }

                boolean alterou = false;
                if (!credor.getNomeFantasia().equals(user.getNome())) {
                    user.setNome(credor.getNomeFantasia());
                    alterou = true;
                }
                if (!login.equals(user.getLogin())) {
                    user.setLogin(login);
                    alterou = true;
                }
                if (!senha.isEmpty()) {
                    user.setSenha(StringUtil.criptografarMD5(senha));
                    alterou = true;
                }
                if (alterou) {
                    usuarioService.salvarUsuario(user, user.getSenha(), credor);
                }
            }
        } catch (Exception ex) {
            throw new UsuarioException(ex.getMessage(), ex);
        }
    }

    public List<TipoSocialEmpresa> getTipoSocialEmpresa() {
        return tipoSocialEmpresaRepositorio.getTipoSocialEmpresa();
    }

    public void atualizaConfiguracaoCriaUsuario(Empresa empresa, String criaUsuarioAoCadastrarCliente) {
        String tenant = adHocTenant.getTenantID();
        try {
            adHocTenant.setTenantID(empresa.getTenatID());
            ParametroSistema ps = parametroSistemaService.getParametroPorEmpresa(empresa);
            ps.setCriarUsuarioParaClienteCadastrado(criaUsuarioAoCadastrarCliente);
            parametroSistemaService.salvar(ps);
            if ("S".equals(ps.getCriarUsuarioParaClienteCadastrado())) {
                clienteService.listar().forEach(clienteService::salvar);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            adHocTenant.setTenantID(tenant);
        }
    }

    public List<AcessoPorEmpresaDTO> listaAcessosPorEmpresa(boolean showSuporte, boolean showContabilidade, In<String> tipoPagamento, MinMax<Date> periodo) {
        List<AcessoPorEmpresaDTO> lista = repositorio.listaAcessosPorEmpresa(showSuporte, showContabilidade, tipoPagamento, periodo);
        List<PagamentoMensalidade> pmList = pagamentoMensalidadeService.buscarPagamentosPor(periodo);

        lista.forEach(dto -> dto.setValor(pmList.stream()
                .filter(pm -> pm.getIdEmpresa().getId().equals(dto.getIdEmpresa()) && pm.getIdUsuarioPagamento().getId().equals(dto.getIdUsuario()))
                .filter(pm -> DataUtil.dateToString(pm.getDataPagamento(), "MM/yyyy").equals(dto.getData()))
                .mapToDouble(pm -> {
                    pm.setProcessado(true);
                    return pm.getValorPago();
                }).sum()));

        if (pmList.stream().anyMatch(pm -> !pm.isProcessado())) {
            pmList.stream()
                    .filter(pm -> !pm.isProcessado())
                    .map(AcessoPorEmpresaDTO::new)
                    .forEach(dto -> {
                        AcessoPorEmpresaDTO acesso = lista.stream()
                                .filter(f -> f.equals(dto))
                                .findAny().orElse(null);
                        if (acesso != null) {
                            acesso.add(dto);
                        } else {
                            lista.add(dto);
                        }
                    });
            Collections.sort(lista, (a, b) -> {
                if (!a.getEmpresa().equals(b.getEmpresa())) {
                    return a.getEmpresa().compareTo(b.getEmpresa());
                }
                return a.getUsuario().compareTo(b.getUsuario());
            });
        }

        return lista;
    }

}
