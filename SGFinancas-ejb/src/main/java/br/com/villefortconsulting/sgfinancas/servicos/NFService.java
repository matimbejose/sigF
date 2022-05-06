package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEndereco;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TVeiculo;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFCorrecao;
import br.com.villefortconsulting.sgfinancas.entidades.NfInutilizacao;
import br.com.villefortconsulting.sgfinancas.entidades.Transportadora;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.InutilizacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NotaFiscalProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.Filler;
import br.com.villefortconsulting.sgfinancas.nfe.util.Validator;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.nfe.NfeProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.NFRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumAmbienteEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumDestinoOperacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFinalidadeNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFormatoImpressaoDanfe;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumModeloEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumOperacaoConsumidor;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoFrete;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoGeracaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Pag;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Pag.DetPag;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NFService extends BasicService<NF, NFRepositorio, NotaFiscalProdutoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private VendaService vendaService;

    @EJB
    private CompraService compraService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private EmailService emailService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private UFService ufService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private CstService cstService;

    @EJB
    private CsosnService csosnService;

    @EJB
    private CfopService cfopService;

    @EJB
    private NcmService ncmService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private NfeProdutoService nfeProdutoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new NFRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(NotaFiscalProdutoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idNfReferencia", "idNfReferencia", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idFornecedor", "idFornecedor", JoinType.LEFT_OUTER_JOIN);

        addIlikeRestrictionTo(criteria, "descricaoServico", filter.getDescricao(), MatchMode.ANYWHERE);

        addEqRestrictionTo(criteria, "numeroNotaFiscal", filter.getNumero());
        addEqRestrictionTo(criteria, "valorNota", filter.getValor());
        addEqRestrictionTo(criteria, "idCliente", filter.getCliente());
        addEqRestrictionTo(criteria, "idFornecedor", filter.getFornecedor());
        addRestrictionTo(criteria, "dataGeracao", filter.getDataGeracao());

        addIlikeRestrictionTo(criteria, "idCliente.email", filter.getEmail(), MatchMode.ANYWHERE);

        if (filter.getDataEmissaoInicio() != null) {
            criteria.add(Restrictions.ge("dataEmissao", filter.getDataEmissaoInicio()));
        }

        if (filter.getDataEmissaoFim() != null) {
            criteria.add(Restrictions.le("dataEmissao", filter.getDataEmissaoFim()));
        }

        if (filter.getSituacoes() != null && !filter.getSituacoes().isEmpty()) {
            criteria.add(Restrictions.in("situacao", filter.getSituacoes()));
        }

        return criteria;
    }

    public boolean existeCodigoNumero(Integer codigoNumerico) {
        return repositorio.existeCodigoNumero(codigoNumerico);
    }

    public boolean existeNotaComNumero(NF nf) {
        return repositorio.existeNotaComNumero(nf);
    }

    public Integer obterProximoNumeroNF() {
        return repositorio.obterProximoNumeroNF();
    }

    public List<NF> notasPorTransportadora(Transportadora transportadora) {
        return repositorio.notasPorTransportadora(transportadora);
    }

    public void enviarXmlPorEmail(NF nf, Empresa empresa, List<VendaProduto> produtos) throws EmailException {
        try {
            EmailDTO emailDTO = EmailUtil.getEmailNotaXml(nf);
            emailService.enviarEmailNotaFiscal(emailDTO, nf, empresa, produtos, "S");
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage(), ex);
        }
    }

    public List<Object> getDadosAuditoriaByID(NFCorrecao obj) {
        return repositorio.getDadosAuditoriaByID(NFCorrecao.class, obj.getId());
    }

    public void loadXML(NF nf) throws IOException {
        DocumentoAnexo documentoAnexo = nf.getIdDocumento() == null ? null : nf.getIdDocumento().getDocumentoAnexoList().stream()
                .filter(anexo -> anexo.getNomeArquivo().equals(NF.XML_ENVIO))
                .findAny().orElse(null);
        if (documentoAnexo != null) {
            byte[] bytes = documentoAnexo.readFromFile();
            if (bytes != null) {
                nf.setObjTNFe(new ObjectMapper().readValue(new String(bytes, StandardCharsets.UTF_8), NFe.class));
            }
        }
    }

    public void loadXMLWithEntity(NF nf) throws IOException {
        if (nf.getObjTNFe() == null) {
            loadXML(nf);
        }
        NFe nfe = nf.getObjTNFe();
        nfe.getInfNFe().getDet().forEach(det -> {
            det.getIdCst().fetchOn(cstService);
            det.getProd().getIdCfop().fetchOn(cfopService);
            det.getProd().getIdNcm().fetchOn(ncmService);
            det.getProd().getIdProduto().fetchOn(produtoService);
            det.getIdCsosn().fetchOn(csosnService);
            det.getIdCst().fetchOn(cstService);
        });
    }

    public NF salvarNotaFiscalProduto(NF nf, Usuario usuarioLogado) throws NfeException {
        Empresa empresa = empresaService.getEmpresPorTenatID();
        if (nf.getId() == null) {
            nf.setNumeroNotaFiscal(obterProximoNumeroNF());
            nf.setCodigoNumerico(gerarCodigoNumerico());
            nf.setChave(Filler.gerarChaveNF(empresa, nf));
        } else {
            if (!nf.getObjTNFe().getInfNFe().getIde().getNNF().equals(nf.getNumeroNotaFiscal() + "")) {
                nf.setNumeroNotaFiscal(Integer.parseInt(nf.getObjTNFe().getInfNFe().getIde().getNNF()));
                nf.setChave(Filler.gerarChaveNF(empresa, nf));
                nf.getObjTNFe().getInfNFe().setId(nf.getChaveFormatada());
            }
        }
        nf.setDataGeracao(new Date());
        Filler.preencherNFe(nf, empresa);
        Filler.processaDet(nf, empresa);
        List<String> erros = Validator.validarNf(nf, empresa);
        if (!erros.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Foram encontrados ")
                    .append(erros.size())
                    .append(" erros de validação. Não será possível enviar a nota até que eles tenham sido corrigidos.");
            erros.stream().map(err -> "\n" + err).forEachOrdered(sb::append);
            throw new NfeException(sb.toString(), null);
        }

        DocumentoAnexo documentoAnexo = nf.getIdDocumento() == null ? null : nf.getIdDocumento().getDocumentoAnexoList().stream()
                .filter(anexo -> anexo.getNomeArquivo().equals(NF.XML_ENVIO))
                .findAny().orElse(null);

        nf.getObjTNFe().getInfNFe().getIde().setCNF(StringUtil.adicionarCaracterEsquerda("" + nf.getCodigoNumerico(), "0", 8));
        nf.getObjTNFe().getInfNFe().setId("NFe" + nf.getChave());
        nf.setValorNota(nf.getObjTNFe().getInfNFe().getDet().stream()
                .mapToDouble(det -> det.getProd().getVProd())
                .sum());

        try {
            byte[] xml = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsBytes(nf.getObjTNFe());
            if (documentoAnexo == null) {
                nf.setIdDocumento(documentoService.criarDocumento(usuarioLogado, NF.XML_ENVIO, "application/xml", xml));
            } else {
                nf.getIdDocumento().getDocumentoAnexoList().add(documentoAnexo.writeToFile(xml));
            }
        } catch (IOException | FileException ex) {
            if (nf.getIdDocumento() != null) {
                DocumentoAnexo aux = nf.getIdDocumento().getDocumentoAnexoList().stream()
                        .filter(da -> da.getNomeArquivo().equals(NF.XML_ENVIO))
                        .findAny()
                        .orElse(null);
                if (aux != null) {
                    aux.writeToFile(null);
                }
            }
            throw new NfeException("Não foi possível salvar a NFe.", ex);
        }

        return salvar(nf);
    }

    public Venda validarVendaNFe(Venda venda) throws NfeException {
        StringBuilder erros = new StringBuilder();

        for (VendaProduto vendaProduto : venda.getVendaProdutoList()) {
            if (vendaProduto.getDadosProduto().getQuantidade() == null || vendaProduto.getDadosProduto().getQuantidade() < 1) {
                erros.append("Informe a quantidade para o produto ").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (vendaProduto.getDadosProduto().getValor() == null || vendaProduto.getDadosProduto().getValor() < 0.01) {
                erros.append("Informe o valor de venda para o produto ").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (vendaProduto.getDadosProduto().getIdCfop() == null || vendaProduto.getDadosProduto().getIdCfop().getId() == null) {
                erros.append("Informe o CFOP para o produto ").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (vendaProduto.getDadosProduto().getIdNcm() == null || vendaProduto.getDadosProduto().getIdNcm().getId() == null) {
                erros.append("Informe o NCM para o produto ").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (empresaService.empresaPertenceAoSimplesNacional()) {
                if (vendaProduto.getDadosProduto().getIdCsosn() == null || vendaProduto.getDadosProduto().getIdCsosn().getId() == null) {
                    erros.append("Informe o CSOSN para o produto ").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
                }
            } else {
                if (vendaProduto.getDadosProduto().getIdCst() == null || vendaProduto.getDadosProduto().getIdCst().getId() == null) {
                    erros.append("Informe o CST para o produto ").append(vendaProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
                }
            }
        }

        if (StringUtils.isNotEmpty(erros.toString())) {
            throw new NfeException(erros.toString(), null);
        }

        return venda;
    }

    public Compra validarCompraNFe(Compra compra) throws NfeException {
        StringBuilder erros = new StringBuilder();
        for (CompraProduto compraProduto : compra.getListCompraProduto()) {
            if (compraProduto.getDadosProduto().getQuantidade() == null || compraProduto.getDadosProduto().getQuantidade() < 1) {
                erros.append("Informe a quantidade para o produto ").append(compraProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (compraProduto.getDadosProduto().getValor() == null || compraProduto.getDadosProduto().getValor() < 0.01) {
                erros.append("Informe o valor de venda para o produto ").append(compraProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (compraProduto.getDadosProduto().getIdCfop() == null || compraProduto.getDadosProduto().getIdCfop().getId() == null) {
                erros.append("Informe o CFOP para o produto ").append(compraProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (compraProduto.getDadosProduto().getIdNcm() == null || compraProduto.getDadosProduto().getIdNcm().getId() == null) {
                erros.append("Informe o NCM para o produto ").append(compraProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
            }
            if (empresaService.empresaPertenceAoSimplesNacional()) {
                if (compraProduto.getDadosProduto().getIdCsosn() == null || compraProduto.getDadosProduto().getIdCsosn().getId() == null) {
                    erros.append("Informe o CSOSN para o produto ").append(compraProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
                }
            } else {
                if (compraProduto.getDadosProduto().getIdCst() == null || compraProduto.getDadosProduto().getIdCst().getId() == null) {
                    erros.append("Informe o CST para o produto ").append(compraProduto.getDadosProduto().getIdProduto().getDescricao()).append(System.lineSeparator());
                }
            }
        }

        if (StringUtils.isNotEmpty(erros.toString())) {
            throw new NfeException(erros.toString(), null);
        }

        return compra;
    }

    private Integer gerarCodigoNumerico() {
        Integer numero;
        do {
            numero = Math.abs((int) (Math.random() * 100_000_001));
        } while (existeCodigoNumero(numero));
        return numero;
    }

    public List<NFCorrecao> obterLitaCorrecoes(NF nf) {
        return repositorio.obterLitaCorrecoes(nf);
    }

    public NFCorrecao preecherNFCorrecao(NF nf) {
        NFCorrecao nFCorrecao = new NFCorrecao();
        nFCorrecao.setSituacao(EnumSituacaoNF.RASCUNHO.getSituacao());
        nFCorrecao.setIdNf(nf);
        nFCorrecao.setTenatID(adHocTenant.getTenantID());
        nFCorrecao.setData(new Date());

        return nFCorrecao;
    }

    public NfInutilizacao salvarInutilizacao(NfInutilizacao nfInutilizacao) {
        return repositorio.salvarInutilizacao(nfInutilizacao);
    }

    public List<NfInutilizacao> buscarInutilizacoes(Integer id) {
        return repositorio.buscarInutilizacoes(id);
    }

    public NfInutilizacao inutilizacaoDtoToEntity(InutilizacaoDTO inut, Usuario usuario) {
        return new NfInutilizacao(
                Integer.parseInt(DataUtil.getAno(inut.getAno()).toString().substring(2, 4)),
                ufService.buscarCUF(inut.getUf()),
                Integer.parseInt(inut.getModelo()),
                Integer.parseInt(inut.getSerie()),
                Integer.parseInt(inut.getNumeroInicial()),
                Integer.parseInt(inut.getNumeroFinal()),
                inut.getMotivo(),
                usuario);
    }

    public boolean notaDevolvida(NF nf) {
        return repositorio.notaDevolvida(nf);
    }

    public void preencherNFeVazia(NF nf) {
        NFe nfe = new NFe();
        nf.setSituacao(EnumSituacaoNF.RASCUNHO.getSituacao());
        nfe.setInfNFe(new NFe.InfNFe());
        nfe.getInfNFe().setIde(new NFe.InfNFe.Ide());
        nfe.getInfNFe().getIde().setSerie("" + parametroSistemaService.getParametro().getSerieSaida());
        nfe.getInfNFe().getIde().setTpAmb(parametroSistemaService.getParametro().getEnvioNotaProduto());
        nfe.getInfNFe().setEmit(new NFe.InfNFe.Emit());
        nfe.getInfNFe().getEmit().setEnderEmit(new TEnderEmi());
        nfe.getInfNFe().setDest(new NFe.InfNFe.Dest());
        nfe.getInfNFe().getDest().setEnderDest(new TEndereco());
        nfe.getInfNFe().setTotal(new NFe.InfNFe.Total());
        nfe.getInfNFe().getTotal().setICMSTot(new NFe.InfNFe.Total.ICMSTot());
        nfe.getInfNFe().setTransp(new NFe.InfNFe.Transp());
        nfe.getInfNFe().getTransp().setModFrete(EnumTipoFrete.SEM_FRETE.getTipo());
        nfe.getInfNFe().getTransp().setVeicTransp(new TVeiculo());
        nfe.getInfNFe().setPag(new NFe.InfNFe.Pag());
        nfe.getInfNFe().getPag().getDetPag().add(new NFe.InfNFe.Pag.DetPag());
        nfe.getInfNFe().setInfAdic(new NFe.InfNFe.InfAdic());
        nf.setObjTNFe(nfe);
        nf.setTipoGeracao(EnumTipoGeracaoNF.AVULSO.getTipo());

        NFe.InfNFe infNfe = nf.getObjTNFe().getInfNFe();
        Empresa empresa = empresaService.getEmpresPorTenatID();
        infNfe.setIde(Filler.preencherIde(nf, empresa, EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo(), "0.01"));
        infNfe.setEmit(Filler.preencherEmit(nf, empresa));
        if (infNfe.getIde().getTpAmb().equals("2")) {
            infNfe.getDest().setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
        }
    }

    public void preencherNFeParaEdicao(NF nf, Venda venda) {
        preencherNFeVazia(nf);
        nf.setModeloNota(EnumModeloEmissaoNF.VENDA.getModelo());
        nf.setTipoGeracao(EnumTipoGeracaoNF.SISTEMA.getTipo());
        Empresa empresa = empresaService.getEmpresPorTenatID();

        nf.setIdVenda(venda);
        if (venda.getIdOrcamentoOSOrigem() != null) {
            nf.setIdCliente(venda.getIdOrcamentoOSOrigem().getIdVendaSeguradora().getIdClienteSeguradora());
        } else {
            nf.setIdCliente(venda.getIdCliente());
        }
        nf.setDataEmissao(venda.getDataVenda());
        nf.setDataGeracao(new Date());
        nf.setIdMunicipioPrestacao(empresa.getEndereco().getIdCidade());
        nf.setIdUfPrestacao(empresa.getEndereco().getIdCidade().getIdUF());
        nf.setSituacao(EnumSituacaoNF.RASCUNHO.getSituacao());
        nf.setTemFrete("N");

        // configuracao da NF
        nf.setTipoOperacao(EnumDestinoOperacao.OPERACAO_INTERNA.getTipo());
        nf.setTipoLocalDestinoOperacao(EnumDestinoOperacao.OPERACAO_INTERNA.getTipo());
        nf.setTipoEmissao(EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo());
        nf.setTipoFormatoImpressao(EnumFormatoImpressaoDanfe.NORMAL.getTipo());
        nf.setIdentificacaoAmbiente(EnumAmbienteEmissaoNF.HOMOLOGACAO.getTipo());
        nf.setFinalidadeEmissao(EnumFinalidadeNF.NF_NORMAL.getTipo());
        nf.setTipoOperacaoConsumidorFinal(EnumOperacaoConsumidor.CONSUMIDOR_FINAL.getTipo());

        // dados fiscais do cliente
        nf.setInscricaoEstadual(venda.getIdCliente().getInscricaoEstadual());
        nf.setIndicadorInscricaoEstadual(venda.getIdCliente().getIndicadorInscricaoEstadual());
        nf.setInscricaoMunicipal(venda.getIdCliente().getInscricaoMunicipal());

        // endereço do cliente informado
        if (venda.getIdCliente().getEndereco() != null && venda.getIdCliente().getEndereco().getIdCidade() != null) {
            nf.setIdCidadeCliente(venda.getIdCliente().getEndereco().getIdCidade());
            nf.setIdUFCliente(venda.getIdCliente().getEndereco().getIdCidade().getIdUF());
        }
        venda.setPodeVerificarEstoque(false);
        venda.setVendaProdutoList(vendaService.listarVendaProduto(venda).stream()
                .map(vp -> {
                    vp.getDadosProduto().setIdNcm(vp.getDadosProduto().getIdProduto().getIdNcm());
                    vp.getDadosProduto().setIdCfop(vp.getDadosProduto().getIdProduto().getIdCfop());
                    return vp;
                }).collect(Collectors.toList()));
        venda.setVendaServicoList(vendaService.listarVendaServico(venda));
        venda.setVendaItemOrdemDeServicoList(vendaService.listarVendaItensOrdemDeServico(venda));
        venda.setVendaFormaPagamentoList(vendaService.listarVendaFormaPagamento(venda));
        vendaService.salvar(venda);
        nf.setObjTNFe(nfeProdutoService.obterTNFe(nf, empresa, EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo()));
    }

    public void preencherNFeParaEdicao(NF nf, Compra compra) {
        preencherNFeVazia(nf);
        nf.setModeloNota(EnumModeloEmissaoNF.ENTRADA_DA_COMPRA.getModelo());
        nf.setTipoGeracao(EnumTipoGeracaoNF.SISTEMA.getTipo());
        Empresa empresa = empresaService.getEmpresPorTenatID();

        nf.setIdCompra(compra);
        nf.setIdFornecedor(compra.getIdFornecedor());
        nf.setDataEmissao(compra.getDataCompra());
        nf.setDataGeracao(new Date());
        nf.setIdMunicipioPrestacao(empresa.getEndereco().getIdCidade());
        nf.setIdUfPrestacao(empresa.getEndereco().getIdCidade().getIdUF());
        nf.setSituacao(EnumSituacaoNF.RASCUNHO_DEVOLUCAO.getSituacao());
        nf.setTemFrete("N");

        // configuracao da NF
        nf.setTipoOperacao(EnumDestinoOperacao.OPERACAO_INTERNA.getTipo());
        nf.setTipoLocalDestinoOperacao(EnumDestinoOperacao.OPERACAO_INTERNA.getTipo());
        nf.setTipoEmissao(EnumTipoEmissaoNF.CONTIGENCIA_DPEC.getTipo());
        nf.setTipoFormatoImpressao(EnumFormatoImpressaoDanfe.NORMAL.getTipo());
        nf.setIdentificacaoAmbiente(EnumAmbienteEmissaoNF.HOMOLOGACAO.getTipo());
        nf.setFinalidadeEmissao(EnumFinalidadeNF.NF_DEVOLUCAO_MERCADORIA.getTipo());
        nf.setTipoOperacaoConsumidorFinal(EnumOperacaoConsumidor.CONSUMIDOR_FINAL.getTipo());

        // dados fiscais do cliente
        nf.setInscricaoEstadual(compra.getIdFornecedor().getInscricaoEstadual());
        nf.setInscricaoMunicipal(compra.getIdFornecedor().getInscricaoMunicipal());

        // endereço do cliente informado
        if (compra.getIdFornecedor().getEndereco() != null && compra.getIdFornecedor().getEndereco().getIdCidade() != null) {
            nf.setIdCidadeCliente(compra.getIdFornecedor().getEndereco().getIdCidade());
            nf.setIdUFCliente(compra.getIdFornecedor().getEndereco().getIdCidade().getIdUF());
        }

        compraService.listarCompraProduto(compra).stream()
                .map(vp -> {
                    vp.getDadosProduto().setIdNcm(vp.getDadosProduto().getIdProduto().getIdNcm());
                    vp.getDadosProduto().setIdCfop(vp.getDadosProduto().getIdProduto().getIdCfop());
                    return vp;
                }).collect(Collectors.toList());
        nf.setObjTNFe(nfeProdutoService.obterTNFe(nf, empresa, EnumTipoEmissaoNF.CONTIGENCIA_DPEC.getTipo()));
        DetPag detPag = new DetPag();
        detPag.setVPag(0d);
        detPag.setIndPag("2");
        detPag.setTPag(EnumMeioDePagamento.SEM_PAGAMENTO.getCodigo());
        Pag pag = new Pag();
        pag.setVTroco(0d);
        pag.getDetPag().add(detPag);
        nf.getObjTNFe().getInfNFe().setPag(pag);
    }

    public List<NF> listarNFProblemaRetorno() {
        return repositorio.listarNFProblemaRetorno();
    }

}
