package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.Mensagem;
import br.com.villefortconsulting.entity.ReturnWrapper;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaNfsDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImpostosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValidacaoNFSeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.EmpresaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.NFSeMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NotaFiscalServicoFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.Assinar;
import br.com.villefortconsulting.sgfinancas.nfe.Certificado;
import br.com.villefortconsulting.sgfinancas.nfe.ConfiguracoesIniciaisNfe;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.NFSRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NFSService extends BasicService<NFS, NFSRepositorio, NotaFiscalServicoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    private NFSeMapper nfsMapper;

    @Inject
    private EmpresaMapper empresaMapper;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private EmailService emailService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContaService contaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private CtissService ctissService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new NFSRepositorio(em, adHocTenant);
    }

    public Conta preencherContaPorNFS(NFS nfs) {
        Conta conta = new Conta();
        ParametroSistema paremetro = parametroSistemaService.getParametro();

        conta.setDataVencimento(nfs.getCompetencia());
        conta.setIdCliente(nfs.getIdCliente());

        if (nfs.getIdVenda() != null) {
            conta.setIdContaBancaria(nfs.getIdVenda().getIdContaBancaria());
            conta.setInformarPagamento(nfs.getIdVenda().getCondicaoPagamento());
            conta.setNumeroParcelas(nfs.getIdVenda().getNumParcela());
            conta.setIdPlanoConta(nfs.getIdVenda().getIdPlanoConta());
        } else {
            conta.setInformarPagamento(EnumFormaPagamento.AVISTA.getForma());
            conta.setNumeroParcelas(1);
            conta.setIdPlanoConta(paremetro.getIdPlanoContaServico());
            conta.setIdContaBancaria(contaBancariaService.getContaBancaria());
        }

        conta.setObservacao(nfs.getDescricaoServico());
        conta.setQtdRepeticao(0);
        conta.setInformarPagamento("N");
        conta.setRepetirConta("N");
        conta.setValor(nfs.getValorTotal());
        conta.setValorTotal(nfs.getValorTotal());
        conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
        conta.getContaParcelaList().stream()
                .forEach(cp -> cp.setIdNFS(nfs));

        return conta;
    }

    public NFS buscarNFSPorNumeroNotaFiscal(String numeroNotaFiscal) {
        return repositorio.buscarNFSPorNumeroNotaFiscal(numeroNotaFiscal);
    }

    @Override
    public Criteria getModel(NotaFiscalServicoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);

        if (filter.getSituacoes() != null && !filter.getSituacoes().isEmpty()) {
            Criterion[] listTiposComposicao = new Criterion[filter.getSituacoes().size()];
            for (int i = 0; i < filter.getSituacoes().size(); i++) {
                listTiposComposicao[i] = Restrictions.eq("situacao", filter.getSituacoes().get(i));
            }
            criteria.add(Restrictions.or(listTiposComposicao));
        }

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            criteria.add(Restrictions.or(Restrictions.ilike("descricaoServico", filter.getDescricao(), MatchMode.ANYWHERE),
                    Restrictions.ilike("numeroNotaFiscal", filter.getDescricao(), MatchMode.ANYWHERE)));
        }
        addEqRestrictionTo(criteria, "valorTotal", filter.getValorNota());
        addIlikeRestrictionTo(criteria, "numeroNotaFiscal", filter.getNumero(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "idCliente", filter.getCliente());
        addIlikeRestrictionTo(criteria, "idCliente.email", filter.getEmail(), MatchMode.ANYWHERE);

        if (filter.getData() != null) {
            addMinMaxRestrictionTo(criteria, "dataEmissao", filter.getData());
        }

        return criteria;
    }

    public ValidacaoNFSeDTO empresaAptaEmitirNFS(String ambiente, Empresa empresaLogada) {
        ValidacaoNFSeDTO validacao = new ValidacaoNFSeDTO();
        Empresa empresa = empresaService.getEmpresPorTenatID();

        if (StringUtils.isEmpty(empresa.getInscricaoMunicipal())) {
            validacao.setInscricaoMunicipalOK(false);
        }

        if (empresa.getIdCnae() == null || !ctissService.empresaPossuiCtiss(empresa)) {
            validacao.setCtissOK(false);
        }

        ParametroSistema parametroSistema = parametroSistemaService.getParametro();

        if (parametroSistema != null) {
            validacao.setNumeroUltimoNotaOK(parametroSistema.getNumNotaFiscalServico() != null);
        } else {
            validacao.setNumeroUltimoNotaOK(false);
        }

        try {
            if ("A1".equals(empresa.getTipoCertificadoDigital())) {
                iniciaConfiguracoes(empresa, ambiente);
            } else if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                Certificado certificado = CertificadoUtil.certificadoPfx("certificados/certificadoVillefortConsulting.pfx", "Aville##2018");
                iniciaConfiguracoesA3(empresa, ambiente, certificado);
            }
        } catch (Exception ex) {
            validacao.setCertificadoOK(false);
        }

        return validacao;
    }

    public NFS salvarNotaFiscalServico(NFS nfs) {
        nfs.setSituacao(EnumSituacaoNF.RASCUNHO.getSituacao());
        nfs.setDataEmissao(DataUtil.getHoje());

        return salvar(nfs);
    }

    private Integer obterProximoNumeroRPS(NFS nfs) throws NfeException {
        if (nfs.getNumeroNotaFiscal() == null) {
            int numeroRps = repositorio.obterProximoNumeroRPS();
            if (numeroRps == 0) {
                if (parametroSistemaService.getParametro().getNumNotaFiscalServico() == null) {
                    throw new NfeException("Informe o número da última NFSe emitida fora do SG Finanças nos parâmetros do sistema.");
                }
                return parametroSistemaService.getParametro().getNumNotaFiscalServico() + 1;
            } else {
                return numeroRps;
            }
        }
        return nfs.getNumeroRPS();
    }

    public String obterXmlCancelamento(NFS nfs, String ambiente) throws NfeException {
        try {
            nfs.setNumeroRPS(obterProximoNumeroRPS(nfs));
            ReturnWrapper<String> response = doPost("cancel/generate", nfsMapper.toDTO(nfs));
            if (!response.isStatus()) {
                throw new NfeException(StringUtil.listaParaTexto(response.getMessages().stream()
                        .map(Mensagem::getMessage)
                        .collect(Collectors.toList())));
            }
            return response.getValue();
        } catch (NfeException ex) {
            throw new NfeException("Falha ao gerar o XML da NFS: " + ex.getMessage(), ex);
        }
    }

    public String assinarXmlCancelamentoNfs(Empresa empresa, String ambiente, String xml) throws NfeException, JAXBException {
        if ("A1".equals(empresa.getTipoCertificadoDigital())) {
            iniciaConfiguracoes(empresa, ambiente);
            return Assinar.assinaNfe(xml, "InfPedidoCancelamento");
        }
        throw new NfeException("Cancelamento suportado apenas para certificado A1");
    }

    public NFS cancelarNfse(Empresa empresa, String ambiente, NFS nfs, String xml) throws NfeException {
        ReturnWrapper<String> response = doPost("cancel/send/" + nfs.getId(), xml);
        if (!response.isStatus()) {
            throw new NfeException(StringUtil.listaParaTexto(response.getMessages().stream()
                    .map(Mensagem::getMessage)
                    .collect(Collectors.toList())));
        }
        // Salvar NFS com os dados da transmição da nota
        nfs.setXmlEnvio(xml);
        nfs.setSituacao(EnumSituacaoNF.PROCESSANDO_CANCELAMENTO.getSituacao());
        return salvar(nfs);
    }

    private ConfiguracoesIniciaisNfe iniciaConfiguracoes(Empresa empresa, String tipoAmbiente) throws NfeException {
        try {
            Certificado certificado = empresaService.buscarCertificadoEmpresa(empresa);
            return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(empresa.getEndereco().getIdCidade().getIdUF().getCuf(), empresa.getEndereco().getIdCidade().getCodigoIBGE(), tipoAmbiente, certificado, "", "3.10");
        } catch (Exception ex) {
            throw new NfeException("Configurações inválidas para NFS: " + ex.getMessage(), ex);
        }
    }

    private static ConfiguracoesIniciaisNfe iniciaConfiguracoesA3(Empresa empresa, String tipoAmbiente, Certificado certificado) throws NfeException {
        try {
            return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(empresa.getEndereco().getIdCidade().getIdUF().getCuf(), empresa.getEndereco().getIdCidade().getCodigoIBGE(), tipoAmbiente, certificado, "", "3.10");
        } catch (Exception ex) {
            throw new NfeException("Configurações inválidas para NFS: " + ex.getMessage(), ex);
        }
    }

    public String gerarXmlEnvioNfse(NFS nfs, String ambiente) throws NfeException {
        try {
            verificarSituacaoNfs(nfs);

            nfs.setNumeroRPS(obterProximoNumeroRPS(nfs));
            ReturnWrapper<String> response = doPost("send/generateXML", nfsMapper.toDTO(nfs));
            if (!response.isStatus()) {
                throw new NfeException(StringUtil.listaParaTexto(response.getMessages().stream()
                        .map(Mensagem::getMessage)
                        .collect(Collectors.toList())));
            }
            return response.getValue();
        } catch (NfeException ex) {
            throw new NfeException("Falha ao transmitir NFS: " + ex.getMessage(), ex);
        }
    }

    public String assinarXmlA1(Empresa empresa, String ambiente, String xml) throws NfeException {
        iniciaConfiguracoes(empresa, ambiente);
        // Assinar xml
        return Assinar.assinaNfe(Assinar.assinaNfe(xml, "InfRps"), "LoteRps");
    }

    public NFS transmitirNfse(NFS nfs, String xml) throws NfeException {
        ReturnWrapper<String> response = doPost("send/sendXML/" + nfs.getId(), xml);
        if (!response.isStatus()) {
            throw new NfeException(StringUtil.listaParaTexto(response.getMessages().stream()
                    .map(Mensagem::getMessage)
                    .collect(Collectors.toList())));
        }
        // Salvar NFS com os dados da transmição da nota
        nfs.setXmlEnvio(xml);
        nfs.setSituacao(EnumSituacaoNF.PROCESSANDO.getSituacao());
        return salvar(nfs);
    }

    private static void verificarSituacaoNfs(NFS nfs) throws NfeException {
        if (nfs.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao())) {
            throw new NfeException("A nota fiscal já foi transmitida!", null);
        }
        if (!(nfs.getSituacao().equals(EnumSituacaoNF.RASCUNHO.getSituacao()) || nfs.getSituacao().equals(EnumSituacaoNF.REJEITADA.getSituacao()))) {
            throw new NfeException("Somente notas fiscais não enviadas que podem ser transmistidas!", null);
        }
    }

    public byte[] compactarXmlsNFS(NFS nfs) throws FileException, IOException {
        Map<String, byte[]> files = new HashMap<>();

        String nomeNota = "nfse_" + nfs.getNumeroNotaFiscal() + "_";

        files.put(nomeNota + "Envio.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile("xmlEnvio", ".xml", nfs.getXmlEnvio())));

        if (StringUtils.isNotEmpty(nfs.getXmlEnvioCancelamento())) {
            files.put(nomeNota + "EnvioCancelamento.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "EnvioCancelamento", ".xml", nfs.getXmlEnvioCancelamento())));
        }

        if (StringUtils.isNotEmpty(nfs.getXmlArmazenamento())) {
            files.put(nomeNota + "Armazenamento.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "Armazenamento", ".xml", nfs.getXmlArmazenamento())));
        }

        if (StringUtils.isNotEmpty(nfs.getXmlArmazenamentoCancelamento())) {
            files.put(nomeNota + "ArmazenamentoCancelamento.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "ArmazenamentoCancelamento", ".xml", nfs.getXmlArmazenamentoCancelamento())));
        }

        if (StringUtils.isNotEmpty(nfs.getXmlRetorno())) {
            files.put(nomeNota + "Retorno.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "Retorno", ".xml", nfs.getXmlRetorno())));
        }

        if (StringUtils.isNotEmpty(nfs.getXmlRetornoCancelamento())) {
            files.put(nomeNota + "RetornoCancelamento.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "RetornoCancelamento", ".xml", nfs.getXmlRetornoCancelamento())));
        }

        return FileUtil.ziparArquivos(files);
    }

    public void enviarXmlPorEmail(NFS nfs, Empresa empresa) throws EmailException {
        try {
            EmailDTO emailDTO = EmailUtil.getEmailNotaXml(nfs);
            emailService.enviarEmailNotaFiscalServico(emailDTO, nfs, empresa, "S");
        } catch (Exception ex) {
            throw new EmailException(ex.getMessage());
        }
    }

    public NFS preencherNotaPorVenda(Venda venda) {
        NFS nfs = new NFS();

        nfs.setIdVenda(venda);
        if (venda.getIdVendaSeguradora() != null) {
            nfs.setIdCliente(venda.getIdVendaSeguradora().getIdClienteSeguradora());
            if (venda.getIdVendaSeguradora().getValorFranquia() != null) {
                nfs.setDescontoIncondicionado(venda.getIdVendaSeguradora().getValorFranquia());
            }
        } else {
            nfs.setIdCliente(venda.getIdCliente());
        }
        nfs.setValorTotal(venda.getValor());// Pegar o valor bruto
        nfs.setDescricaoServico("Serviços realizados: " + StringUtil.listaParaTexto(venda.getVendaServicoList().stream()
                .map(VendaServico::getIdServico)
                .map(Servico::getDescricao)
                .collect(Collectors.toList())));

        return preencherNfsPorCliente(nfs);
    }

    public NFS preencherNotaPorParcela(ContaParcela parcela) {
        NFS nfs = new NFS();

        nfs.setIdCliente(parcela.getIdConta().getIdCliente());
        nfs.setValorTotal(parcela.getValor());
        parcela.setIdNFS(nfs);

        return preencherNfsPorCliente(nfs);
    }

    public NFS preencherNfsPorCliente(NFS nfs) {
        Cliente cliente = nfs.getIdCliente();
        if (cliente != null) {
            // Calculo de retencoes advindas do cliente
            ImpostosDTO impostos = clienteService.calcularImpostosPorCliente(nfs.getValorTotal(), cliente);
            nfs.setValorCofins(impostos.getCofins());
            nfs.setValorCsll(impostos.getCsll());
            nfs.setValorInss(impostos.getInss());
            nfs.setValorIr(impostos.getIr());
            nfs.setValorIss(impostos.getIss());
            nfs.setValorPis(impostos.getPis());

            // Identificacao e endereco
            nfs.setTipoPessoaCliente(cliente.getTipo());
            nfs.setNomeCliente(cliente.getNome());
            nfs.setCpfCnpjCliente(cliente.getCpfCNPJ());
            nfs.setEmailCliente(cliente.getEmail());
            nfs.setCepCliente(cliente.getEndereco().getCep());
            nfs.setTelefoneCliente(cliente.getTelefoneCelular());
            nfs.setEnderecoCliente(cliente.getEndereco().getEndereco());
            nfs.setNumeroCliente(cliente.getEndereco().getNumero());
            nfs.setComplementoCliente(cliente.getEndereco().getComplemento());
            nfs.setBairroCliente(cliente.getEndereco().getBairro());
            nfs.setOptaSimples(cliente.getOptaSimples());
            if (cliente.getDescricaoNFS() == null) {
                nfs.setDescricaoServico(cliente.getDescricaoNFS());
            }

            if (cliente.getEndereco().getIdCidade() != null) {
                nfs.setIdCidadeCliente(cliente.getEndereco().getIdCidade());
                nfs.setCidadeCliente(cliente.getEndereco().getIdCidade().getDescricao());
                nfs.setIdUFCliente(cliente.getEndereco().getIdCidade().getIdUF());
                nfs.setUfCliente(cliente.getEndereco().getIdCidade().getIdUF().getDescricao());
            }

            if (nfs.getValorIss() != null && nfs.getValorIss() > 0) {
                nfs.setIssRetido("S");
            }
        } else {
            nfs.setTipoPessoaCliente(null);
            nfs.setNomeCliente(null);
            nfs.setCpfCnpjCliente(null);
            nfs.setRgCliente(null);
            nfs.setEmailCliente(null);
            nfs.setCepCliente(null);
            nfs.setTelefoneCliente(null);
            nfs.setEnderecoCliente(null);
            nfs.setNumeroCliente(null);
            nfs.setComplementoCliente(null);
            nfs.setBairroCliente(null);
            nfs.setIdCidadeCliente(null);
            nfs.setCidadeCliente(null);
            nfs.setIdUFCliente(null);
            nfs.setUfCliente(null);
            nfs.setValorCofins(null);
            nfs.setValorCsll(null);
            nfs.setValorInss(null);
            nfs.setValorIr(null);
            nfs.setValorIss(null);
            nfs.setValorPis(null);
        }

        return nfs;
    }

    public NFS preencherNotaComNotaExistente(NFS nfsNova, String numeroNotaFiscalFormatado) throws NfeException {
        if (StringUtils.isNotEmpty(numeroNotaFiscalFormatado)) {
            NFS notaFiscalExistente = buscarNFSPorNotaFiscalExistente(numeroNotaFiscalFormatado);
            nfsNova = preencherNotaFiscalSelecionada(nfsNova, notaFiscalExistente);
        } else {
            throw new NfeException("Informe o número da nota fiscal.", null);
        }
        return nfsNova;
    }

    public NFS buscarNFSPorNotaFiscalExistente(String numeroNotaFiscalFormatado) throws NfeException {
        String numeroNotaFiscal = preencherZerosNumeroNotaFiscal(numeroNotaFiscalFormatado);

        NFS notaFiscal = buscarNFSPorNumeroNotaFiscal(numeroNotaFiscal);

        if (notaFiscal == null) {
            throw new NfeException("Nota fiscal não encontrada.", null);
        }

        return notaFiscal;
    }

    public List<NFS> buscarNFSPorContrato(Contrato contrato) {
        return repositorio.buscarNFSPorContrato(contrato);
    }

    public String preencherZerosNumeroNotaFiscal(String numeroNotaFiscalFormatado) throws NfeException {
        String[] notaFiscalSplitted = numeroNotaFiscalFormatado.split("/");

        if (notaFiscalSplitted.length < 2) {
            throw new NfeException("Formato do número da nota fiscal inválido.", null);
        }

        final int qtdeCaracteresADireitaDoAno = 11;

        String numeroNotaFiscal = notaFiscalSplitted[0];
        return StringUtil.adicionarCaracterDireita(numeroNotaFiscal, qtdeCaracteresADireitaDoAno - notaFiscalSplitted[1].length(), "0")
                + notaFiscalSplitted[1];
    }

    public NFS preencherNotaFiscalSelecionada(NFS nfsNova, NFS notaFiscalExistente) {
        nfsNova.setBairroCliente(notaFiscalExistente.getBairroCliente());
        nfsNova.setCepCliente(notaFiscalExistente.getCepCliente());
        nfsNova.setCidadeCliente(notaFiscalExistente.getCidadeCliente());
        nfsNova.setCompetencia(notaFiscalExistente.getCompetencia());
        nfsNova.setComplementoCliente(notaFiscalExistente.getComplementoCliente());
        nfsNova.setCpfCnpjCliente(notaFiscalExistente.getCpfCnpjCliente());
        nfsNova.setCpfCnpjIntermediario(notaFiscalExistente.getCpfCnpjIntermediario());
        nfsNova.setDescontoCondicionado(notaFiscalExistente.getDescontoCondicionado());
        nfsNova.setDescontoIncondicionado(notaFiscalExistente.getDescontoIncondicionado());
        nfsNova.setDescricaoServico(notaFiscalExistente.getDescricaoServico());
        nfsNova.setEmailCliente(notaFiscalExistente.getEmailCliente());
        nfsNova.setEnderecoCliente(notaFiscalExistente.getEnderecoCliente());
        nfsNova.setIdCidadeCliente(notaFiscalExistente.getIdCidadeCliente());
        nfsNova.setIdCliente(notaFiscalExistente.getIdCliente());
        nfsNova.setContaParcelaList(notaFiscalExistente.getContaParcelaList());
        nfsNova.setIdCtiss(notaFiscalExistente.getIdCtiss());
        nfsNova.setIdMunicipioISSQN(notaFiscalExistente.getIdMunicipioISSQN());
        nfsNova.setIdUFCliente(notaFiscalExistente.getIdUFCliente());
        nfsNova.setIdVenda(notaFiscalExistente.getIdVenda());
        nfsNova.setIncentivadorCultural(notaFiscalExistente.getIncentivadorCultural());
        nfsNova.setInscricaoMunicipalCliente(notaFiscalExistente.getInscricaoMunicipalCliente());
        nfsNova.setInscricaoMunicipalIntermediario(notaFiscalExistente.getInscricaoMunicipalIntermediario());
        nfsNova.setIssRetido(notaFiscalExistente.getIssRetido());
        nfsNova.setNaturezaOperacao(notaFiscalExistente.getNaturezaOperacao());
        nfsNova.setNomeCliente(notaFiscalExistente.getNomeCliente());
        nfsNova.setNomeIntermediario(notaFiscalExistente.getNomeIntermediario());
        nfsNova.setNumeroArt(notaFiscalExistente.getNumeroArt());
        nfsNova.setNumeroCei(notaFiscalExistente.getNumeroCei());
        nfsNova.setNumeroCliente(notaFiscalExistente.getNumeroCliente());
        nfsNova.setNumeroRPS(notaFiscalExistente.getNumeroRPS());
        nfsNova.setOptaSimples(notaFiscalExistente.getOptaSimples());
        nfsNova.setOutrasRetencoes(notaFiscalExistente.getOutrasRetencoes());
        nfsNova.setRegimeTributacao(notaFiscalExistente.getRegimeTributacao());
        nfsNova.setRgCliente(notaFiscalExistente.getRgCliente());
        nfsNova.setSerie(notaFiscalExistente.getSerie());
        nfsNova.setTelefoneCliente(notaFiscalExistente.getTelefoneCliente());
        nfsNova.setTipoPessoaCliente(notaFiscalExistente.getTipoPessoaCliente());
        nfsNova.setTipoPessoaIntermediario(notaFiscalExistente.getTipoPessoaIntermediario());
        nfsNova.setUfCliente(notaFiscalExistente.getUfCliente());
        nfsNova.setValorCofins(notaFiscalExistente.getValorCofins());
        nfsNova.setValorCsll(notaFiscalExistente.getValorCsll());
        nfsNova.setValorDeducoes(notaFiscalExistente.getValorDeducoes());
        nfsNova.setValorInss(notaFiscalExistente.getValorInss());
        nfsNova.setValorIr(notaFiscalExistente.getValorIr());
        nfsNova.setValorIss(notaFiscalExistente.getValorIss());
        nfsNova.setValorIssRetido(notaFiscalExistente.getValorIssRetido());
        nfsNova.setValorPis(notaFiscalExistente.getValorPis());

        return nfsNova;
    }

    public StreamedContent downloadXML(NFS nfs) {
        if (nfs != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(nfs.getXmlRetorno().getBytes(StandardCharsets.UTF_8)), "application/xml", "XML "
                    + nfs.getNumeroNotaFiscal() + " - " + DataUtil.dateToString(nfs.getDataEmissao()) + ".xml", "UTF-8");
        }
        return null;
    }

    public void atualizaMicrosservico() {
        try {
            EmpresaNfsDTO dto = empresaMapper.toNfsDTO(empresaService.getEmpresa());
            ParametroSistema parametro = parametroSistemaService.getParametro();
            boolean cadastro = parametro.getTokenNFS() == null;
            dto.setAmbiente(parametro.getEnvioNotaServico());
            ReturnWrapper<Map<String, Object>> retorno = doPost("empresa/" + (cadastro ? "signup" : "update"), dto);
            if (cadastro) {
                parametro.setTokenNFS((String) retorno.getValue().get("token"));
                parametroSistemaService.salvar(parametro);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private <T> ReturnWrapper<T> doPost(String url, Object body) {
        return doPost(url, body, new HashMap<>());
    }

    private <T> ReturnWrapper<T> doPost(String url, Object body, Map<String, String> headers) {
        ParametroSistema parametro = parametroSistemaService.getParametro();
        if (parametro.getTokenNFS() != null) {
            headers.put("Authorization", "Bearer " + parametro.getTokenNFS());
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.NFSE, url, body, headers).getBody(), ReturnWrapper.class);
        } catch (IOException ex) {
            Logger.getLogger(NFSService.class.getName()).log(Level.SEVERE, null, ex);
            return new ReturnWrapper<>(ex);
        }
    }

}
