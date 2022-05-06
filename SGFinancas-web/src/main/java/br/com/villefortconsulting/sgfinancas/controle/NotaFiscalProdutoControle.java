package br.com.villefortconsulting.sgfinancas.controle;

import br.com.swconsultoria.nfe.schema.envcce.TretEvento;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEndereco;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUf;
import br.com.villefortconsulting.sgfinancas.controle.apoio.XmlElementSignatureController;
import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import br.com.villefortconsulting.sgfinancas.entidades.Cst;
import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.NFCorrecao;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.InutilizacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NcmDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValidacaoNFeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NotaFiscalProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.Filler;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.CompraService;
import br.com.villefortconsulting.sgfinancas.servicos.CtissService;
import br.com.villefortconsulting.sgfinancas.servicos.DanfeService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.NFImpreService;
import br.com.villefortconsulting.sgfinancas.servicos.NFService;
import br.com.villefortconsulting.sgfinancas.servicos.NcmService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.nfe.NfeProdutoService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNota;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCodigoModeloFiscal;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoFrete;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.COFINS;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.COFINSST;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.II;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ISSQN;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.PIS;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.PISST;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp.Vol;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
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
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaFiscalProdutoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private NFService nFService;

    @EJB
    private NcmService ncmService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private UFService ufService;

    @EJB
    private DanfeService danfeService;

    @EJB
    private NFImpreService nFImpreService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private CtissService ctissService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private VendaService vendaService;

    @EJB
    private CompraService compraService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private NF nfSelecionado;

    private NFCorrecao nfCorrecaoSelecionado;

    private Venda vendaSelecionada;

    private Compra compraSelecionada;

    private VendaProduto vendaProdutoSelecionado;

    private LazyDataModel<NF> model;

    private NotaFiscalProdutoFiltro filtro = new NotaFiscalProdutoFiltro();

    private NcmDTO ncmDTO;

    private List<VendaProduto> produtosNf;

    private String cancNota;

    @EJB
    private NfeProdutoService nfeProdutoService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    private ValidacaoNFeDTO validacaoNFeDTO;

    private String retornoEnvioNfe;

    private boolean envioOK;

    private boolean envioBotoesOK;

    private String textoChave;

    private String textoAcesso;

    private boolean habilitaDialog = false;

    private String certificateField;

    private String signatureField;

    private String xmlEnvio;

    private String chave;

    private String tipoUsoCertificadoA3;

    private InutilizacaoDTO inutilizacaoDTO;

    private String situacaoAmbiente;

    private Cfop cfopPadrao;

    private Integer nivelExibicao;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, nFService::quantidadeRegistrosFiltrados, nFService::getListaFiltrada);
        filtro.getDataGeracao().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getDataGeracao().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    @Override
    public void cleanCache() {
        nfSelecionado = null;
        nfCorrecaoSelecionado = null;
        vendaSelecionada = null;
        compraSelecionada = null;
        vendaProdutoSelecionado = null;
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_NOTA_FISCAL_PRODUTO.getChave()).getDescricao());
        return "cadastroNotaFiscalProduto.xhtml";
    }

    public List<Ctiss> getCtiss() {
        return ctissService.listar();
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Cidade> getCidades() {
        if (nfSelecionado.getIdUFCliente() != null) {
            return cidadeService.listar(nfSelecionado.getIdUFCliente());
        }
        return new LinkedList<>();
    }

    public void preencherDescricaoCidade() {
        if (nfSelecionado.getIdCidadeCliente() != null) {
            TEndereco end = nfSelecionado.getObjTNFe().getInfNFe().getDest().getEnderDest();
            end.setCMun(nfSelecionado.getIdCidadeCliente().getCodigoIBGE());
            end.setXMun(nfSelecionado.getIdCidadeCliente().getDescricao());
            end.setUF(TUf.fromValue(nfSelecionado.getIdCidadeCliente().getIdUF().getDescricao()));
        }
    }

    public List<Cidade> getMunicipios() {
        if (nfSelecionado.getIdUfPrestacao() != null) {
            return cidadeService.listar(nfSelecionado.getIdUfPrestacao());
        }
        return new LinkedList<>();
    }

    public void preencherDest() {
        Cliente cliente = nfSelecionado.getIdCliente();
        Fornecedor fornecedor = nfSelecionado.getIdFornecedor();
        nfSelecionado.getObjTNFe().getInfNFe().setDest(Filler.preencherDest(cliente, fornecedor, null));
    }

    public List<Produto> getProdutos() {
        return produtoService.listar();
    }

    public void desabilitarDialog() {
        habilitaDialog = false;
    }

    public void alterarFrete() {
        NFe.InfNFe.Transp transp = nfSelecionado.getObjTNFe().getInfNFe().getTransp();
        if (transp.getModFrete().equals(EnumTipoFrete.POR_CONTA_TERCEIROS.getTipo())) {
            transp.getVol().clear();
        }
    }

    public void alterarProduto(VendaProduto vendaProduto) {
        habilitaDialog = true;
        vendaProdutoSelecionado = vendaProduto;
        ncmDTO = ncmService.carregarNcmDTO(vendaProduto.getDadosProduto().getIdNcm());
    }

    public void salvarNcm() {
        if (vendaSelecionada.getVendaProdutoList().contains(vendaProdutoSelecionado)) {
            try {
                vendaProdutoSelecionado.getDadosProduto().getIdProduto().setIdNcm(vendaProdutoSelecionado.getDadosProduto().getIdNcm());
                produtoService.salvarProduto(vendaProdutoSelecionado.getDadosProduto().getIdProduto());
            } catch (CadastroException ex) {
                Logger.getLogger(NotaFiscalProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
            }
            vendaSelecionada.getVendaProdutoList().set(vendaSelecionada.getVendaProdutoList().indexOf(vendaProdutoSelecionado), vendaProdutoSelecionado);
        }
    }

    public String consultaSituacaoAmbiente() {
        situacaoAmbiente = nfeProdutoService.consulta();
        return situacaoAmbiente;
    }

    public StreamedContent downloadXmls(NF nf) {
        try {
            String nomeNota = "nfe_" + nf.getNumeroNotaFiscal() + ".rar";
            return FileUtil.downloadFile(nfeProdutoService.compactarXmlsNFS(nf), "application/x-rar-compressed", nomeNota);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public StreamedContent downloadXmlArmazenamento(NF nf) {
        try {
            String nomeNota = "nfe_" + nf.getNumeroNotaFiscal();

            return FileUtil.downloadFile(FileUtil.convertFileToBytes(FileUtil
                    .createTxtFile(nomeNota, ".xml", nf.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao()) ? nf.getXmlArmazenamento() : nf.getXmlArmazenamentoCancelamento())), "application/xml", nomeNota + ".xml");
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public String doStartAdd() {
        cfopPadrao = null;
        validacaoNFeDTO = nfeProdutoService.empresaAptaEmitirNF(parametroSistemaService.getAmbienteNotaFiscalProduto());

        if (!validacaoNFeDTO.isTodosOK()) {
            return "/restrito/notaFiscalProduto/informacao.xhtml";
        }

        nfSelecionado = new NF();
        nivelExibicao = 1;

        try {
            if (vendaSelecionada == null && compraSelecionada == null) {
                nFService.preencherNFeVazia(nfSelecionado);
            } else if (compraSelecionada == null) {// vendaSelecionada != null (essa validação é necessária pq se tanto a venda quanto a compra estiverem preenchidas não pode gerar a nota)
                nFService.preencherNFeParaEdicao(nfSelecionado, vendaSelecionada);
            } else if (vendaSelecionada == null) {// compraSelecionada != null (essa validação é necessária pq se tanto a venda quanto a compra estiverem preenchidas não pode gerar a nota)
                nFService.preencherNFeParaEdicao(nfSelecionado, compraSelecionada);
            } else {
                createFacesErrorMessage("Ocorreu um problema ao tentar criar a NFe, tente novamente.");
                vendaSelecionada = null;
                compraSelecionada = null;
                nfSelecionado = null;
            }
            preencherDest();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            String motivo = "";
            if (ex.getMessage() != null) {
                motivo = ex.getMessage();
            }
            createFacesErrorMessage("Não foipossível gerar a NFe. (" + motivo + ")");
            return "";
        }
        return "/restrito/notaFiscalProduto/cadastroNotaFiscalProduto.xhtml?faces-redirect=true";
    }

    public String doStartDisablement(String cnpj, String uf) {
        if (situacaoAmbiente != null && !situacaoAmbiente.isEmpty() && !situacaoAmbiente.endsWith(": Operacional.")) {
            createFacesErrorMessage(situacaoAmbiente + " A ação não pôde ser realizada.");
            return "listaNotaFiscalProduto.xhtml";
        }
        inutilizacaoDTO = new InutilizacaoDTO(new Date(), cnpj, uf);
        return "informarInutulizacao.xhtml";
    }

    public String doFinishAddDisablement() {
        try {
            if (inutilizacaoDTO.getMotivo().length() < 15) {
                throw new NfeException("A justificativa deve ter no mínimo 15 caracteres.", null);
            }
            if (inutilizacaoDTO.getModelo() == null) {
                throw new NfeException("Selecione um modelo.", null);
            }
            nfeProdutoService.inutilizarNumeracao(inutilizacaoDTO);
            nFService.salvarInutilizacao(nFService.inutilizacaoDtoToEntity(inutilizacaoDTO, getUsuarioLogado()));
            createFacesSuccessMessage("Numeração inutilizada.");
        } catch (NfeException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "informarInutulizacao.xhtml";
        }
        return "listaNotaFiscalProduto.xhtml";
    }

    public String doStartAddCorrecao() {
        nfCorrecaoSelecionado = nFService.preecherNFCorrecao(nfSelecionado);
        return "cadastroCorrecaoNotaFiscalProduto.xhtml";
    }

    public void preencherVendaProduto() {
        vendaSelecionada.setVendaProdutoList(vendaService.listarVendaProduto(vendaSelecionada));
        for (VendaProduto vendaProduto : vendaSelecionada.getVendaProdutoList()) {
            if (vendaProduto.getDadosProduto().getIdNcm() == null) {
                if (vendaProduto.getDadosProduto().getIdProduto().getIdNcm() != null) {
                    vendaProduto.getDadosProduto().setIdNcm(vendaProduto.getDadosProduto().getIdProduto().getIdNcm());
                } else {
                    vendaProduto.getDadosProduto().setIdNcm(new Ncm());
                }
            }
            if (vendaProduto.getDadosProduto().getIdCfop() == null) {
                vendaProduto.getDadosProduto().setIdCfop(new Cfop());
            }
            if (empresaService.empresaPertenceAoSimplesNacional(empresaService.getEmpresa())) {
                if (vendaProduto.getDadosProduto().getIdCsosn() == null) {
                    if (vendaProduto.getDadosProduto().getIdProduto().getIdCsosn() != null) {
                        vendaProduto.getDadosProduto().setIdCsosn(vendaProduto.getDadosProduto().getIdProduto().getIdCsosn());
                    } else {
                        vendaProduto.getDadosProduto().setIdCsosn(new Csosn());
                    }
                }
            } else if (vendaProduto.getDadosProduto().getIdCst() == null) {
                if (vendaProduto.getDadosProduto().getIdProduto().getIdCst() != null) {
                    vendaProduto.getDadosProduto().setIdCst(vendaProduto.getDadosProduto().getIdProduto().getIdCst());
                } else {
                    vendaProduto.getDadosProduto().setIdCst(new Cst());
                }
            }
        }
    }

    public void preencherCompraProduto() {
        for (CompraProduto compraProduto : compraSelecionada.getListCompraProduto()) {
            if (compraProduto.getDadosProduto().getIdNcm() == null) {
                if (compraProduto.getDadosProduto().getIdProduto().getIdNcm() != null) {
                    compraProduto.getDadosProduto().setIdNcm(compraProduto.getDadosProduto().getIdProduto().getIdNcm());
                } else {
                    compraProduto.getDadosProduto().setIdNcm(new Ncm());
                }
            }
            if (compraProduto.getDadosProduto().getIdCfop() == null) {
                compraProduto.getDadosProduto().setIdCfop(new Cfop());
            }
            if (empresaService.empresaPertenceAoSimplesNacional(empresaService.getEmpresa())) {
                if (compraProduto.getDadosProduto().getIdCsosn() == null) {
                    if (compraProduto.getDadosProduto().getIdProduto().getIdCsosn() != null) {
                        compraProduto.getDadosProduto().setIdCsosn(compraProduto.getDadosProduto().getIdProduto().getIdCsosn());
                    } else {
                        compraProduto.getDadosProduto().setIdCsosn(new Csosn());
                    }
                }
            } else if (compraProduto.getDadosProduto().getIdCst() == null) {
                if (compraProduto.getDadosProduto().getIdProduto().getIdCst() != null) {
                    compraProduto.getDadosProduto().setIdCst(compraProduto.getDadosProduto().getIdProduto().getIdCst());
                } else {
                    compraProduto.getDadosProduto().setIdCst(new Cst());
                }
            }
            if (compraProduto.getDadosProduto().getIdNcm() == null) {
                compraProduto.getDadosProduto().setIdNcm(new Ncm());
            }
            if (compraProduto.getDadosProduto().getIdCfop() == null) {
                compraProduto.getDadosProduto().setIdCfop(new Cfop());
            }
            if (empresaService.empresaPertenceAoSimplesNacional(empresaService.getEmpresa())) {
                if (compraProduto.getDadosProduto().getIdCsosn() == null) {
                    compraProduto.getDadosProduto().setIdCsosn(new Csosn());
                }
            } else if (compraProduto.getDadosProduto().getIdCst() == null) {
                compraProduto.getDadosProduto().setIdCst(new Cst());
            }
        }
    }

    public String doStartAlterarCorrecao() {
        return "cadastroCorrecaoNotaFiscalProduto.xhtml";
    }

    public String doStartAlterar() {
        vendaSelecionada = nfSelecionado.getIdVenda();
        compraSelecionada = nfSelecionado.getIdCompra();
        nivelExibicao = 1;
        if (vendaSelecionada != null) {
            vendaSelecionada.setVendaProdutoList(vendaService.listarVendaProduto(vendaSelecionada));
        } else if (compraSelecionada != null) {
            compraSelecionada.setListCompraProduto(compraService.listarCompraProduto(compraSelecionada));
        }
        try {
            nFService.loadXMLWithEntity(nfSelecionado);
            return "cadastroNotaFiscalProduto.xhtml";
        } catch (IOException ex) {
            createFacesErrorMessage("Não foi possível carregar o XML da nota.");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String doFinishAddCorrecao() {
        Empresa empresa = empresaService.getEmpresa();
        try {
            nfeProdutoService.geraXMLCCe(nfCorrecaoSelecionado, empresa);
            nfSelecionado.addNfCorrecao(nfCorrecaoSelecionado);
            nFService.salvar(nfSelecionado);
            if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                xmlEnvio = XmlElementSignatureController.normalizarXML(nfCorrecaoSelecionado.getXmlEnvio());
                nfCorrecaoSelecionado.setXmlEnvio(xmlEnvio);
                return "assinarCCeA3.xhtml";
            }
            nfeProdutoService.enviarCCe(nfCorrecaoSelecionado, empresa);
            processaRetCCe();
            nfCorrecaoSelecionado.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
            createFacesSuccessMessage("Correção salva com sucesso!");
            return "listaNotaFiscalProdutoCorrecao.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            nfCorrecaoSelecionado.setSituacao(EnumSituacaoNF.REJEITADA.getSituacao());
        }
        nFService.salvar(nfSelecionado);
        return "cadastroCorrecaoNotaFiscalProduto.xhtml";
    }

    private void processaRetCCe() throws NfeException {
        atualizaListaDeCorrecao();
        try {
            if (nfCorrecaoSelecionado.getXmlRetorno() == null) {
                throw new NfeException("Não foi possível obter o retorno da SEFAZ.", null);
            }
            br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento ret = new ObjectMapper().readValue(nfCorrecaoSelecionado.getXmlRetorno(), br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento.class);
            String motivo = ret.getRetEvento().stream()
                    .map(TretEvento::getInfEvento)
                    .filter(in -> !in.getCStat().equals("135"))//pegar as que o evento não foi vinculado
                    .map(in -> in.getXEvento() + ", chave: " + in.getChNFe() + ", motivo: " + in.getXMotivo())
                    .reduce("", (a, b) -> a + "\n" + b);
            if (!motivo.isEmpty()) {
                nfCorrecaoSelecionado.setSituacao("J");
                nFService.salvar(nfCorrecaoSelecionado.getIdNf());
                throw new NfeException("Os seguintes eventos não puderam ser registrados:\n" + motivo, null);
            } else {
                nfCorrecaoSelecionado.setSituacao("E");
            }
        } catch (IOException ex) {
            Logger.getLogger(NotaFiscalProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
        }
        nFService.salvar(nfCorrecaoSelecionado.getIdNf());
    }

    public String assinarCCe() {
        Empresa empresa = empresaService.getEmpresa();
        try {
            if (nfCorrecaoSelecionado.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao())) {
                createFacesErrorMessage("Correção já emitida.");
                return "listaNotaFiscalProduto.xhtml";
            }
            if (certificateField == null) {
                createFacesErrorMessage("Informe o certificado");
                return "assinarCCeA3.xhtml";
            }

            String xmlAssinado = XmlElementSignatureController.assinarXmlCartaCorrecaoA3(
                    certificateField,
                    signatureField,
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", XmlUtil.removeAcentos(xmlEnvio))));

            nfCorrecaoSelecionado.setXmlEnvio(xmlAssinado);
            nfeProdutoService.enviarCCe(nfCorrecaoSelecionado, empresa);
            processaRetCCe();
            nfCorrecaoSelecionado.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
            createFacesSuccessMessage("Correção salva com sucesso!");
            return "listaNotaFiscalProdutoCorrecao.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            nfCorrecaoSelecionado.setSituacao(EnumSituacaoNF.REJEITADA.getSituacao());
            nFService.salvar(nfCorrecaoSelecionado.getIdNf());
        }
        nFService.salvar(nfSelecionado);
        return "cadastroCorrecaoNotaFiscalProduto.xhtml";
    }

    public String doFinishAdd() {
        try {
            nFService.salvarNotaFiscalProduto(nfSelecionado, getUsuarioLogado());

            cleanCache();
            createFacesSuccessMessage("Nota fiscal de serviço salva com sucesso!");
            return "listaNotaFiscalProduto.xhtml";
        } catch (NfeException ex) {
            for (String s : ex.getMessage().split("\n")) {
                createFacesErrorMessage(s);
            }
            return "";
        }
    }

    public String doFinishExcluirCorrecao() {
        nfSelecionado.removeNfCorrecao(nfCorrecaoSelecionado);
        nFService.salvar(nfSelecionado);
        atualizaListaDeCorrecao();
        createFacesSuccessMessage("Correção removida com sucesso!");
        return "listaNotaFiscalProdutoCorrecao.xhtml";
    }

    public String doFinishExcluir() {
        nFService.remover(nfSelecionado);
        return "listaNotaFiscalProduto.xhtml";
    }

    public StreamedContent gerarDanfe() {
        return gerarDanfe(nfSelecionado);
    }

    public StreamedContent gerarDanfe(NF nf) {
        try {
            Empresa empresa = empresaService.getEmpresa();
            return danfeService.gerarDanfe(empresa, nf);
        } catch (Exception ex) {
            return null;
        }
    }

    public StreamedContent gerarNota() {
        return gerarNota(nfSelecionado);
    }

    public StreamedContent gerarNota(NF nf) {
        try {
            return nFImpreService.gerarNFe(nf);
        } catch (Exception ex) {
            Logger.getLogger(NotaFiscalProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Empresa getEmpresa() {
        return empresaService.getEmpresa();
    }

    public String getDescontoTotal() {
        // somando os descontos dos produtos da lista
        Double desconto = vendaSelecionada.getVendaProdutoList().stream()
                .filter(compraProduto -> compraProduto.getDadosProduto().getDesconto() != null)
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getDesconto)
                .sum();

        return "R$ " + super.converterValorParaMonetario(desconto);
    }

    public String getValorTotal() {
        Double total = 0d;

        // somando o total dos produtos da lista
        if (vendaSelecionada != null) {
            total = vendaSelecionada.getVendaProdutoList().stream()
                    .mapToDouble((VendaProduto vendaProduto) -> vendaProduto.getDadosProduto().getValor() * vendaProduto.getDadosProduto().getQuantidade())
                    .sum();
        } else if (compraSelecionada != null) {
            total = compraSelecionada.getListCompraProduto().stream()
                    .mapToDouble((CompraProduto compraProduto) -> compraProduto.getDadosProduto().getValor() * compraProduto.getDadosProduto().getQuantidade())
                    .sum();
        }
        return "R$ " + super.converterValorParaMonetario(total);
    }

    public String getValorTotalComDesconto() {
        Double total = 0d;
        Double desconto = 0d;

        // somando o total dos produtos da lista
        if (vendaSelecionada != null) {
            total = vendaSelecionada.getVendaProdutoList().stream()
                    .mapToDouble((VendaProduto vendaProduto) -> vendaProduto.getDadosProduto().getValor() * vendaProduto.getDadosProduto().getQuantidade())
                    .sum();
            desconto = vendaSelecionada.getVendaProdutoList().stream()
                    .filter(vendaProduto -> vendaProduto.getDadosProduto().getDesconto() != null)
                    .map(VendaProduto::getDadosProduto)
                    .mapToDouble(DadosProduto::getDesconto)
                    .sum();
        } else if (compraSelecionada != null) {
            total = compraSelecionada.getListCompraProduto().stream()
                    .mapToDouble((CompraProduto compraProduto) -> compraProduto.getDadosProduto().getValor() * compraProduto.getDadosProduto().getQuantidade())
                    .sum();
            desconto = compraSelecionada.getListCompraProduto().stream()
                    .filter(compraProduto -> compraProduto.getDadosProduto().getDesconto() != null)
                    .map(CompraProduto::getDadosProduto)
                    .mapToDouble(DadosProduto::getDesconto)
                    .sum();
        }

        return "R$ " + super.converterValorParaMonetario(total - desconto);
    }

    public String doShowCartaCorrecao() {
        if (situacaoAmbiente != null && !situacaoAmbiente.isEmpty() && !situacaoAmbiente.endsWith(": Operacional.")) {
            createFacesErrorMessage(situacaoAmbiente + " A ação não pôde ser realizada.");
            return "listaNotaFiscalProduto.xhtml";
        }
        atualizaListaDeCorrecao();
        return "listaNotaFiscalProdutoCorrecao.xhtml";
    }

    public void atualizaListaDeCorrecao() {
        nfSelecionado.setNfCorrecaoList(nFService.obterLitaCorrecoes(nfSelecionado));
    }

    public String doShowAuditoria() {
        return "listaAuditoriaNotaFiscalProduto.xhtml";
    }

    public String doShowAuditoriaCorrecao() {
        return "listaAuditoriaCorrecaoNotaFiscalProduto.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return nFService.getDadosAuditoriaByID(nfSelecionado);
    }

    public List<Object> getDadosAuditoriaCorrecao() {
        return nFService.getDadosAuditoriaByID(nfCorrecaoSelecionado);
    }

    public String visualizarNF() {
        cancNota = "N";
        try {
            if (nfSelecionado.getObjTNFe() == null) {
                nFService.loadXMLWithEntity(nfSelecionado);
            }
            if (nfSelecionado.getIdVenda() != null) {
                produtosNf = vendaService.listarVendaProduto(nfSelecionado.getIdVenda());
            }
            if (nfSelecionado.getIdCompra() != null) {
                produtosNf = compraService.listarCompraProduto(nfSelecionado.getIdCompra()).stream()
                        .map(cp -> {
                            VendaProduto vp = new VendaProduto();
                            vp.setId(cp.getId());
                            vp.getDadosProduto().setDesconto(0d);
                            vp.getDadosProduto().setIdProduto(cp.getDadosProduto().getIdProduto());
                            vp.getDadosProduto().setQuantidade(cp.getDadosProduto().getQuantidade());
                            vp.getDadosProduto().setValor(cp.getDadosProduto().getValor());
                            return vp;
                        }).collect(Collectors.toList());
            }
            return "visualizarNotaFiscalProdutoEmitida.xhtml";
        } catch (IOException ex) {
            Logger.getLogger(NotaFiscalProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar a nota.");
            return "";
        }

    }

    public String enviarXmlPorEmail() {
        try {
            if (StringUtils.isEmpty(nfSelecionado.getIdCliente().getEmail())) {
                createFacesErrorMessage("Email não informado para o cliente");
                return "visualizarNotaFiscalProdutoEmitida.xhtml";
            }
            nFService.enviarXmlPorEmail(nfSelecionado, empresaService.getEmpresa(), produtosNf);
            createFacesSuccessMessage("Arquivo Xml enviado com sucesso! Email de envio: " + nfSelecionado.getIdCliente().getEmail());
            return "visualizarNotaFiscalProdutoEmitida.xhtml";

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "visualizarNotaFiscalProdutoEmitida.xhtml";
        }
    }

    public String getHash() {
        try {
            byte[] arquivo = FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", XmlUtil.removeAcentos(xmlEnvio)));
            return XmlElementSignatureController.gerarHash(arquivo, EnumTipoNota.PRODUTO.getTipo());
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return null;
        }
    }

    public String getHashCancelamento() {
        try {
            byte[] arquivo = FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio));
            return XmlElementSignatureController.gerarHash(arquivo, EnumTipoNota.CANCELAMENTO_PRODUTO.getTipo());
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return null;
        }
    }

    public String getHashCCe() {
        try {
            byte[] arquivo = FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", XmlUtil.removeAcentos(xmlEnvio)));
            return XmlElementSignatureController.gerarHash(arquivo, EnumTipoNota.EVENTO_PRODUTO.getTipo());
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return null;
        }
    }

    public String enviarNF() {
        vendaSelecionada = null;
        compraSelecionada = null;
        if (situacaoAmbiente != null && !situacaoAmbiente.isEmpty() && !situacaoAmbiente.endsWith(": Operacional.")) {
            createFacesErrorMessage(situacaoAmbiente + " A ação não pôde ser realizada.");
            return "listaNotaFiscalProduto.xhtml";
        }
        if (nfSelecionado.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao()) && !nfSelecionado.getChave().equals(chave)) {
            createFacesErrorMessage("Nota já emitida.");
            return "listaNotaFiscalProduto.xhtml";
        }
        String just = nfSelecionado.getJustificativaDevolucao();
        if (nfSelecionado.getIdNfReferencia() != null && (just == null || just.isEmpty())) {
            createFacesErrorMessage("Informe a justificativa para devolução.");
            return "listaNotaFiscalProduto.xhtml";
        }
        Empresa empresa = empresaService.getEmpresa();
        try {
            if ("2".equals(parametroSistemaService.getAmbienteNotaFiscalProduto())) {
                nfSelecionado.getObjTNFe().getInfNFe().getDest().setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
            }
            xmlEnvio = nfeProdutoService.obterXmlNfe(nfSelecionado, parametroSistemaService.getAmbienteNotaFiscalProduto())
                    .replaceAll("\\s+<", "<").replaceAll(">\\s+", ">").replaceAll("&", "&amp;");

            if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                return "assinarNotaFiscalProdutoA3.xhtml";
            }

            return transmitirNfe(XmlUtil.removeAcentos(xmlEnvio));
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaNotaFiscalProduto.xhtml";
        }
    }

    public String doStartCancelarNF() {
        if (situacaoAmbiente != null && !situacaoAmbiente.isEmpty() && !situacaoAmbiente.endsWith(": Operacional.")) {
            createFacesErrorMessage(situacaoAmbiente + " A ação não pôde ser realizada.");
            return "listaNotaFiscalProduto.xhtml";
        }
        cancNota = "S";
        return "visualizarNotaFiscalProdutoEmitida.xhtml";
    }

    public String doFinishCancelarNF() {
        Empresa empresa = empresaService.getEmpresa();

        try {
            xmlEnvio = nfeProdutoService.obterXmlNfeCancelamento(nfSelecionado, parametroSistemaService.getAmbienteNotaFiscalProduto());

            if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                return "cancelarNotaFiscalProdutoA3.xhtml";
            }

            nfSelecionado.setXmlEnvioCancelamento(xmlEnvio);

            return cancelarNfe(xmlEnvio);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaNotaFiscalProduto";
        }
    }

    public String cancelarNfe(String xml) {
        try {
            nfSelecionado.setXmlEnvioCancelamento(xml);
            nfeProdutoService.cancelarNFProduto(nfSelecionado);

            if (nfSelecionado.getSituacao().equals(EnumSituacaoNF.CANCELADA.getSituacao())) {
                createFacesSuccessMessage("Nota fiscal cancelada com sucesso!");
            } else {
                createFacesErrorMessage("Erro ao cancelar NF: " + nfSelecionado.getMensagemErroEnvio());
            }
            return "listaNotaFiscalProduto";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
            return "listaNotaFiscalProduto.xhtml";
        }
    }

    public String transmitirNfe(String xml) {
        try {
            nfSelecionado = nfeProdutoService.transmitirNFe(nfSelecionado, parametroSistemaService.getAmbienteNotaFiscalProduto(), xml, "NFe");

            if (EnumSituacaoNF.ENVIADA.getSituacao().equals(nfSelecionado.getSituacao())
                    || EnumSituacaoNF.ENVIADA_DEVOLUCAO.getSituacao().equals(nfSelecionado.getSituacao())) {
                createFacesSuccessMessage("Nota fiscal enviada com sucesso!");
                compraSelecionada = null;
                vendaSelecionada = null;
                if (nfSelecionado.getIdCompra() != null) {
                    produtosNf.stream()
                            .forEach(cp -> {
                                try {
                                    CompraProduto compraProduto = compraService.buscarCompraProduto(cp.getId());
                                    compraProduto.setDevolvido("S");
                                    compraService.salvarCompraProduto(compraProduto);
                                } catch (Exception ex) {
                                    Logger.getLogger(NotaFiscalProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                }
                return visualizarNF();
            } else {
                createFacesErrorMessage(nfSelecionado.getMensagemErroEnvio());
                return "listaNotaFiscalProduto.xhtml";
            }
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaNotaFiscalProduto.xhtml";
        }
    }

    public String assinarTransmitirXml() {
        try {
            if (nfSelecionado.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao()) && !nfSelecionado.getChave().equals(chave)) {
                createFacesErrorMessage("Nota já emitida.");
                return "listaNotaFiscalProduto.xhtml";
            }
            if (certificateField == null) {
                createFacesErrorMessage("Informe o certificado");
                return "assinarNotaFiscalServicoA3.xhtml";
            }

            String xmlAssinado = XmlElementSignatureController.assinarXmlNotaFiscalProdutoA3(
                    certificateField,
                    signatureField,
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", XmlUtil.removeAcentos(xmlEnvio))));

            return transmitirNfe(xmlAssinado);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "assinarNotaFiscalServicoA3.xhtml";
        }
    }

    public String assinarCancelarXml() {
        try {
            if (certificateField == null) {
                createFacesErrorMessage("Informe o certificado");
                return "assinarNotaFiscalServicoA3.xhtml";
            }

            String xmlAssinado = XmlElementSignatureController.obterXmlCancelamentoNotaFiscalProdutoA3(
                    certificateField,
                    signatureField,
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio)));

            return cancelarNfe(xmlAssinado);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
            return "assinarNotaFiscalServicoA3.xhtml";
        }
    }

    public String getSituacao(String tipo) {
        validacaoNFeDTO = nfeProdutoService.empresaAptaEmitirNF(parametroSistemaService.getAmbienteNotaFiscalProduto());

        String ok = "icon-like font-blue-madison";
        String notOk = "icon-dislike font-red-mint";

        switch (tipo) {
            case "CE":
                return validacaoNFeDTO.isCertificadoOK() ? ok : notOk;
            case "IE":
                return validacaoNFeDTO.isInscricaoEstadualOK() ? ok : notOk;
            case "ED":
                return validacaoNFeDTO.isEnderecoOK() ? ok : notOk;
            case "NF":
                return validacaoNFeDTO.isNumeroNFeOK() ? ok : notOk;
            case "SE":
                return validacaoNFeDTO.isSerieNFeOK() ? ok : notOk;
            case "II":
                return validacaoNFeDTO.isIndicadorInscricaoEstadualOK() ? ok : notOk;
            case "IM":
                return validacaoNFeDTO.isInscricaoMunicipalOK() ? ok : notOk;
            case "PE":
                return validacaoNFeDTO.isPorteEmpresaOK() ? ok : notOk;
            case "RT":
                return validacaoNFeDTO.isRegimeTributarioOK() ? ok : notOk;
            default:
                return "icon-dislike";
        }
    }

    public void processaRetornoA3() {
        nfSelecionado = nFService.buscar(nfSelecionado.getId());

        if (nfSelecionado.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao())) {
            envioOK = true;
            envioBotoesOK = true;
            textoChave = nfSelecionado.getChaveFormatada();
            textoAcesso = "Chave de acesso";
        } else {
            envioOK = false;
            textoChave = nfSelecionado.getMensagemErroEnvio();
            textoAcesso = "Falha ao transmitir a NF-e";
        }
    }

    public String getEnumMeioDePagamentoDesc(String cod) {
        if (cod == null) {
            return "";
        }
        return EnumMeioDePagamento.retornaEnumSelecionado(cod).getDescricao();
    }

    public EnumCodigoModeloFiscal[] getModelos() {
        return EnumCodigoModeloFiscal.values();
    }

    public boolean notaDevolvida(NF nf) {
        return nFService.notaDevolvida(nf);
    }

    public void removerItem(CompraProduto cp) {
        compraSelecionada.getListCompraProduto().remove(cp);
    }

    public void updateCfop() {
        nfSelecionado.getObjTNFe().getInfNFe().getDet().stream()
                .filter(det -> det.getProd().getCFOP() == null)
                .forEach(det -> det.getProd().getIdCfop().setValue(cfopPadrao));
    }

    public boolean requerCest(VendaProduto vp) {
        if (vp == null) {
            return false;
        }
        String cst = vp.getDadosProduto().getIdCst() != null ? vp.getDadosProduto().getIdCst().getCodigo() : "";
        String csosn = vp.getDadosProduto().getIdCsosn() != null ? vp.getDadosProduto().getIdCsosn().getCodigo() : "";
        return (Arrays.asList("10", "30", "60", "70", "90").stream().anyMatch(cod -> cod.equals(cst)))
                || (Arrays.asList("201", "202", "203", "500", "900").stream().anyMatch(cod -> cod.equals(csosn)));
    }

    public List<Ncm> listarNcm(String desc) {
        return ncmService.listarPorCodigo(desc.split(" - ")[0]);
    }

    public List<String> listarNcmAsString(String desc) {
        return ncmService.listarPorCodigo(desc).stream().map(Ncm::getCodigo).collect(Collectors.toList());
    }

    public String getXmlEnvioJson() {
        return new Gson().toJson(xmlEnvio);
    }

    public Boolean getUsaTransportadora() {
        return EnumTipoFrete.POR_CONTA_TERCEIROS.getTipo().equals(nfSelecionado.getObjTNFe().getInfNFe().getTransp().getModFrete());
    }

    public void addGrupoVolume() {
        nfSelecionado.getObjTNFe().getInfNFe().getTransp().getVol().add(new Vol());
    }

    public void removeGrupoVolume(Vol vol) {
        nfSelecionado.getObjTNFe().getInfNFe().getTransp().getVol().remove(vol);
    }

    public void addMeioPagamento() {
        nfSelecionado.getObjTNFe().getInfNFe().getPag().getDetPag().add(new NFe.InfNFe.Pag.DetPag());
    }

    public void removeMeioPagamento(NFe.InfNFe.Pag.DetPag pag) {
        nfSelecionado.getObjTNFe().getInfNFe().getPag().getDetPag().remove(pag);
    }

    public void addProduto() {
        NFe.InfNFe.Det det = new NFe.InfNFe.Det();
        det.setImposto(new NFe.InfNFe.Det.Imposto());
        det.setImpostoDevol(new NFe.InfNFe.Det.ImpostoDevol());
        det.setInfAdProd("");
        det.setNItem(nfSelecionado.getObjTNFe().getInfNFe().getDet().stream().map(NFe.InfNFe.Det::getNItem).reduce(1, (a, b) -> b + 1));
        det.setProd(new NFe.InfNFe.Det.Prod());
        nfSelecionado.getObjTNFe().getInfNFe().getDet().add(det);
    }

    public void removeProduto(NFe.InfNFe.Det det) {
        nfSelecionado.getObjTNFe().getInfNFe().getDet().remove(det);
    }

    public int getQteColunasPag() {
        int size = nfSelecionado.getObjTNFe().getInfNFe().getPag().getDetPag().size();
        return size < 3 ? size : 3;
    }

    public String getEmail(NF nf) {
        try {
            nFService.loadXML(nf);
            if (nf.getObjTNFe() != null) {
                return nf.getObjTNFe().getInfNFe().getDest().getEmail();
            }
        } catch (IOException ex) {
            Logger.getLogger(NotaFiscalProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void addimposto(Det det, String nome) {
        Imposto imposto = det.getImposto();
        switch (nome) {
            case "PISST":
                imposto.setPISST(new PISST());
                break;
            case "PIS":
                imposto.setPIS(new PIS(det));
                break;
            case "ISSQN":
                imposto.setISSQN(new ISSQN());
                nfSelecionado.getObjTNFe().getInfNFe().getTotal().setISSQNtot(new NFe.InfNFe.Total.ISSQNtot());
                break;
            case "II":
                imposto.setII(new II());
                break;
            case "ICMS":
                imposto.setICMS(new ICMS());
                EnumRegimeTributario ert = EnumRegimeTributario.retornaEnumSelecionado(empresaService.getEmpresa().getRegimeTributario());
                if (ert == EnumRegimeTributario.SIMPLES_NACIONAL_EMPRE_PEQ_PORT || ert == EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE) {
                    imposto.getICMS().getIcmsByCsosn(det.getIdCsosn().getValue().getCodigo());
                } else if (ert == EnumRegimeTributario.LUCRO_PRESUMIDO || ert == EnumRegimeTributario.LUCRO_REAL) {
                    imposto.getICMS().getIcmsByCst(det.getIdCst().getValue().getCodigo());
                }
                break;
            case "COFINSST":
                imposto.setCOFINSST(new COFINSST());
                break;
            case "COFINS":
                imposto.setCOFINS(new COFINS(det));
                break;
            default:
                break;
        }
    }

    public void changePIS(PIS pis) {
        pis.setPISAliq(null);
        pis.setPISNT(null);
        pis.setPISOutr(null);
        pis.setPISQtde(null);
        if (pis.getTipoPIS() != null) {
            switch (pis.getTipoPIS()) {
                case "PISAliq":
                    pis.setPISAliq(new PIS.PISAliq(pis));
                    break;
                case "PISNT":
                    pis.setPISNT(new PIS.PISNT(pis));
                    break;
                case "PISOutr":
                    pis.setPISOutr(new PIS.PISOutr(pis));
                    break;
                case "PISQtde":
                    pis.setPISQtde(new PIS.PISQtde(pis));
                    break;
                default:
                    break;
            }
        }
    }

    public void changeCOFINS(COFINS cofins) {
        cofins.setCOFINSAliq(null);
        cofins.setCOFINSNT(null);
        cofins.setCOFINSOutr(null);
        cofins.setCOFINSQtde(null);
        if (cofins.getTipoCOFINS() != null) {
            switch (cofins.getTipoCOFINS()) {
                case "COFINSAliq":
                    cofins.setCOFINSAliq(new COFINS.COFINSAliq(cofins));
                    break;
                case "COFINSNT":
                    cofins.setCOFINSNT(new COFINS.COFINSNT(cofins));
                    break;
                case "COFINSOutr":
                    cofins.setCOFINSOutr(new COFINS.COFINSOutr(cofins));
                    break;
                case "COFINSQtde":
                    cofins.setCOFINSQtde(new COFINS.COFINSQtde(cofins));
                    break;
                default:
                    break;
            }
        }
    }

    public boolean contains(String cod, List<Long> list) {
        String _cod = cod.replaceAll("\\D", "");
        if (_cod.isEmpty()) {
            return false;
        }
        Long intCod = Long.parseLong(_cod);
        return list.stream().anyMatch(intCod::equals);
    }

    public void recalculaTotais() {
        Filler.preencherTotais(nfSelecionado);
    }

    public String getResetTotal() {
        recalculaTotais();
        return "";
    }

    public void setResetTotal(String s) {
        //O JSF da erro se essa função não existir
    }

    public boolean autoUpdate() {
        return nfSelecionado.getModeloNota() != null && nfSelecionado.getEnumModelo().isCalculaImpostos();

    }

}
