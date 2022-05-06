package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.RecorrenciaAgendamento;
import br.com.villefortconsulting.sgfinancas.entidades.TipoProdutoUso;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AuthDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ParametroSistemaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ParametroSistemaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConfirmacaoAgendamento;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoProduto;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumAmbienteEmissaoNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.json.simple.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParametroSistemaService extends BasicService<ParametroSistema, ParametroSistemaRepositorio, ParametroSistemaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private EmpresaService empresaService;

    private static final String URL_MS_NFE = "nfe/v4/service/";

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostConstruct
    @PostActivate
    protected void postConstruct() {
        repositorio = new ParametroSistemaRepositorio(em, adHocTenant);
    }

    public ParametroSistema addParametroSistema(ParametroSistema parm) {
        return adicionar(parm);
    }

    public ParametroSistema getInstrucaoBoleto(Empresa empresa) {
        return repositorio.getInstrucaoBoleto(empresa);
    }

    public ParametroSistema setParametroSistema(ParametroSistema parm) {
        return alterar(parm);
    }

    public boolean salvarCredenciaisNFCe(ParametroSistema param) {
        JSONObject json = new JSONObject();
        json.put("csc", param.getNfceCsc());
        json.put("idToken", param.getNfceToken());

        ResponseEntity<String> response = MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.NFE, URL_MS_NFE + "nfce/credentials", json, getHttpHeaders(param));
        return response.getStatusCode() == HttpStatus.OK;
    }

    public String registrarParaEnvioDeNfe(ParametroSistema param) throws IOException {
        Empresa empresa = empresaService.getEmpresa();
        JSONObject json = new JSONObject();
        json.put("username", CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
        json.put("state", empresa.getEndereco().getIdCidade().getIdUF().getDescricao());

        ResponseEntity<String> response = MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.NFE, URL_MS_NFE + "signUp", json, getHttpHeaders(param));
        if (response.getStatusCode() == HttpStatus.CREATED) {
            ObjectMapper mapper = new ObjectMapper();

            AuthDTO auth = mapper.readValue(response.getBody(), AuthDTO.class);
            return auth.getToken();
        }
        return null;
    }

    public boolean salvarCertificadoParaEnvioDeNfe(Empresa empresa) {
        ParametroSistema param = getParametro();
        if (param.getNfeMsToken() == null || param.getNfeMsToken().isEmpty() || empresa.getIdDocumento() == null || empresa.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            return true;
        }

        try {
            List<DocumentoAnexo> docList = empresa.getIdDocumento().getDocumentoAnexoList();
            byte[] conteudoArquivo = docList.get(docList.size() - 1).readFromFile();// Como o arquivo acabou de ser adicionado, ele deve ser o último
            File file = File.createTempFile("tempfile", ".pfx");
            try (OutputStream out = new FileOutputStream(file)) {
                out.write(conteudoArquivo, 0, conteudoArquivo.length);
            }
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("certificateFile", new FileSystemResource(file));
            body.add("password", empresa.getSenhaCertificado());

            ResponseEntity<String> response = MicroServiceUtil.doFormDataPost(MicroServiceUtil.MicroServicos.NFE, URL_MS_NFE + "certificate", body, getHttpHeaders(param));
            return response.getStatusCode() == HttpStatus.ACCEPTED;
        } catch (IOException ex) {
            return false;
        }
    }

    public ParametroSistema getParametro() {
        return repositorio.getParametro();
    }

    @Override
    public Criteria getModel(ParametroSistemaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public boolean temParametroEmpresa(Empresa empresa) {
        return repositorio.temParametroEmpresa(empresa);
    }

    public boolean dadosImportacaoPreenchidos() {
        ParametroSistema parametroSistema = getParametro();
        if (parametroSistema != null) {

            return parametroSistema.getIdCategoriaImportacaoProduto() != null
                    && parametroSistema.getIdCategoriaImportacaoServico() != null;

        } else {
            return false;
        }

    }

    public boolean dadosNotaFiscalPreenchidos() {

        ParametroSistema parametroSistema = getParametro();

        if (parametroSistema != null) {

            return parametroSistema.getNumNotaFiscal() != null
                    && parametroSistema.getSerieEntrada() != null
                    && parametroSistema.getSerieSaida() != null;

        } else {
            return false;
        }
    }

    public ParametroSistema getParametroPorEmpresa(Empresa empresa) {
        return repositorio.pegaParametroPorEmpresa(empresa);
    }

    public ParametroSistema populaParametroSistema(String tenatID) {
        ParametroSistema parametro = new ParametroSistema();
        adHocTenant.setTenantID(tenatID);

        parametro.setTenatID(adHocTenant.getTenantID());
        List<TipoProdutoUso> tipoProdutoUsoList = new ArrayList<>();
        for (EnumTipoUsoProduto tipoUso : EnumTipoUsoProduto.values()) {
            for (EnumTipoProdutoVenda tipoProduto : EnumTipoProdutoVenda.values()) {
                tipoProdutoUsoList.add(new TipoProdutoUso(tipoUso.getTipo(), tipoProduto.getTipo(), parametro));
            }
        }

        parametro.setEnvioNotaProduto(EnumAmbienteEmissaoNF.PRODUCAO.getTipo());
        parametro.setEnvioNotaServico(EnumAmbienteEmissaoNF.PRODUCAO.getTipo());
        parametro.setPeriodoAlmoco(60);
        parametro.setPeriodoAlmocoEstagiario(20);
        parametro.setPeriodoAlmocoGestaoPessoa(60);
        parametro.setPeriodoAlmocoMenorAprendiz(20);
        parametro.setHoraTrabalhadaFuncionario("08:48");
        parametro.setHoraTrabalhadaEstagiario("06:00");
        parametro.setHoraTrabalhadaMenorAprendiz("06:00");
        parametro.setHoraInicioJornada("08:00");
        parametro.setHoraFimJornada("18:00");
        parametro.setIdPlanoContaCliente(getPlanoContaPorCodigo("1.1.2.01", tenatID));
        parametro.setIdPlanoContaContaBancaria(getPlanoContaPorCodigo("1.1.1.01", tenatID));
        parametro.setIdPlanoContaFornecedor(getPlanoContaPorCodigo("2.1.3", tenatID));
        parametro.setIdPlanoContaFuncionario(getPlanoContaPorCodigo("3.2.2.01", tenatID));
        parametro.setIdPlanoContaProduto(getPlanoContaPorCodigo("1.1.3.01.006", tenatID));
        parametro.setIdPlanoContaVendaPadrao(getPlanoContaPorCodigo("4.1.1.01.001", tenatID));
        parametro.setIdPlanoContaCompraPadrao(getPlanoContaPorCodigo("3.2.3.01", tenatID));
        parametro.setIdPlanoContaServico(getPlanoContaPorCodigo("4.1.1.02", tenatID));
        parametro.setIdPlanoContaTransportadora(getPlanoContaPorCodigo("3.2.1.04.001", tenatID));
        parametro.setGeraContaUnicaEmOrcamento("S");
        parametro.setStatusOSGeradaPorOrcamento(EnumStatusOrdemDeServico.NOVO.getCodigo());
        parametro.setObservacaoImpressaoOrcamento("");
        parametro.setPermiteVendaSemEstoqueProdutosNormais("S");
        parametro.setPermiteVendaSemEstoqueProdutosCompostos("N");
        parametro.setTenatID(tenatID);
        parametro.setHabilitaAgenda("S");
        parametro.setEnviaSmsClienteAgendamento("S");
        parametro.setEnviaSmsClienteAlteracaoAgendamento("S");
        parametro.setEnviaSmsClienteUmDiaAntes("S");
        parametro.setEnviaSmsEmpresaSolicitacao("S");
        parametro.setEnviaSmsFuncionarioConfirmacao("S");
        parametro.setAgendaIntervaloHorario(30);
        parametro.setTipoConfirmacaoAgendamento(EnumTipoConfirmacaoAgendamento.AUTOMATICA.getTipo());
        parametro.setRecorrenciaAgendamentoList(new ArrayList<>());
        parametro.getRecorrenciaAgendamentoList().add(new RecorrenciaAgendamento("S", "S", 2, parametro));
        parametro.setTipoProdutoUsoList(tipoProdutoUsoList);
        parametro.setAgendaIntervaloHorario(30);
        parametro.setIdPlanoContaVendaPadrao(planoContaService.obterPlanoContaPorDescricao("VENDA DE PRODUTOS NO MERCADO INTERNO", tenatID));
        parametro.setEnviaNotificacaoContas("S");
        parametro.setCriarUsuarioParaClienteCadastrado("N");
        parametro.setNotificacaoSmsEnviar("N");

        addParametroSistema(parametro);
        return parametro;

    }

    public void populaParametroContaBancariaPadrao(ParametroSistema parametro) {
        parametro.setAppContaBancariaPadrao(contaBancariaService.getContaBancariaPadrao((Integer) null));

        setParametroSistema(parametro);
    }

    public PlanoConta getPlanoContaPorCodigo(String codigo, String tenatID) {
        return planoContaService.obterPlanoContaPorCodigo(codigo, tenatID);
    }

    public boolean isNotaFiscalProdutoProducao() {
        return EnumAmbienteEmissaoNF.PRODUCAO.getTipo().equals(
                getParametro().getEnvioNotaProduto());
    }

    public boolean isNotaFiscalServicoProducao() {
        return EnumAmbienteEmissaoNF.PRODUCAO.getTipo().equals(
                getParametro().getEnvioNotaServico());
    }

    public String getAmbienteNotaFiscalProduto() {
        return isNotaFiscalProdutoProducao() ? EnumAmbienteEmissaoNF.PRODUCAO.getTipo() : EnumAmbienteEmissaoNF.HOMOLOGACAO.getTipo();
    }

    public String getAmbienteNotaFiscalServico() {
        return isNotaFiscalServicoProducao() ? EnumAmbienteEmissaoNF.PRODUCAO.getTipo() : EnumAmbienteEmissaoNF.HOMOLOGACAO.getTipo();
    }

    private Map<String, String> getHttpHeaders(ParametroSistema param) {
        Empresa empresa = empresaService.getEmpresa();
        String cnpj = CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj());
        String msToken = param.getNfeMsToken();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Basic " + Base64.getEncoder().encodeToString((cnpj + ":" + msToken).getBytes(StandardCharsets.UTF_8)));
        return headerMap;
    }

    public boolean updateParametro(String name, Object value) {
        return repositorio.updateParametro(name, value);
    }

    public Time getIntervaloAgendamento() {
        Integer intervalo = getParametro().getAgendaIntervaloHorario();
        if (intervalo == null) {
            intervalo = 30;
        }
        return new Time(intervalo * 60 * 1000l);
    }

}
