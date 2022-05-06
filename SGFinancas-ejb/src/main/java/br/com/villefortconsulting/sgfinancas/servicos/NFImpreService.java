package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NotaImpressaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoNfDTO;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Dest;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.primefaces.model.StreamedContent;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NFImpreService extends BasicService<EntityId, BasicRepository<EntityId>, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @EJB
    private VendaService vendaService;

    @EJB
    private EmpresaService empresaService;

    private Empresa empresa;

    private NF nf;

    NotaImpressaDTO notaImpressao = new NotaImpressaDTO();

    public List<ProdutoNfDTO> setaCampoProdutoDaNota(Venda venda) {
        return vendaService.listarVendaProduto(venda).stream()
                .map(vendaProd -> {
                    ProdutoNfDTO produtoDTO = new ProdutoNfDTO();
                    produtoDTO.setCst(vendaProd.getDadosProduto().getIdCst().getDescricao());
                    produtoDTO.setProdutoCest(vendaProd.getDadosProduto().getIdProduto().getIdNcm().getCest());
                    produtoDTO.setProdutoNCM(vendaProd.getDadosProduto().getIdNcm().getCodigo());
                    produtoDTO.setProdutoUniComercial(vendaProd.getDadosProduto().getIdProduto().getIdUnidadeMedida().getDescricao());
                    produtoDTO.setProdutoCFOP(vendaProd.getDadosProduto().getIdCfop().getDescricao());
                    produtoDTO.setProdutoDescricao(vendaProd.getDadosProduto().getIdProduto().getDescricao());
                    produtoDTO.setProdutoCodigo(vendaProd.getDadosProduto().getIdProduto().getCodigo());
                    produtoDTO.setProdutoICMS("60 - ICMS cobrado anteriormente por substituição tributária.");
                    produtoDTO.setProdutoCodigoEANComercial("18116194032158");
                    produtoDTO.setProdutoValorICMS(12.49);
                    produtoDTO.setProdutoICMSValorBase(12.49);
                    produtoDTO.setProtudoICMSValorAliquota(12.49);
                    produtoDTO.setProdutoCofinsValorBase(12.49);
                    produtoDTO.setProdutoIndicadorCompValorNFE("1 - O valor do item (vProd) compõe o valor total da NF-e (vProd).");
                    produtoDTO.setProdutoPisValorBase(12.49);
                    produtoDTO.setProtudoPisValorAliquota(12.49);
                    produtoDTO.setProdutoValorPis(12.49);
                    produtoDTO.setProdutoCofinsValorBase(12.49);
                    produtoDTO.setProdutoValorCofins(12.49);
                    produtoDTO.setProtudoCofinsValorAliquota(12.49);
                    produtoDTO.setProdutoNumPedidoCompra("");
                    produtoDTO.setProdutoOrigemMercadoria(vendaProd.getDadosProduto().getIdProduto().getIdOrigemMercadoria().getDescricao());
                    produtoDTO.setProdutoQnt(vendaProd.getDadosProduto().getQuantidade());
                    produtoDTO.setProdutoValor(vendaProd.getDadosProduto().getValorTotal());
                    produtoDTO.setProdutoValorDesconto(vendaProd.getIdVenda().getValorDesconto());
                    produtoDTO.setProdutoValorFrete(vendaProd.getIdVenda().getValorFrete());
                    produtoDTO.setProdutoValorUnitarioTributacao(0.0);
                    return produtoDTO;
                }).collect(Collectors.toList());
    }

    public Double somaValorDesconto(List<ProdutoNfDTO> listProdutoNFDTO) {
        return listProdutoNFDTO.stream()
                .mapToDouble(ProdutoNfDTO::getProdutoValorDesconto)
                .sum();
    }

    public Double somaValorTotalProduto(List<ProdutoNfDTO> listProdutoNFDTO) {
        return listProdutoNFDTO.stream()
                .mapToDouble(ProdutoNfDTO::getProdutoValor)
                .sum();
    }

    public Double valorTotalNota(List<ProdutoNfDTO> listProdutoNFDTO) {
        return somaValorTotalProduto(listProdutoNFDTO) - somaValorDesconto(listProdutoNFDTO);
    }

    public Double somaQntTotalProduto(List<ProdutoNfDTO> listProdutoNFDTO) {
        return listProdutoNFDTO.stream()
                .mapToDouble(ProdutoNfDTO::getProdutoQnt)
                .sum();
    }

    public Double somaValorTotalFreteProduto(List<ProdutoNfDTO> listProdutoNFDTO) {
        return listProdutoNFDTO.stream()
                .mapToDouble(ProdutoNfDTO::getProdutoValorFrete)
                .sum();
    }

    public void setaCabecalhoNota() {
        notaImpressao.setNotaChaveAcesso(nf.getCodigoNumerico());
        notaImpressao.setNumeroNfe(nf.getNumeroNotaFiscal());
        notaImpressao.setVersaoNfe("3.0");
    }

    public void setaDadosNfe(List<ProdutoNfDTO> listProdutoNFDTO) {
        notaImpressao.setModeloNota(55);
        notaImpressao.setSerieNota(Integer.parseInt(nf.getObjTNFe().getInfNFe().getIde().getSerie()));
        notaImpressao.setDataEmissaoNota(nf.getDataEmissao());
        notaImpressao.setDataHoraSaidaEntradaNota(nf.getDataGeracao());
        notaImpressao.setValorTotalNota(nf.getValorNota());
    }

    public void setaDadosEmitente() {
        if (empresaService.getEmpresPorTenatID().getTenatID().equals(nf.getTenatID())) {
            notaImpressao.setCnpjEmitente(empresa.getCnpj());
            notaImpressao.setRazaoSocialEmitente(empresa.getDescricao());
            notaImpressao.setInscricaoEstadualEmitente(empresa.getInscricaoEstadual());
            notaImpressao.setUfEmitente(empresa.getEndereco().getIdCidade().getIdUF().getDescricao());
        }
    }

    public void setaDadosDestinatario() {
        Dest dest = nf.getObjTNFe().getInfNFe().getDest();
        notaImpressao.setCnpjDestinatario(dest.getCpfCnpj());
        notaImpressao.setRazaoSocialDestinatario(dest.getXNome());
        notaImpressao.setInscricaoEstadualDestinatario(nf.getIdCliente().getInscricaoEstadual());
        notaImpressao.setUfDestinatario(dest.getEnderDest().getUF().toString());
        notaImpressao.setDestinoOperacao("1 - Operação interna.");
        notaImpressao.setConsumidorFinal("1 - Consumidor final.");
        notaImpressao.setPresencaComprador("9 - Operação não presencial(outros).");
    }

    public void setaDadosEmissao() {
        notaImpressao.setProcesso("0 ­ com aplicativo do Contribuinte");
        notaImpressao.setVersaoProcesso("SisTranfe 1.0.0.0");
        notaImpressao.setFinalidade("1 ­ Normal");
        notaImpressao.setNaturezaOperacao("VENDA SUBST.TRIB.");
        notaImpressao.setTipoOperacao("1 ­ Saída");
        notaImpressao.setFormaPagamento(nf.getIdVenda().getFormaPagamento());
        notaImpressao.setDigestValueNfe("hBCeUnTBaZW4pvSjLascoOgtv5A=");
    }

    public void setaSituacaoAtual() {
        notaImpressao.setSituacao(nf.getSituacao());
        notaImpressao.setEventoNfe("Autorização de Uso");
        notaImpressao.setProtocolo("0514848511846");
        notaImpressao.setDataAutorizacao(DataUtil.getHoje());
        notaImpressao.setDataInclusaoAn(DataUtil.getHoje());
    }

    public void setaDadosComplementaresEmitente() {
        if (empresaService.getEmpresPorTenatID().getTenatID().equals(nf.getTenatID())) {
            notaImpressao.setEnderecoEmitente(empresa.getEndereco().getEndereco());
            notaImpressao.setBairroEmitente(empresa.getEndereco().getBairro());
            notaImpressao.setCepEmitente(empresa.getEndereco().getCep());
            notaImpressao.setMunicipioEmitente(empresa.getEndereco().getIdCidade().getDescricao());
            notaImpressao.setTelefoneEmitente(empresa.getFone());
            notaImpressao.setPaisEmitente("Brasil");
            notaImpressao.setInscricaoEstadualSubsTribuEmitente("");
            notaImpressao.setInscricaoMunicipalEmitente(empresa.getInscricaoMunicipal());
            notaImpressao.setMunicipioOcorrenciaFatoGeradorICMSEmitente("");
            notaImpressao.setCnaeFiscalEmitente("");
            notaImpressao.setCodRegimeTributarioEmitente("158432");
        }
    }

    public void setaDadosComplementaresDestinatario() {
        Dest dest = nf.getObjTNFe().getInfNFe().getDest();
        notaImpressao.setIndicadorIeDestinatario(nf.getIdCliente().getIndicadorInscricaoEstadual());
        notaImpressao.setEnderecoDestinatario(dest.getEnderDest().getXLgr());
        notaImpressao.setBairroDestinatario(dest.getEnderDest().getXBairro());
        notaImpressao.setCepDestinatario(dest.getEnderDest().getCEP());
        notaImpressao.setMunicipioDestinatario(dest.getEnderDest().getXMun());
        notaImpressao.setTelefoneDestinatario(dest.getEnderDest().getFone());
        notaImpressao.setPaisDestinatario("Brasil");
        notaImpressao.setInscricaoMunicipalDestinatario(nf.getIdCliente().getInscricaoMunicipal());
        notaImpressao.setInscricaoSuframaDestinatario("");
        notaImpressao.setEmailDestinatario(dest.getEnderDest().getFone());
    }

    public void setaDadosTotaisIcms(List<ProdutoNfDTO> listProdutoNFDTO) {
        notaImpressao.setBaseCalculoIcms(0.0);
        notaImpressao.setValorIcms(0.0);
        notaImpressao.setValorIcmsDesonerado(0.0);
        notaImpressao.setValorBaseCalculoIcmsSt(0.0);
        notaImpressao.setValorIcmsSubstituicao(0.0);
        notaImpressao.setValorTotalProdutos(somaValorTotalProduto(listProdutoNFDTO));
        notaImpressao.setValorTotalFrete(nf.getValorFrete());
        notaImpressao.setValorSeguro(nf.getValorSeguro());
        notaImpressao.setOutrasDespesas(nf.getValorDespesasAdicionais());
        notaImpressao.setValorTotalIpi(0.);
        notaImpressao.setValorTotalDescontos(somaValorDesconto(listProdutoNFDTO));
        notaImpressao.setValorTotalII(0.0);
        notaImpressao.setValorPis(nf.getValorPis());
        notaImpressao.setValorCofins(nf.getValorConfins());
        notaImpressao.setValorApxTributos(0.0);
        notaImpressao.setValorTotalIcmsFcp(0.0);
        notaImpressao.setValorIcmsInteerestUfDestino(0.0);
        notaImpressao.setValorTotalIcmsInteerstUfRemente(0.0);
    }

    public void setaDadosTransporte() {
        notaImpressao.setModalidadeFrete(nf.getTemFrete());
        notaImpressao.setCnpjTransportador(nf.getIdTransportadora().getCnpj());
        notaImpressao.setRazaoSocialTransportador(nf.getIdTransportadora().getDescricao());
        notaImpressao.setInscricaoEstadualTransportador(nf.getIdTransportadora().getInscricaoEstadual());
        notaImpressao.setEndCompTransportador(nf.getIdTransportadora().getEndereco());
        notaImpressao.setMunicipioTransportador(nf.getIdTransportadora().getIdCidade().getDescricao());
        notaImpressao.setUfTransportador(nf.getIdTransportadora().getIdCidade().getIdUF().getDescricao());
        // Veículo
        notaImpressao.setPlacaVeiculo(nf.getPlacaVeiculo());
        notaImpressao.setUfVeiculo("");
        notaImpressao.setRntcVeiculo(nf.getRntcVeiculo());
    }

    public void setaDadosVolume(List<ProdutoNfDTO> listProdutoNFDTO) {
        notaImpressao.setQntProdutos(somaQntTotalProduto(listProdutoNFDTO));
        notaImpressao.setEspecie("");
        notaImpressao.setMarcaVolume("");
        notaImpressao.setNumeracaoVolume("");
        notaImpressao.setPesoBrutoTotal(nf.getPesoBrutoTotal());
        notaImpressao.setPesoLiquidoTotal(nf.getPesoLiquidoTotal());
    }

    // Ver link http://www.javac.com.br/jc/posts/list/286.page
    public StreamedContent gerarNFe(NF notaFiscal) throws JRException, FileException {
        JasperReport jr = JasperCompileManager.compileReport(ClienteService.class.getResourceAsStream("Fiscal.jrxml"));
        InputStream is = new ByteArrayInputStream(notaFiscal.getXmlArmazenamento().getBytes(StandardCharsets.UTF_8));

        JRXmlDataSource xml = new JRXmlDataSource(is, "/nfeProc/NFe/infNFe/det");
        JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap<>(), xml);
        byte[] bFile = JasperExportManager.exportReportToPdf(jp);

        return FileUtil.downloadFile(bFile, "application/pdf", "NotaFiscal.pdf");
    }

}
