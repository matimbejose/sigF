package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.CategoriaEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Classificacao;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContatoTitular;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaCnae;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoTransferencia;
import br.com.villefortconsulting.sgfinancas.entidades.TipoSocialEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClassificacaoCnaeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteHdDDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LoginAcessoFiltro;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.AccountDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.AddressDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.BankAccountDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.CompanyDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.HolderDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.TransferPlanDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.UserDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.exception.CriarContaDigitalException;
import br.com.villefortconsulting.sgfinancas.servicos.CategoriaEmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.CnaeService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaDigitalService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.NFSService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoLogradouro;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoSistema;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import br.com.villefortconsulting.util.operator.In;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String MSG_ERRO_DESCONHECIDO = "Ocorreu um erro desconhecido no nosso sistema, entre em contato com o suporte.";

    public static final String URL_PAGINA_DADOS_ADICIONAIS_CONTA_DIGITAL = "cadastroDadosAdicionaisContadigital.xhtml";

    public static final String URL_LISTA_EMPRESA = "/restrito/empresa/listaEmpresa.xhtml";

    public static final String MSG_CONTA_CRIADA_COM_SUCESSO = "Conta Digital criada com sucesso.";

    protected static final String[] RAMO_EMPRESA = new String[]{
        "Agências de viagens e turismo",
        "Hotéis e similares",
        "Esporte, recreação e lazer",
        "Produção de vídeos cinematográficos e programas de televisão; ",
        "Produção de som e música",
        "Atividades artísticas, criativas e de espetáculos",
        "Seguros, previdência e planos de saúde",
        "Hospitais, clínicas e consultórios de atendimento à saúde humana",
        "Serviços de saúde home care",
        "Hospitais, clínicas e consultórios de atendimento à saúde animal",
        "Serviços de consultorias em geral",
        "Atividades de serviços financeiros",
        "Atividades imobiliárias",
        "Atividades jurídicas ",
        "Atividade de contabilidade e auditoria",
        "Administração e concepção de associações",
        "Organismos internacionais e outras instituições extraterritoriais",
        "Gráfica",
        "Educação",
        "Pesquisa e desenvolvimento científico",
        "Publicidade e pesquisa de mercado",
        "Seleção, agenciamento  mão-de-obra",
        "Atividades de vigilância, segurança e investigação",
        "Serviços de tecnologia da informação",
        "Serviços domésticos",
        "Fabricação de veículos automotores, reboques e carrocerias",
        "Comércio de veículos automotores e/ou motocicletas",
        "Reparação de veículos automotores e/ou motocicletas",
        "Fabricação de produtos, máquinas e equipamentos metálicos ou não",
        "Armazenamento, transporte e distribuição de produtos, máquinas e equipamentos metálicos ou não",
        "Confecção de artigos do vestuário e acessórios",
        "Comércio atacadista de calçados, vestuário, alimentos, bebidas e etc",
        "Comércio varejista de calçados, vestuário, alimentos, bebidas e etc",
        "Agricultura, pecuária e aquicultura",
        "Fabricação de produtos alimentícios",
        "Restaurantes e outros serviços de alimentação e bebidas",
        "Extração de minerais e hidrocarbonetos",
        "Eletricidade, gás e outras utilidades",
        "Captação, tratamento e distribuição de água",
        "Coleta, tratamento, disposição de resíduos; ",
        "Reciclagem de materiais",
        "Obras e construções civis ",
        "Serviços de arquitetura, decoração e engenharia",
        "Aluguéis de máquinas e equipamentos",
        "Manutenção, reparação e instalação de máquinas e equipamentos de informática e comunicação ou não",
        "Telecomunicações"
    };

    private List<String> messagesErrorList = new ArrayList<>();

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private ContaDigitalService contaDigitalService;

    @EJB
    private UFService ufService;

    @EJB
    private NFSService nfsService;

    @EJB
    private CnaeService cnaeService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private CategoriaEmpresaService categoriaEmpresaService;

    @Inject
    private UsuarioControle usuarioControle;

    private ContaBancaria contaBancariaSelecionada;

    private Empresa empresaSelecionada;

    private transient Part partLogo;

    private transient Part part;

    private String mensagemAjuda;

    private Date dataInicioRelatorio;

    private Date dataFimRelatorio;

    private LazyDataModel<Empresa> model;

    private LoginAcessoFiltro filtro = new LoginAcessoFiltro();

    private UF ufSelecionado;

    private transient UploadedFile file;

    private List<ClassificacaoCnaeDTO> listCnaePrincipal;

    private List<ClassificacaoCnaeDTO> listCnaeSecundario;

    private List<EmpresaCnae> listEmpresaCnae = new LinkedList<>();

    private Boolean pj;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                loginAcessoService::quantidadeRegistrosFiltrados,
                loginAcessoService::getListaFiltradaEmpresa,
                filter -> {
                    filter.setUsuario(getUsuarioLogado());
                    filter.setEmpresa(getEmpresaLogada());
                    filter.setTipoEmpresa(In.fromList(Arrays.asList("EM")));
                });
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_TRANSPORTADORA.getChave()).getDescricao());
        return "cadastroTransportadora.xhtml";
    }

    public void preecherData() {
        dataInicioRelatorio = DataUtil.getPrimeiroDiaDoMes(new Date());
        dataFimRelatorio = DataUtil.getUltimoDiaDoMes(new Date());
    }

    public Empresa getEmpresaLogada() {
        return empresaService.getEmpresa();
    }

    public boolean empresaPertenceAoSimplesNacional() {
        return empresaService.empresaPertenceAoSimplesNacional();
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Classificacao> getClassificacoes() {
        return empresaService.getClassificoes();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public void buscarEnderecoPorCep() {
        CepDTO cepDTO = cidadeService.buscarEnderecoPorCep(empresaSelecionada);
        if (cepDTO != null) {
            ufSelecionado = cepDTO.getUf();
        }
    }

    public String getRegimeTributario() {
        if (empresaSelecionada.getRegimeTributario() != null) {
            return EnumRegimeTributario.getDescricao(empresaSelecionada.getRegimeTributario());
        }
        return "";
    }

    //Retorna os valores do vetor do enum de regime tributario
    public EnumRegimeTributario[] getRegimesTributarios() {
        return EnumRegimeTributario.values();
    }

    public List<Cnae> listarCnaesPrincipais(Classificacao classificacao) {
        if (classificacao == null || classificacao.getId() == null) {
            return new ArrayList<>();
        }
        List<Cnae> cnaesDisponiveis = cnaeService.listarCnaePorClassificacao(classificacao);

        if (!listCnaeSecundario.isEmpty()) {
            List<Cnae> cSecundarios = listCnaeSecundario.stream()
                    .map(ClassificacaoCnaeDTO::getCnae)
                    .collect(Collectors.toList());
            cSecundarios.stream()
                    .filter(cSecundario -> cSecundario != null && cSecundario.getId() != null)
                    .forEachOrdered(cnaesDisponiveis::remove);
        }

        return cnaesDisponiveis;
    }

    public List<Cnae> listarCnaesSecundarios(ClassificacaoCnaeDTO ccDTO) {
        Classificacao classificacao = ccDTO.getClassificacao();

        if (classificacao == null || classificacao.getId() == null) {
            return new ArrayList<>();
        }

        List<Cnae> cnaesDisponiveis = cnaeService.listarCnaePorClassificacao(classificacao);

        if (!listCnaePrincipal.isEmpty()) {
            Cnae cPrincipal = listCnaePrincipal.get(0).getCnae();
            if (cPrincipal != null && cPrincipal.getId() != null) {
                cnaesDisponiveis.remove(cPrincipal);
            }
        }

        if (!listCnaeSecundario.isEmpty()) {
            Cnae cnae = ccDTO.getCnae();
            List<Cnae> cSecundarios = listCnaeSecundario.stream()
                    .map(ClassificacaoCnaeDTO::getCnae)
                    .collect(Collectors.toList());
            cSecundarios.stream()
                    .filter(cSecundario -> cSecundario != null && cSecundario.getId() != null && !cSecundario.equals(cnae))
                    .forEachOrdered(cnaesDisponiveis::remove);
        }

        return cnaesDisponiveis;
    }

    public void addCnaeSecundario() {
        this.listCnaeSecundario.add(new ClassificacaoCnaeDTO(new Classificacao(), new Cnae()));
    }

    public void removeCnaeSecundario(ClassificacaoCnaeDTO ccDTO) {
        this.listCnaeSecundario.remove(ccDTO);
    }

    public String doStartAdd() {
        this.empresaSelecionada = new Empresa();

        this.ufSelecionado = null;

        cleanCnae();
        this.listCnaePrincipal.add(new ClassificacaoCnaeDTO(new Classificacao(), new Cnae()));

        return "cadastroEmpresa.xhtml";
    }

    public String doStartAddCredor() {
        String path = doStartAdd();
        empresaSelecionada.setTipoEmpresa("CR");
        return path;
    }

    public void cleanCnae() {
        this.listCnaePrincipal = new ArrayList<>();
        this.listCnaeSecundario = new ArrayList<>();
    }

    public String doStartAlterarFromVisaoGeral() {
        try {
            empresaSelecionada = empresaService.buscar(Integer.parseInt(getUsuarioLogado().getTenat()));
            return "/restrito/empresa/cadastroEmpresa.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
            return "/restrito/empresa/listaEmpresa.xhtml";
        }
    }

    public String doStartAlterar() {
        this.part = null;
        this.file = null;

        if (empresaSelecionada.getEndereco() == null) {
            empresaSelecionada.setEndereco(new Endereco());
        }
        carregarCnae();
        carregarUF();
        pj = null;
        int size = CpfCnpjUtil.removerMascaraCnpj(empresaSelecionada.getCnpj()).length();
        if (size == 14) {
            pj = true;
        } else if (size == 11) {
            pj = false;
        }

        return "/restrito/empresa/cadastroEmpresa.xhtml";
    }

    public String getLabelPJ() {
        if (pj == null) {
            return "CPF/CNPJ";
        }
        return pj ? "CNPJ" : "CPF";
    }

    private void carregarCnae() {
        cleanCnae();

        // Carregando cnae principal
        Cnae cnaePrincipal = empresaSelecionada.getIdCnae();

        if (cnaePrincipal != null) {
            ClassificacaoCnaeDTO ccPrincipal = new ClassificacaoCnaeDTO(cnaePrincipal.getIdClassificacao(), cnaePrincipal);
            listCnaePrincipal.add(ccPrincipal);
        } else {
            this.listCnaePrincipal.add(new ClassificacaoCnaeDTO(new Classificacao(), new Cnae()));
        }

        // Carregando cnae secundario
        List<Cnae> cnaes = empresaService.listarEmpresaCnae(empresaSelecionada).stream()
                .map(EmpresaCnae::getIdCnae)
                .distinct()
                .collect(Collectors.toList());
        listCnaeSecundario.addAll(cnaes.stream()
                .map(cnaeSecundario -> new ClassificacaoCnaeDTO(cnaeSecundario.getIdClassificacao(), cnaeSecundario))
                .collect(Collectors.toList()));
    }

    private void carregarUF() {
        ufSelecionado = null;
        if (empresaSelecionada.getEndereco() != null && empresaSelecionada.getEndereco().getIdCidade() != null) {
            ufSelecionado = empresaSelecionada.getEndereco().getIdCidade().getIdUF();
        }
    }

    public String doFinishAdd() {
        try {
            // associando logo a empresa
            empresaSelecionada.setLogo(empresaService.getLogo(partLogo, empresaSelecionada));

            if (empresaSelecionada.isPJ()) {
                // associando empresa ao cnae principal
                empresaSelecionada.setIdCnae(empresaService.preencherCnaePrincipal(listCnaePrincipal));

                // associando empresa aos cnaes secundarios
                empresaSelecionada.setListEmpresaCnae(empresaService.preencherCnaeSecundario(empresaSelecionada, listCnaeSecundario, listEmpresaCnae));
            }

            // salva empresa e certificado
            empresaService.salvarEmpresa(empresaSelecionada, part, getUsuarioLogado());

            if (!parametroSistemaService.salvarCertificadoParaEnvioDeNfe(empresaSelecionada)) {
                createFacesErrorMessage("Ocorreu um erro ao salvar o certificado no MS");
            }

            createFacesSuccessMessage("Empresa salva com sucesso.");

            nfsService.atualizaMicrosservico();
            return "listaEmpresa.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroEmpresa.xhtml";
        }
    }

    public String doFinishExcluir() {
        empresaService.remover(empresaSelecionada);
        createFacesSuccessMessage("Empresa excluída com sucesso.");
        return "listaEmpresa.xhtml";
    }

    public void deletarAnexo() {
        empresaSelecionada.setIdDocumento(null);
        part = null;
    }

    public String doShowAuditoria() {
        return "listaAuditoriaEmpresa.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return empresaService.getDadosAuditoriaByID(empresaSelecionada);
    }

    public List<Empresa> getEmpresas() {
        return empresaService.getEmpresas();
    }

    public Boolean getNumeroMaximo() {
        return empresaService.getEmpresas().isEmpty();
    }

    public String getTipo() {
        return empresaService.getTipo();
    }

    public String getDescricaoTipoEmpresa() {
        return empresaService.getDescricaoTipoEmpresa();
    }

    public String getDescricaoTipoFuncionario() {
        return empresaService.getDescricaoTipoFuncionario();
    }

    public String getDescricaoTipoFuncionarioPlural() {
        return empresaService.getDescricaoTipoFuncionarioPlural();
    }

    public String doStartAlterarInf() {
        empresaSelecionada = empresaService.getEmpresPorTenatID();
        return doStartAlterar();
    }

    public Date obterDataHora() {
        return new Date();
    }

    public StreamedContent getLogo() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (empresaSelecionada != null && empresaSelecionada.getLogo() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(empresaSelecionada.getLogo()), "image/jpeg");
        }
        return null;
    }

    public StreamedContent getLogoEmpresaLogada() {
        Empresa empresa = empresaService.getEmpresPorTenatID();
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (empresa != null && empresa.getLogo() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(empresa.getLogo()), "image/jpeg");
        } else {
            return null;
        }
    }

    public void buscarDadosEmpresaPorCNPJ() {
        try {
            if (Boolean.TRUE.equals(pj)) {
                if (empresaSelecionada.getCnpj() != null) {
                    empresaService.buscarDadosEmpresaPorCNPJ(empresaSelecionada.getCnpj(), empresaSelecionada, listEmpresaCnae);
                } else {
                    createFacesErrorMessage("Informe o CNPJ para buscar.");
                }
            } else {
                if (empresaSelecionada.getCnpj() != null && empresaSelecionada.getDataConstituicao() != null) {
                    ClienteHdDDTO dados = clienteService.buscarDadosEmpresaPorCpf(empresaSelecionada.getCnpj(), empresaSelecionada.getDataConstituicao());
                    empresaSelecionada.setDescricao(dados.getNome());
                    empresaSelecionada.setNomeFantasia(dados.getNome());
                } else {
                    createFacesErrorMessage("Informe o CPF e a data de nascimento para buscar.");
                }
            }

            if (empresaSelecionada.getEndereco() != null && empresaSelecionada.getEndereco().getIdCidade() != null) {
                ufSelecionado = empresaSelecionada.getEndereco().getIdCidade().getIdUF();
            }
            cleanCnae();

            if (listCnaePrincipal != null && empresaSelecionada.getIdCnae() != null) {
                listCnaePrincipal.add(new ClassificacaoCnaeDTO(empresaSelecionada.getIdCnae().getIdClassificacao(), empresaSelecionada.getIdCnae()));
            }
            listCnaeSecundario = empresaSelecionada.getListEmpresaCnae().stream()
                    .map(ec -> new ClassificacaoCnaeDTO(ec.getIdCnae().getIdClassificacao(), ec.getIdCnae()))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            if (!ex.getMessage().isEmpty()) {
                createFacesErrorMessage(ex.getMessage());
            }
        } catch (NotFoundException ex) {
            createFacesInfoMessage(ex.getMessage());
        }
    }

    public List<EnumTipoLogradouro> getListaTiposLogradouros() {
        return Arrays.asList(EnumTipoLogradouro.values());
    }

    public List<CategoriaEmpresa> getCategoriaEmpresaList() {
        return categoriaEmpresaService.listar();
    }

    public EnumTipoUsoSistema getEnumTipoUsoSistema() {
        return empresaService.getEnumTipoUsoSistema();
    }

    public boolean isLogadoComoEmpresa() {
        return empresaService.getEmpresa() != null && !empresaService.getEmpresa().isCredor();
    }

    public boolean isLogadoComoCredor() {
        return empresaService.getEmpresa() != null && empresaService.getEmpresa().isCredor();
    }

    public boolean isLogadoComoGestor() {
        return empresaService.getEmpresa() == null;
    }

    public String getDescricaoBreadcrumb() {
        if (isLogadoComoEmpresa()) {
            return empresaService.getEmpresa().getDescricao();
        } else if (isLogadoComoCredor()) {
            return "Módulo credor";
        } else if (isLogadoComoGestor()) {
            return "Módulo gestor";
        } else {
            return "SG FInanças";
        }
    }

    public void preecherEmpresa() {
        if (empresaSelecionada == null) {
            empresaSelecionada = empresaService.getEmpresa();
        }
    }

    public String acessoCadastroDadosAdicionaisEmpresa() {

        if (empresaSelecionada.getPlanoTransferencia() == null) {
            empresaSelecionada.setPlanoTransferencia(new PlanoTransferencia());
        }
        if (empresaSelecionada.getContatoTitular() == null) {
            empresaSelecionada.setContatoTitular(new ContatoTitular());
        }

        return "/restrito/contadigital/cadastroDadosAdicionaisContadigital.xhtml?faces-redirect=true";
    }

    public String doFinishCriarContaDigital() {
        try {
            AccountDTO accountDTO = validarCampos(empresaSelecionada);
            contaDigitalService.criarContaDigital(accountDTO);
            empresaService.salvar(empresaSelecionada);

            createFacesSuccessMessage(MSG_CONTA_CRIADA_COM_SUCESSO);
            return URL_LISTA_EMPRESA;
        } catch (CriarContaDigitalException e) {
            e.getMessagens().forEach(this::createFacesErrorMessage);
            return URL_PAGINA_DADOS_ADICIONAIS_CONTA_DIGITAL;
        } catch (Exception e) {
            createFacesErrorMessage(MSG_ERRO_DESCONHECIDO);
            return URL_PAGINA_DADOS_ADICIONAIS_CONTA_DIGITAL;
        }
    }

    private AccountDTO validarCampos(Empresa empresaSelecionada) throws CriarContaDigitalException {
        messagesErrorList.clear();
        empresaSelecionada.setIdContaBancariaDigital(empresaSelecionada.getIdContaBancariaDigital()); //TODO extrair isso para um metodo

        UserDTO userDTO = UserDTO.buildUser(empresaSelecionada);
        getViolations(userDTO).forEach(this::errorsDomainList);

        AddressDTO addressDTO = AddressDTO.buildAddress(empresaSelecionada);
        getViolations(addressDTO).forEach(this::errorsDomainList);

        CompanyDTO companyDTO = CompanyDTO.buildCompany(empresaSelecionada);
        getViolations(companyDTO).forEach(this::errorsDomainList);

        HolderDTO holderDTO = HolderDTO.buildHolder(empresaSelecionada);
        holderDTO.setCompany(companyDTO);
        getViolations(holderDTO).forEach(this::errorsDomainList);

        BankAccountDTO bankAccountDTO = BankAccountDTO.buildBankAccount(empresaSelecionada);
        getViolations(bankAccountDTO).forEach(this::errorsDomainList);

        TransferPlanDTO transferPlanDTO = TransferPlanDTO.buildTransferPlanDTO(empresaSelecionada);

        AccountDTO accountDTO = AccountDTO.builder().
                businessActivityId("Ep") //TODO "outros serviços", fazer de-para da tabela de categoria
                .marketingMediaId("dZ")
                .appUrl("")
                .commercialName(empresaSelecionada.getDescricao())
                .holder(holderDTO)
                .address(addressDTO)
                .bankAccount(bankAccountDTO)
                .user(userDTO)
                .transferPlan(transferPlanDTO)
                .build();

        if (!messagesErrorList.isEmpty()) {
            throw new CriarContaDigitalException(messagesErrorList);
        }
        return accountDTO;
    }

    public List<TipoSocialEmpresa> getListaTipoSocialEmpresa() {
        return empresaService.getTipoSocialEmpresa();
    }

    private void errorsDomainList(ConstraintViolation<Serializable> a) {
        messagesErrorList.add(a.getMessage());
    }

    public StreamedContent gerarRelatorioIndicacao() {
        try {
            return relatorioService.gerarExcelRelatorioIndicacao(dataInicioRelatorio, dataFimRelatorio);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());

        }
        return null;

    }

    public StreamedContent baixaTermo() {
        try {
            File termo = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathTermoAdesao", NotFoundException::new));
            return FileUtil.downloadFile(FileUtil.convertFileToBytes(termo), "application/octet-stream", "Termo de uso.pdf");
        } catch (FileException | NotFoundException ex) {
            Logger.getLogger(WizardControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível buscar o termo.");
            return null;
        } catch (Exception ex) {
            Logger.getLogger(WizardControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao processar a requisição.");
            return null;
        }
    }

    public StreamedContent baixaPoliticaPrivacidade() {
        try {
            File termo = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathPoliticaPrivacidade", NotFoundException::new));
            return FileUtil.downloadFile(FileUtil.convertFileToBytes(termo), "application/octet-stream", "Política de Privacidade.pdf");
        } catch (FileException | NotFoundException ex) {
            Logger.getLogger(WizardControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível buscar o termo.");
            return null;
        } catch (Exception ex) {
            Logger.getLogger(WizardControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao processar a requisição.");
            return null;
        }
    }

    public String[] getRamoEmpresa() {
        return RAMO_EMPRESA;
    }

    public String doStartAlterarEmpresaLogada() {
        empresaSelecionada = empresaService.getEmpresa();
        return "/restrito/empresa/cadastroEmpresa.xhtml";
    }

}
