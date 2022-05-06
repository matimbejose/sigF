package br.com.villefortconsulting.sgfinancas.servicos.nfe;

import br.com.swconsultoria.nfe.dom.enuns.EventosEnum;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEvento.InfEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEvento.InfEvento.DetEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TProcEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUfEmi;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TRetInutNFe;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import br.com.swconsultoria.nfe.util.ConstantesUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFCorrecao;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.InutilizacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValidacaoNFeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.NFeMapper;
import br.com.villefortconsulting.sgfinancas.nfe.Assinar;
import br.com.villefortconsulting.sgfinancas.nfe.Certificado;
import br.com.villefortconsulting.sgfinancas.nfe.ConfiguracoesIniciaisNfe;
import br.com.villefortconsulting.sgfinancas.nfe.EnumNFe;
import br.com.villefortconsulting.sgfinancas.nfe.Enviar;
import br.com.villefortconsulting.sgfinancas.nfe.Evento;
import br.com.villefortconsulting.sgfinancas.nfe.Nfe;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.Filler;
import br.com.villefortconsulting.sgfinancas.nfe.util.Validator;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.servicos.CompraService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.NFService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.EnumTpEmis;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumDestinoOperacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoEntradaSaida;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoProcessamento;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Emit;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Total;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Total.ICMSTot;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import freemarker.template.utility.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Christopher
 *
 * Link para extrair certificado http://williansco.blogspot.com.br/2015/06/certificado-digital-opcao-de-exportar.html
 *
 * Link para validar o xml gerado https://www.sefaz.rs.gov.br/nfe/nfe-val.aspx
 *
 * http://www.javac.com.br/jc/busca.javac?cx=011746815393091855791%3Avhycmqflg6s&cof=FORID%3A10&ie=ISO-8859-1&q=indIEDest
 *
 *
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NfeProdutoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    NFeMapper nfeMapper;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private VendaService vendaService;

    @EJB
    private NFService nfService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private CompraService compraService;

    private static final String URL_MS_NFE = "nfe/v4/";

    private static final String PREFIXO_SITUACAO = "Situação do ambiente de ";

    private Double valorTotalPIS;

    private Double valorTotalCOFINS;

    private Double valorTotalVBc;

    private Double valorTotalIcms;

    private Double valorTotalIcmsSt;

    public ValidacaoNFeDTO empresaAptaEmitirNF(String ambiente) {
        ValidacaoNFeDTO validacaoNFeDTO = new ValidacaoNFeDTO();
        Empresa empresa = empresaService.getEmpresPorTenatID();
        // verifica se as informações da empresa estao devidamente preenchidas para
        // emissão da NF
        if (StringUtils.isEmpty(empresa.getCnpj())) {
            validacaoNFeDTO.setCnpjOK(false);
        }
        // vrifica se o endereço da empresa está informado
        if (empresa.getEndereco() == null) {
            validacaoNFeDTO.setEnderecoOK(false);
        } else {
            if (empresa.getEndereco().getIdCidade() == null) {
                validacaoNFeDTO.setEnderecoOK(false);
            }
            if (empresa.getEndereco().getEndereco() == null) {
                validacaoNFeDTO.setEnderecoOK(false);
            }
            if (empresa.getEndereco().getBairro() == null) {
                validacaoNFeDTO.setEnderecoOK(false);
            }
            if (StringUtils.isEmpty(empresa.getEndereco().getNumero())) {
                validacaoNFeDTO.setEnderecoOK(false);
            }
        }
        if (StringUtils.isEmpty(empresa.getInscricaoEstadual())) {
            validacaoNFeDTO.setInscricaoEstadualOK(false);
        }
        if (StringUtils.isEmpty(empresa.getIndicadorInscricaoEstadual())) {
            validacaoNFeDTO.setIndicadorInscricaoEstadualOK(false);
        }
        if (StringUtils.isEmpty(empresa.getInscricaoMunicipal())) {
            validacaoNFeDTO.setInscricaoMunicipalOK(false);
        }
        if (StringUtils.isEmpty(empresa.getPorte())) {
            validacaoNFeDTO.setPorteEmpresaOK(false);
        }
        if (StringUtils.isEmpty(empresa.getRegimeTributario())) {
            validacaoNFeDTO.setRegimeTributarioOK(false);
        }
        ParametroSistema parametroSistema = parametroSistemaService.getParametro();
        if (parametroSistema != null) {
            validacaoNFeDTO.setNumeroNFeOK(parametroSistema.getNumNotaFiscal() != null);
            validacaoNFeDTO.setSerieNFeOK(parametroSistema.getSerieEntrada() != null && parametroSistema.getSerieSaida() != null);
        } else {
            validacaoNFeDTO.setNumeroNFeOK(false);
            validacaoNFeDTO.setSerieNFeOK(false);
        }
        try {
            if (null == empresa.getTipoCertificadoDigital()) {
                validacaoNFeDTO.setCertificadoOK(false);
            } else {
                switch (empresa.getTipoCertificadoDigital()) {
                    case "A1":
                        iniciaConfiguracoes(empresa, ambiente);
                        break;
                    case "A3":
                        Certificado certificado = CertificadoUtil.certificadoPfx("certificados/certificadoVillefortConsulting.pfx", "Aville##2018");
                        iniciaConfiguracoesA3(empresa, ambiente, certificado);
                        break;
                    default:
                        validacaoNFeDTO.setCertificadoOK(false);
                        break;
                }
            }
        } catch (NfeException ex) {
            validacaoNFeDTO.setCertificadoOK(false);
        }
        return validacaoNFeDTO;
    }

    public String consulta() {
        boolean isProducao = parametroSistemaService.getParametro().getEnvioNotaProduto().equals("1");
        String descricaoAmbiente;
        JSONObject request = new JSONObject();
        Empresa empresa = empresaService.getEmpresa();
        request.put("estado", empresa.getEndereco().getIdCidade().getIdUF().getDescricao());
        if (isProducao) {
            descricaoAmbiente = EnumNFe.PRODUCAO.getNome();
            request.put("ambiente", 1);
        } else {
            descricaoAmbiente = EnumNFe.HOMOLOGACAO.getNome();
            request.put("ambiente", 2);
        }
        ResponseEntity<String> status = null;
        try {
            status = doPost("service/status", request);

            ObjectMapper mapper = new ObjectMapper();
            TRetConsStatServ response = mapper.readValue(status.getBody(), TRetConsStatServ.class);
            if (isOk(status) && "107".equals(response.getCStat())) {
                descricaoAmbiente = PREFIXO_SITUACAO + descricaoAmbiente + ": Operacional.";
            } else {
                descricaoAmbiente = PREFIXO_SITUACAO + descricaoAmbiente + ": Sem comunicação. \n";
                descricaoAmbiente += "Stat: " + response.getCStat() + " \n";
                descricaoAmbiente += "Motivo: " + response.getXMotivo();
            }
        } catch (UnrecognizedPropertyException ex) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(status.getBody());
                String errMsg = (String) json.getOrDefault("message", "");
                if (errMsg.isEmpty()) {
                    return PREFIXO_SITUACAO + descricaoAmbiente + ": Sem comunicação. \n";
                } else {
                    return PREFIXO_SITUACAO + descricaoAmbiente + ": Erro (" + errMsg + "). \n";
                }
            } catch (NullPointerException | ParseException e) {
                return PREFIXO_SITUACAO + descricaoAmbiente + ": Retorno não identificado.";
            }
        } catch (HttpServerErrorException ex) {
            return PREFIXO_SITUACAO + descricaoAmbiente + ": Sem comunicação.\nCódigo: " + ex.getStatusCode();
        } catch (ResourceAccessException | ConnectException ex) {
            return PREFIXO_SITUACAO + descricaoAmbiente + ": Sem comunicação (MS).";
        } catch (IOException ex) {
            return PREFIXO_SITUACAO + descricaoAmbiente + ": Sem comunicação.";
        } catch (Exception ex) {
            return PREFIXO_SITUACAO + descricaoAmbiente + ": Erro não identificado.";
        }
        return descricaoAmbiente;
    }

    public byte[] compactarXmlsNFS(NF nf) throws FileException, IOException {
        Map<String, byte[]> files = new HashMap<>();

        String nomeNota = "nfse_" + nf.getNumeroNotaFiscal() + "_";

        files.put(nomeNota + "Envio.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile("xmlEnvio", ".xml", nf.getXmlEnvio())));
        if (StringUtils.isNotEmpty(nf.getXmlEnvioCancelamento())) {
            files.put(nomeNota + "EnvioCancelamento.xml",
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "EnvioCancelamento", ".xml", nf.getXmlEnvioCancelamento())));
        }
        if (StringUtils.isNotEmpty(nf.getXmlArmazenamento())) {
            files.put(nomeNota + "Armazenamento.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "Armazenamento", ".xml", nf.getXmlArmazenamento())));
        }
        if (StringUtils.isNotEmpty(nf.getXmlArmazenamentoCancelamento())) {
            files.put(nomeNota + "ArmazenamentoCancelamento.xml",
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "ArmazenamentoCancelamento", ".xml", nf.getXmlArmazenamentoCancelamento())));
        }
        if (StringUtils.isNotEmpty(nf.getXmlRetorno())) {
            files.put(nomeNota + "Retorno.xml", FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "Retorno", ".xml", nf.getXmlRetorno())));
        }
        if (StringUtils.isNotEmpty(nf.getXmlRetornoCancelamento())) {
            files.put(nomeNota + "RetornoCancelamento.xml",
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile(nomeNota + "RetornoCancelamento", ".xml", nf.getXmlRetornoCancelamento())));
        }
        return FileUtil.ziparArquivos(files);
    }

    public String obterXmlNfe(NF nf, String ambiente) throws NfeException {
        try {
            nfService.loadXMLWithEntity(nf);
            NFe objNFe = nf.getObjTNFe();
            Validator.removerCamposVazios(objNFe);
            Validator.removerMascara(objNFe);
            Filler.preencherImposto(nf, empresaService.getEmpresa());
            Validator.validarImpostos(objNFe);
            Filler.preencherRestante(nf);
            TEnviNFe enviNFe = preencherNfEnvio(nf, nfeMapper.toHis(objNFe));// obterTEnviNFe(nf, ambiente);
            Empresa empresa = empresaService.getEmpresPorTenatID();
            if (nf.getIdCompra() != null) {
                enviNFe.getNFe().stream().forEach(nfe -> {
                    TNFe.InfNFe.Ide ide = nfe.getInfNFe().getIde();

                    TNFe.InfNFe.Ide.NFref nfref = new TNFe.InfNFe.Ide.NFref();
                    nfref.setRefNFe(nf.getIdCompra().getNReferencia());
                    ide.getNFref().add(nfref);
                    nfe.getInfNFe().setId("NFe" + Filler.gerarChaveNF(empresa, nf, EnumTpEmis.CONTINGENCIA_EPEC.getTipo()));
                    nf.setChave(nfe.getInfNFe().getId());
                });
            }
            return Nfe.obterXmlNfe(enviNFe);
        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public String assinarXmlNfe(String xml) throws NfeException {
        return Enviar.assinarXmlNfe(xml, false);
    }

    public String obterXmlNfeCancelamento(NF nf, String ambiente) throws NfeException {
        Empresa empresa = empresaService.getEmpresPorTenatID();

        // Troque o X pela Chave
        String chave = nf.getChave();

        // Troque o Y pelo Protocolo
        String prot = nf.getProtocolo();
        String id = "ID110111" + chave + "01";

        TEnvEvento enviEvento = new TEnvEvento();
        enviEvento.setVersao("1.00");
        enviEvento.setIdLote(nf.getId().toString());

        TEvento eventoCancela = new TEvento();
        eventoCancela.setVersao("1.00");

        InfEvento infoEvento = new InfEvento();
        infoEvento.setId(id);
        infoEvento.setChNFe(chave);
        infoEvento.setCOrgao(nf.getIdCidadeCliente().getIdUF().getCuf());
        infoEvento.setTpAmb(ambiente);

        // Troque o X pela CNPJ
        infoEvento.setCNPJ(empresa.getCnpj().replace(".", "").replace("/", "").replace("-", ""));

        // Altere a Data
        infoEvento.setDhEvento(DataUtil.retornaDataNFe(new Date()));
        infoEvento.setTpEvento("110111"); // fixo
        infoEvento.setNSeqEvento("1");
        infoEvento.setVerEvento("1.00");

        DetEvento detEvento = new DetEvento();
        detEvento.setVersao("1.00");
        detEvento.setDescEvento("Cancelamento");
        // código não suportado na nova versão da lib de nfe -- detEvento.setNProt(prot)
        detEvento.setXJust("Erro na emissão");
        infoEvento.setDetEvento(detEvento);
        eventoCancela.setInfEvento(infoEvento);
        enviEvento.getEvento().add(eventoCancela);

        return XmlUtil.removeAcentos(Evento.obterXmlCancelamento(enviEvento));
    }

    public String assinarXmlNfeCancelamento(String xml) throws NfeException {
        return Assinar.assinaNfe(xml, Assinar.EVENTO);
    }

    public void cancelarNFProduto(NF nf) throws NfeException {
        try {
            ResponseEntity<String> status = doPost("service/cancellation", nf.getXmlEnvioCancelamento());
            ObjectMapper mapper = new ObjectMapper();
            TRetEnvEvento retorno = mapper.readValue(status.getBody(), TRetEnvEvento.class);
            if (retorno.getRetEvento().get(0).getInfEvento().getCStat().equals("135")) {// Cancelamento dentro do prazo
                TEvento eventoCancela = new TEvento();
                eventoCancela.setVersao("1.00");

                // Cria TProcEventoNFe
                TProcEvento procEvento = new TProcEvento();
                procEvento.setVersao("1.00");
                procEvento.setEvento(eventoCancela);
                procEvento.setRetEvento(retorno.getRetEvento().get(0));

                // nota cancelada com sucesso.
                nf.setSituacao(EnumSituacaoNF.CANCELADA.getSituacao());
                nf.setXmlArmazenamentoCancelamento(XmlUtil.objectToXml(procEvento));
            } else {
                nf.setMensagemErroEnvio(retorno.getRetEvento().get(0).getInfEvento().getCStat() + ": " + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
            }
            nfService.alterar(nf);
        } catch (UnrecognizedPropertyException | JAXBException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new NfeException("Ocorreu um erro ao processar a resposta da SEFAZ", ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new NfeException("Ocorreu um erro na comunicação com a SEFAZ", ex);
        } catch (NfeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new NfeException("Ocorreu um erro no cancelamento", ex);
        }
    }

    public NF transmitirNFe(NF nf, String ambiente, String xml, String tipo) throws JAXBException {
        nf.setXmlEnvio(xml);
        TEnviNFe enviNFe = XmlUtil.xmlToObject(xml, TEnviNFe.class);
        String url = "synchronous";
        if (tipo.equals("NFCe")) {
            url = "nfce";
        }
        try {
            if (StringUtils.isEmpty(nf.getXmlEnvio())) {
                nf.setXmlEnvio(Nfe.getXmlEnvio());
            }
            ResponseEntity<String> status = doPost("send/" + url, xml);
            ObjectMapper mapper = new ObjectMapper();
            if (!isOk(status)) {// Repassa a Exception do micro serviço
                NfeException mensagem = mapper.readValue(status.getBody(), NfeException.class);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Retorno da SEFAZ: {0}", status.getBody());
                String msg = mensagem.getMessage();
                if (msg == null) {
                    msg = "Não foi possível enviar a nota para a SEFAZ. " + status.getStatusCode().value();
                }
                throw new NfeException(msg, null);
            }
            TRetEnviNFe retorno = mapper.readValue(status.getBody(), TRetEnviNFe.class);
            if (null == retorno) {
                return nf;// go to the finally block
            }
            // NF enviada com sucesso => testar se igual a 100
            if (retorno.getProtNFe() == null) {
                rejeitarNFe(nf, retorno.getXMotivo());
            } else if (!"100".equals(retorno.getProtNFe().getInfProt().getCStat())) {
                rejeitarNFe(nf, retorno.getProtNFe().getInfProt().getCStat() + ": " + retorno.getProtNFe().getInfProt().getXMotivo());
            } else {
                nf.setProtocolo(retorno.getProtNFe().getInfProt().getNProt());
                if (nf.getIdVenda() != null) {
                    nf.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
                } else if (nf.getIdCompra() != null) {
                    nf.setSituacao(EnumSituacaoNF.ENVIADA_DEVOLUCAO.getSituacao());
                } else {
                    // Situação inválida
                    nf.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
                }
                nf.setMensagemErroEnvio(null);
                nf.setDataRetornoProcessamento(DataUtil.retornaDataNFeConvertida(retorno.getProtNFe().getInfProt().getDhRecbto()));
                nf.setDigestValue(new String(Base64.getEncoder().encode(retorno.getProtNFe().getInfProt().getDigVal())));
                nf.setXmlRetorno(XmlNfeUtil.criaNfeProc(enviNFe, retorno.getProtNFe()));
                nf.setXmlArmazenamento(montaXmlArmazenamento(nf));
                nf.setChave(enviNFe.getNFe().get(0).getInfNFe().getId().replace("NFe", ""));
                // atualiza a chave sem o NFe
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            if (null == e.getMessage()) {
                rejeitarNFe(nf, "Não foi possível enviar a nota.");// erro, NF não enviada
            } else {
                rejeitarNFe(nf, e.getMessage());// erro, NF não enviada
            }
        } finally {
            nf = nfService.alterar(nf);
            // Atualiza a nf de acordo com o parametros informados
            TNFe.InfNFe nfe = enviNFe.getNFe().get(0).getInfNFe();
            nf.setTipoOperacaoConsumidorFinal(nfe.getIde().getIndFinal());
            nf.setIndicadorInscricaoEstadual(nfe.getDest().getIndIEDest());
            nf.setFinalidadeEmissao(nfe.getIde().getFinNFe());
            nf.setTipoEmissao(nfe.getIde().getTpEmis());
            nf.setTipoFormatoImpressao(nfe.getIde().getTpImp());
            nf.setIdentificacaoAmbiente(ambiente);
            nf.setTipoOperacao(EnumTipoEntradaSaida.SAIDA.getTipo());
            nf.setTipoLocalDestinoOperacao(EnumDestinoOperacao.OPERACAO_INTERNA.getTipo());
            if (nf.getChave() != null && nf.getChave().startsWith("NFe")) {
                nf.setChave(nf.getChave().substring(3));
            }
            nf = nfService.alterar(nf);
        }
        return nf;
    }

    private ResponseEntity<String> doPost(String url, JSONObject request) {
        boolean isProducao = parametroSistemaService.getParametro().getEnvioNotaProduto().equals("1");
        if (isProducao) {
            url = URL_MS_NFE + url + "/" + EnumNFe.PRODUCAO.getSigla();
        } else {
            url = URL_MS_NFE + url + "/" + EnumNFe.HOMOLOGACAO.getSigla();
        }
        return MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.NFE, url, request, getHttpHeaders());
    }

    private ResponseEntity<String> doPost(String url, String request) {
        boolean isProducao = parametroSistemaService.getParametro().getEnvioNotaProduto().equals("1");
        if (isProducao) {
            url = URL_MS_NFE + url + "/" + EnumNFe.PRODUCAO.getSigla();
        } else {
            url = URL_MS_NFE + url + "/" + EnumNFe.HOMOLOGACAO.getSigla();
        }
        return MicroServiceUtil.doXmlPost(MicroServiceUtil.MicroServicos.NFE, url, request, getHttpHeaders());
    }

    private Map<String, String> getHttpHeaders() {
        Empresa empresa = empresaService.getEmpresa();
        String cnpj = CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj());
        String msToken = parametroSistemaService.getParametro().getNfeMsToken();

        Map<String, String> headerList = new HashMap<>();

        headerList.put("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((cnpj + ":" + msToken).getBytes()));
        headerList.put("Accept", "application/json");
        return headerList;
    }

    private boolean isOk(ResponseEntity<String> response) {
        return response.getStatusCode() == HttpStatus.OK;
    }

    private String getCUF(Empresa empresa) throws NfeException {
        try {
            return empresa.getEndereco().getIdCidade().getIdUF().getCuf();
        } catch (NullPointerException ex) {
            throw new NfeException("Cidade não selecionada no cadastro de empresa.", ex);
        }
    }

    private String getCodIBGE(Empresa empresa) throws NfeException {
        try {
            return empresa.getEndereco().getIdCidade().getCodigoIBGE();
        } catch (NullPointerException ex) {
            throw new NfeException("Cidade não selecionada no cadastro de empresa.", ex);
        }
    }

    public ConfiguracoesIniciaisNfe iniciaConfiguracoes(Empresa empresa, String tipoAmbiente) throws NfeException {
        // Certificado Arquivo, Parametros: -Caminho Certificado, - Senha
        Certificado certificado = empresaService.buscarCertificadoEmpresa(empresa);

        return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(getCUF(empresa), tipoAmbiente, certificado, "", "4.00");
    }

    private ConfiguracoesIniciaisNfe iniciaConfiguracoesA3(Empresa empresa, String tipoAmbiente, Certificado certificado) throws NfeException {
        try {
            return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(getCUF(empresa), getCodIBGE(empresa), tipoAmbiente, certificado, "", "4.00");
        } catch (Exception ex) {
            throw new NfeException("Configurações inválidas para NFS: " + ex.getMessage(), ex);
        }
    }

    private ConfiguracoesIniciaisNfe iniciaConfiguracoesA3(Empresa empresa, String tipoAmbiente) throws NfeException {
        try {
            return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(empresa.getEndereco().getIdCidade().getIdUF().getCuf(), empresa.getEndereco().getIdCidade().getCodigoIBGE(), tipoAmbiente, null, "", "4.00");
        } catch (Exception ex) {
            throw new NfeException("Configurações inválidas para NFS: " + ex.getMessage(), ex);
        }
    }

    // http://www.javac.com.br/jc/posts/list/355.page
    // http://www.javac.com.br/jc/posts/list/355-nfe-gerando-o-xml-nfeproc-leiaute-da-distribuicao-da-nfe.page
    private String montaXmlArmazenamento(NF nf) {
        try {
            String retorno = null;
            String xmlEnviNFe = nf.getXmlEnvio();
            String xmlRetConsReciNFe = nf.getXmlRetorno();
            Document document = documentFactory(xmlEnviNFe);
            NodeList nodeListNfe = document.getDocumentElement().getElementsByTagName("NFe");
            NodeList nodeListInfNfe = document.getElementsByTagName("infNFe");
            for (int i = 0; i < nodeListNfe.getLength(); i++) {
                Element el = (Element) nodeListInfNfe.item(i);
                String chaveNFe = el.getAttribute("Id");

                String xmlNFe = outputXML(nodeListNfe.item(i));
                String xmlProtNFe = getProtNFe(xmlRetConsReciNFe, chaveNFe);

                retorno = buildNFeProc(xmlNFe, xmlProtNFe);
            }
            return retorno;
        } catch (TransformerException | SAXException | IOException | ParserConfigurationException ex) {
            return null;
        }
    }

    private static Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(
                StandardCharsets.UTF_8)));
    }

    private static String outputXML(Node node) throws TransformerException, UnsupportedEncodingException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(node), new StreamResult(os));
        String xml = os.toString("UTF-8");
        if ((xml != null) && (!"".equals(xml))) {
            xml = xml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
        }
        return xml;
    }

    private static String getProtNFe(String xml, String chaveNFe) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        Document document = documentFactory(xml);
        NodeList nodeListProtNFe = document.getDocumentElement().getElementsByTagName("protNFe");
        NodeList nodeListChNFe = document.getElementsByTagName("chNFe");
        for (int i = 0; i < nodeListProtNFe.getLength(); i++) {
            Element el = (Element) nodeListChNFe.item(i);
            String chaveProtNFe = el.getTextContent();
            if (chaveNFe.contains(chaveProtNFe)) {
                return outputXML(nodeListProtNFe.item(i));
            }
        }
        return "";
    }

    private static String buildNFeProc(String xmlNFe, String xmlProtNFe) {
        StringBuilder nfeProc = new StringBuilder();
        nfeProc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<nfeProc versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">").append(xmlNFe)
                .append(xmlProtNFe).append("</nfeProc>");
        return nfeProc.toString();
    }

    private void rejeitarNFe(NF nf, String erro) {
        if (nf.getSituacao().equals(EnumSituacaoNF.RASCUNHO.getSituacao()) || nf.getSituacao().equals(EnumSituacaoNF.REJEITADA.getSituacao())) {
            nf.setSituacao(EnumSituacaoNF.REJEITADA.getSituacao());
        } else {
            nf.setSituacao(EnumSituacaoNF.REJEITADA_DEVOLUCAO.getSituacao());
        }
        if (erro.contains("shutdown") || erro.contains("reset")) {
            nf.setMensagemErroEnvio("Erro ao transmitir a nota fiscal: Sem comunicação com a Sefaz.");
        } else {
            nf.setMensagemErroEnvio(erro);
        }
    }

    public NFe obterTNFe(NF nf, Empresa empresa, String tipo) {
        NFe.InfNFe infNFe = nf.getObjTNFe().getInfNFe();
        if (infNFe == null) {
            infNFe = new InfNFe();
        }
        infNFe.setVersao("4.00");

        valorTotalPIS = valorTotalCOFINS = valorTotalVBc = valorTotalIcmsSt = 0d;
        infNFe.setIde(Filler.preencherIde(nf, empresa, tipo, "0.01"));
        infNFe.setEmit(preencherEmitente(empresa));
        // infNFe.setDest(Filler.preencherDest(nf.getIdCliente(), nf.getIdFornecedor(), config));
        preencherProdutos(nf, empresa);
        infNFe.setCobr(Filler.preencherCobr());
        infNFe.setPag(Filler.preencherPagamento(nf, EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo()));
        nf.setValorNota(infNFe.getDet().stream().mapToDouble(item -> item.getProd().getVProd()).sum());
        infNFe.setTotal(preencherTotais(nf, empresa));
        infNFe.setTransp(Filler.preencherTranp(nf, empresa));
        infNFe.setInfAdic(Filler.preencherInfAdic(nf));
        infNFe.setId(Filler.gerarChaveNF(empresa, nf, tipo));

        NFe nfe = new NFe();
        nfe.setInfNFe(infNFe);

        return nfe;
    }

    private List<Det> preencherProdutos(NF nf, Empresa empresa) {
        List<VendaProduto> produtosVenda = new ArrayList<>();
        List<CompraProduto> produtosCompra = new ArrayList<>();
        if (nf.getIdVenda() != null) {
            produtosVenda.addAll(vendaService.listarVendaProduto(nf.getIdVenda()));
        }
        if (nf.getIdCompra() != null) {
            produtosCompra.addAll(compraService.listarCompraProduto(nf.getIdCompra()));
        }
        return Filler.preencherDet(nf, produtosVenda, produtosCompra, empresa);
    }

    private Total preencherTotais(NF nf, Empresa empresa) {
        Total total = new Total();

        ICMSTot icmstot = new ICMSTot();

        // if (nf.getValorIpiDevolucao() == null) {
        // nf.setValorIpiDevolucao(0d);
        // }
        icmstot.setVBC(NumeroUtil.somar(valorTotalVBc));
        icmstot.setVICMS(NumeroUtil.somar(valorTotalIcms));
        icmstot.setVICMSDeson(0d);
        icmstot.setVBCST(0d);
        icmstot.setVST(NumeroUtil.somar(valorTotalIcmsSt));

        // alter: valor todos dos produtos
        icmstot.setVProd(nf.getValorNota());

        icmstot.setVFrete(0d);
        icmstot.setVSeg(0d);
        icmstot.setVDesc(0d);

        icmstot.setVII(0d);
        icmstot.setVIPI(0d);
        icmstot.setVOutro(0d);
        // icmstot.setVNF(NumeroUtil.somar(nf.getValorNota(),
        // nf.getValorIpiDevolucao()));

        icmstot.setVFCPUFDest(0d);
        icmstot.setVICMSUFDest(0d);
        icmstot.setVICMSUFRemet(0d);
        icmstot.setVFCP(0d);
        icmstot.setVFCPST(0d);
        icmstot.setVFCPSTRet(0d);
        // icmstot.setVIPIDevol(nf.getValorIpiDevolucao());
        if (empresa.getRegimeTributario().equals(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma())) {
            icmstot.setVPIS(0d);
            icmstot.setVCOFINS(0d);
        } else {
            icmstot.setVPIS(NumeroUtil.somar(valorTotalPIS));
            icmstot.setVCOFINS(NumeroUtil.somar(valorTotalCOFINS));
        }
        total.setICMSTot(icmstot);

        return total;
    }

    private TEnviNFe preencherNfEnvio(NF nf, TNFe nfe) {
        TEnviNFe enviNFe = new TEnviNFe();
        enviNFe.setVersao("4.00");
        enviNFe.setIdLote(nf.getId() + ""); // inicialmente cada nf sera enviada em um unico lote
        enviNFe.setIndSinc(EnumTipoProcessamento.SINCRONO.getTipo()); // 1=Empresa solicita processamento síncrono do Lote de NF-e (sem a geração de
        // Recibo para consulta futura)
        enviNFe.getNFe().add(nfe);

        return enviNFe;
    }

    private Emit preencherEmitente(Empresa empresa) {
        Emit emit = new Emit();
        emit.setCNPJ(empresa.getCnpj().replace(".", "").replace("/", "").replace("-", ""));

        emit.setXNome(empresa.getDescricao());
        emit.setXFant(empresa.getNomeFantasia());
        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(empresa.getEndereco().getEndereco());
        enderEmit.setNro(empresa.getEndereco().getNumero());
        if (!StringUtils.isEmpty(empresa.getEndereco().getComplemento())) {
            enderEmit.setXCpl(empresa.getEndereco().getComplemento());
        }
        enderEmit.setXBairro(empresa.getEndereco().getBairro());
        enderEmit.setCMun(empresa.getEndereco().getIdCidade().getCodigoIBGE());
        enderEmit.setXMun(empresa.getEndereco().getIdCidade().getDescricao());
        enderEmit.setUF(TUfEmi.valueOf(empresa.getEndereco().getIdCidade().getIdUF().getDescricao()));
        enderEmit.setCEP(empresa.getEndereco().getCep().replace(".", "").replace("-", ""));
        enderEmit.setCPais("1058");
        enderEmit.setXPais("Brasil");
        emit.setEnderEmit(enderEmit);

        emit.setIE(empresa.getInscricaoEstadual());

        // rever isso
        // identifica o tipo de regime da empresa
        if (empresa.getRegimeTributario().equals(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma())) { // simples nacional
            // Código de Regime Tributário
            emit.setCRT("1");// 1=Simples Nacional; 2=Simples Nacional, excesso sublimite de receita bruta;
            // 3=Regime Normal. (v2.0).
        } else {
            emit.setCRT("3");
        }
        return emit;
    }

    public String getDigitoVerificadorDaChave(String chave) {
        return chave.substring(chave.length() - 1);
    }

    public void geraXMLCCe(NFCorrecao nfCorrecao, Empresa empresa) throws NfeException, JAXBException {
        br.com.swconsultoria.nfe.schema.envcce.TEvento tEnv = new br.com.swconsultoria.nfe.schema.envcce.TEvento();
        br.com.swconsultoria.nfe.schema.envcce.TEvento.InfEvento infEvento = new br.com.swconsultoria.nfe.schema.envcce.TEvento.InfEvento();
        br.com.swconsultoria.nfe.schema.envcce.TEvento.InfEvento.DetEvento detEvento = new br.com.swconsultoria.nfe.schema.envcce.TEvento.InfEvento.DetEvento();

        detEvento.setDescEvento("Carta de Correcao");
        detEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);
        detEvento.setXCondUso("A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");
        detEvento.setXCorrecao(nfCorrecao.getDescricaoCorrecao());

        String cpfCnpj = empresaService.getEmpresa().getCnpj().replaceAll("(\\D)", "");
        String chave = nfCorrecao.getIdNf().getChave();

        if (cpfCnpj.length() == 11) {
            infEvento.setCPF(cpfCnpj);
        } else {
            infEvento.setCNPJ(cpfCnpj);
        }

        infEvento.setId("ID" + EventosEnum.CCE + chave + StringUtil.leftPad(nfCorrecao.getNroSequencia() + "", 2, "0"));
        infEvento.setCOrgao(empresa.getEndereco().getIdCidade().getIdUF().getCuf());// Código do estado
        infEvento.setChNFe(chave);
        infEvento.setDhEvento(dataNfe());
        infEvento.setDetEvento(detEvento);
        infEvento.setNSeqEvento(nfCorrecao.getNroSequencia() + "");
        infEvento.setTpAmb(parametroSistemaService.getAmbienteNotaFiscalProduto());
        infEvento.setTpEvento(EventosEnum.CCE.getCodigo());
        infEvento.setVerEvento(ConstantesUtil.VERSAO.EVENTO_CCE);

        tEnv.setInfEvento(infEvento);
        tEnv.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);

        br.com.swconsultoria.nfe.schema.envcce.TEnvEvento tEnvEvento = new br.com.swconsultoria.nfe.schema.envcce.TEnvEvento();

        tEnvEvento.getEvento().add(tEnv);
        tEnvEvento.setIdLote(nfCorrecao.getIdNf().getId() + "");
        tEnvEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);

        String xmlEnvio = XmlUtil.objectToXml(tEnvEvento);
        xmlEnvio = XmlUtil.removeAcentos(xmlEnvio);
        nfCorrecao.setXmlEnvio(xmlEnvio);
    }

    public void enviarCCe(NFCorrecao nfCorrecao, Empresa empresa) throws NfeException, IOException, JAXBException, NfeException {
        if (nfCorrecao.getXmlEnvio() == null) {
            geraXMLCCe(nfCorrecao, empresa);
        }
        ResponseEntity<String> status = doPost("service/electronic/correction/letter", nfCorrecao.getXmlEnvio());
        ObjectMapper mapper = new ObjectMapper();

        if (!isOk(status)) {
            throw mapper.readValue(status.getBody(), NfeException.class);
        }
        mapper.readValue(status.getBody(), br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento.class);
        nfCorrecao.setXmlRetorno(status.getBody());
    }

    public void inutilizarNumeracao(InutilizacaoDTO inutilizacaoDTO) throws NfeException {
        JSONObject request = new JSONObject();
        request.put("id", inutilizacaoDTO.getId());
        request.put("motivo", inutilizacaoDTO.getMotivo());
        try {
            ResponseEntity<String> status = doPost("service/disablement", request);

            ObjectMapper mapper = new ObjectMapper();
            TRetInutNFe retorno = mapper.readValue(status.getBody(), TRetInutNFe.class);
            if (!retorno.getInfInut().getCStat().equals("102")) {
                throw new NfeException(retorno.getInfInut().getXMotivo(), null);
            }
        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public boolean reProcessarNFe(NF nf) {
        try {
            TRetEnviNFe retorno = XmlUtil.xmlToObject(nf.getXmlRetorno(), TRetEnviNFe.class);
            TEnviNFe enviNFe = XmlUtil.xmlToObject(nf.getXmlEnvio(), TEnviNFe.class);
            if (null == retorno) {
                return false;
            }

            nf.setProtocolo(retorno.getProtNFe().getInfProt().getNProt());
            if (nf.getIdVenda() != null) {
                nf.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
            } else if (nf.getIdCompra() != null) {
                nf.setSituacao(EnumSituacaoNF.ENVIADA_DEVOLUCAO.getSituacao());
            } else {
                // Situação inválida
                nf.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
            }
            nf.setTipoGeracao("S");
            nf.setMensagemErroEnvio(null);
            nf.setDataRetornoProcessamento(DataUtil.retornaDataNFeConvertida(retorno.getProtNFe().getInfProt().getDhRecbto()));
            nf.setDigestValue(new String(Base64.getEncoder().encode(retorno.getProtNFe().getInfProt().getDigVal())));
            nf.setXmlRetorno(XmlNfeUtil.criaNfeProc(enviNFe, retorno.getProtNFe()));
            nf.setXmlArmazenamento(montaXmlArmazenamento(nf));
            nf.setChave(enviNFe.getNFe().get(0).getInfNFe().getId().replace("NFe", ""));

            nfService.salvar(nf);
            return true;

        } catch (Exception ex) {
            Logger.getLogger(NfeProdutoService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public static String dataNfe() throws NfeException {
        try {
            LocalDateTime dataASerFormatada = LocalDateTime.now();
            GregorianCalendar calendar = GregorianCalendar.from(dataASerFormatada.atZone(ZoneId.of("Brazil/East")));

            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            xmlCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

            return xmlCalendar.toString();
        } catch (DatatypeConfigurationException e) {
            throw new NfeException(e.getMessage());
        }
    }

}
