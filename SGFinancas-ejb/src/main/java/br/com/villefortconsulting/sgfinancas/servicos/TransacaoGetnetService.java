/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.servicos.getnet.RequestApi;
import br.com.villefortconsulting.servicos.getnet.ResponseApi;
import br.com.villefortconsulting.servicos.getnet.ServiceApi;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoSistema;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CallbackDebitoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CartaoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemVendaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoSistemaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TransacaoGetnetRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTransacao;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTransacaoGetnet;
import br.com.villefortconsulting.util.AESUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransacaoGetnetService extends BasicService<TransacaoGetnet, TransacaoGetnetRepositorio, BasicFilter<TransacaoGetnet>> {

    private static final long serialVersionUID = 1L;

    private static final String SECRET_KEY = "cJe65pucIZOXKm18";

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PagamentoSistemaService pagamentoSistemaService;

    @EJB
    private ServiceApi serviceApi;

    @EJB
    private SmsService smsService;

    @EJB
    private VendaService vendaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new TransacaoGetnetRepositorio(em, adHocTenant);
    }

    public TransacaoGetnet buscar(String idPagamento) {
        return repositorio.buscar(idPagamento);
    }

    public TransacaoGetnet buscarAES(String id) {
        try {
            return buscar(Integer.parseInt(AESUtil.decrypt(id, SECRET_KEY)));
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings({"UseSpecificCatch", "squid:S2139"})
    public TransacaoGetnet pagamentoCredito(TransacaoGetnet transacao, CartaoGetnetDTO cartao, PagamentoSistemaDTO dto) throws IOException {
        RequestApi request = new RequestApi();
        ResponseApi response = null;

        if (transacao.getIdVenda() != null) {
            request.setCliente(serviceApi.preencherCliente(transacao.getIdVenda().getIdCliente()));
            request.setCompra(serviceApi.preencherCompra(transacao.getIdVenda()));
            request.setValor(transacao.getIdVenda().getValor());
        } else if (transacao.getIdPagamentoSistema() != null) {
            request.setCliente(serviceApi.preencherCliente(empresaService.getEmpresa(), dto));
            request.setCompra(serviceApi.preencherCompra(getProximoPagamento()));
            request.setValor(transacao.getIdPagamentoSistema().getValor());
        } else {
            throw new IllegalArgumentException("Transação inválida.");
        }

        request.setCredito(serviceApi.preencherCredito(cartao, "Compras SGFinanças", 1d));
        request.setIdSistema(2);
        try {
            response = serviceApi.paymentCredit(request);

            return preencherTransacaEAlterar(response, transacao, request);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            if (response != null) {
                serviceApi.estorno(response.getIdPagamento());
            }
            throw ex;
        }
    }

    public TransacaoGetnet pagamentoDebito(TransacaoGetnet transacao, CartaoGetnetDTO cartao) throws IOException {
        RequestApi request = new RequestApi();

        request.setDebito(serviceApi.preencherDebito(cartao));
        request.setCliente(serviceApi.preencherCliente(transacao.getIdVenda().getIdCliente()));
        request.setCompra(serviceApi.preencherCompra(transacao.getIdVenda()));
        request.setIdSistema(2);
        request.setValor(transacao.getIdVenda().getValor());

        ResponseApi response = serviceApi.paymentDebit(request);

        transacao.setDataSolicitacao(DataUtil.getHoje());
        transacao.setIdPagamento(response.getIdPagamento());
        transacao.setNomeCartao(request.getCartao().getNomeCartao());
        transacao.setNumeroCartao(StringUtil.finalDoCartao(request.getCartao().getNumeroCartao()));
        transacao.setMd(response.getMd());

        transacao = alterar(transacao);

        transacao.setResponseDebito(response);

        return transacao;
    }

    public TransacaoGetnet finalizacaoDebito(TransacaoGetnet transacao, String paRes) throws IOException {
        CallbackDebitoDTO request = new CallbackDebitoDTO();

        request.setPaRes(paRes);
        request.setMd(transacao.getMd());

        ResponseApi response = serviceApi.finshDebit(request);

        transacao.setSituacao(response.getStatus());
        transacao.setIdAutorizacao(response.getIdAutorizacao());
        transacao.setMensagem(response.getMensagem());
        if (response.getStatus().equals(EnumSituacaoTransacaoGetnet.AUTORIZADO.getSitaucao())) {
            Venda venda = transacao.getIdVenda();
            PagamentoSistema pagamentoSistema = transacao.getIdPagamentoSistema();
            if (venda != null) {
                venda.getIdConta().setContaParcelaList(contaService.listarContaParcela(venda.getIdConta()));
                vendaService.darBaixa(venda);
            } else if (pagamentoSistema != null) {
                Empresa empresa = empresaService.getEmpresa();
                empresa.setTipoConta("P");
                empresaService.salvar(empresa);
            }
        }

        return alterar(transacao);
    }

    public TransacaoGetnet pagamentoBoleto(TransacaoGetnet transacao, PagamentoSistemaDTO dto) throws IOException {
        RequestApi request = new RequestApi();

        if (transacao.getIdVenda() != null) {
            request.setBoleto(serviceApi.preencherBoleto(transacao.getIdVenda()));
            request.setCliente(serviceApi.preencherCliente(transacao.getIdVenda().getIdCliente()));
            request.setCompra(serviceApi.preencherCompra(transacao.getIdVenda()));
            request.setValor(transacao.getIdVenda().getValor());
        } else if (transacao.getIdPagamentoSistema() != null) {
            request.setBoleto(serviceApi.preencherBoleto(transacao.getIdPagamentoSistema()));
            request.setCliente(serviceApi.preencherCliente(empresaService.getEmpresa(), dto));
            request.setCompra(serviceApi.preencherCompra(getProximoPagamento()));
            request.setValor(transacao.getIdPagamentoSistema().getValor());
        } else {
            throw new IllegalArgumentException("Transação inválida.");
        }
        request.setIdSistema(2);

        ResponseApi response = serviceApi.paymentBoleto(request);

        return preencherTransacaEAlterar(response, transacao, request);
    }

    public TransacaoGetnet preencherTransacaEAlterar(ResponseApi response, TransacaoGetnet transacao, RequestApi request) {
        transacao.setDataSolicitacao(DataUtil.getHoje());
        transacao.setIdPagamento(response.getIdPagamento());
        transacao.setIdAutorizacao(response.getIdAutorizacao());
        transacao.setMensagem(response.getMensagem());

        if (response.getStatus().equals(EnumSituacaoTransacaoGetnet.AUTORIZADO.getSitaucao())) {
            Venda venda = transacao.getIdVenda();
            if (venda != null) {
                venda.getIdConta().setContaParcelaList(contaService.listarContaParcela(venda.getIdConta()));
                vendaService.darBaixa(venda);
            }
        }

        if (response.getMensagem() == null && response.getBoleto() != null) {
            transacao.setLinhaDigitavel(response.getBoleto().getLinhaDigitavel());
            transacao.setLinkBoleto(response.getBoleto().getUrlBoleto());
        }
        if (request.getCartao() != null) {
            transacao.setNomeCartao(request.getCartao().getNomeCartao());
            transacao.setNumeroCartao(StringUtil.finalDoCartao(request.getCartao().getNumeroCartao()));
        }

        return alterar(transacao);
    }

    public TransacaoGetnet adicionarEnviarTransacaoPendente(PagamentoSistemaDTO pagamentoSistemaDto) throws IOException {
        TransacaoGetnet transacao = new TransacaoGetnet();
        PagamentoSistema pagamentoSistema = new PagamentoSistema();
        pagamentoSistema.setPlano(pagamentoSistemaDto.getIndexPlano());
        pagamentoSistema.setRecorrenciaPagamento(pagamentoSistemaDto.getRecorrencia().name());
        pagamentoSistema.setTenatID(adHocTenant.getTenantID());
        pagamentoSistema.setValor(pagamentoSistemaDto.getPreco());
        pagamentoSistema = pagamentoSistemaService.salvar(pagamentoSistema);
        transacao.setIdPagamentoSistema(pagamentoSistema);
        transacao.setSituacao(EnumSituacaoTransacaoGetnet.PENDENTE.getSitaucao());
        transacao.setTenatID(adHocTenant.getTenantID());
        switch (pagamentoSistemaDto.getTipoPagamento()) {
            case "BB":
                return pagamentoBoleto(transacao, pagamentoSistemaDto);
            case "CC":
                CartaoGetnetDTO cartao = new CartaoGetnetDTO();
                String anoValidade = DataUtil.getAno(pagamentoSistemaDto.getCartao().getDataValidade()).toString();

                cartao.setAnoValidade(anoValidade.substring(2, 4));
                cartao.setCvv(pagamentoSistemaDto.getCartao().getCvv().toString());
                cartao.setMesValidade(DataUtil.getMes(pagamentoSistemaDto.getCartao().getDataValidade()).toString());
                cartao.setNomeCartao(pagamentoSistemaDto.getCartao().getNome());
                cartao.setNumeroCartao(pagamentoSistemaDto.getCartao().getNumero());
                return pagamentoCredito(transacao, cartao, pagamentoSistemaDto);
            default:
                return null;
        }
    }

    public TransacaoGetnet adicionarEnviarTransacaoPendente(Venda venda) {
        FormaPagamento fp = venda.getIdFormaPagamento();
        if (fp == null && !venda.getVendaFormaPagamentoList().isEmpty()) {
            fp = venda.getVendaFormaPagamentoList().get(0).getIdFormaPagamento();
        }
        if (fp == null || fp.getCodigoNfe() == null || !fp.getEnumNfe().isPermiteLinkPagamento()) {
            return null;
        }
        TransacaoGetnet transacao = new TransacaoGetnet();

        transacao.setIdVenda(venda);
        transacao.setSituacao(EnumSituacaoTransacaoGetnet.PENDENTE.getSitaucao());
        transacao.setTenatID(venda.getTenatID());
        adicionar(transacao);

        if (StringUtils.isEmpty(venda.getTelefoneCliente())) {
            throw new CadastroException("Número de telefone do cliente não informado.", null);
        }
        Endereco end = venda.getIdCliente().getEndereco();
        if (EnumMeioDePagamento.BOLETO == fp.getEnumNfe() && (end == null || end.isBlank())) {
            throw new CadastroException("Endereço do cliente não informado. Não será possível gerar um boleto para esta venda.", null);
        }

        try {
            smsService.enviarLinkPagamento(venda.getTelefoneCliente(), getTransacaoId(transacao));
            return transacao;
        } catch (Exception ex) {
            Logger.getLogger(TransacaoGetnetService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static String getTransacaoId(TransacaoGetnet transacao) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return AESUtil.encrypt(transacao.getId().toString(), SECRET_KEY);
    }

    private static String getFoto(Documento doc) {
        if (doc == null || doc.getDocumentoAnexoList().isEmpty()) {
            return null;
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(doc.getDocumentoAnexoList().get(0).readFromFile());
    }

    public List<ItemVendaDTO> itensVenda(Venda venda) {
        List<ItemVendaDTO> listaItens = new ArrayList<>();

        listaItens.addAll(repositorio.listarVendaProduto(venda).stream()
                .map(vp -> {
                    ItemVendaDTO item = new ItemVendaDTO();
                    item.setIdProdutoServico(new ProdutoServicoDTO(vp.getDadosProduto().getIdProduto()));
                    item.setQuantidade(vp.getDadosProduto().getQuantidade());
                    item.getIdProdutoServico().setFoto(getFoto(vp.getDadosProduto().getIdProduto().getIdDocumento()));
                    return item;
                })
                .collect(Collectors.toList()));

        listaItens.addAll(repositorio.listarVendaServico(venda).stream()
                .map(vs -> {
                    ItemVendaDTO item = new ItemVendaDTO();
                    item.setIdProdutoServico(new ProdutoServicoDTO(vs.getIdServico()));
                    item.setQuantidade(1d);
                    item.getIdProdutoServico().setFoto(getFoto(vs.getIdServico().getIdDocumento()));
                    return item;
                })
                .collect(Collectors.toList()));

        return listaItens;
    }

    public TransacaoGetnet buscarPorMd(String md) {
        return repositorio.buscarPorMd(md);
    }

    public TransacaoGetnet atualizarSituacaoTransacaoPagamento(TransacaoGetnet transacao, String status, String obs) {
        switch (status) {
            case "A":
                transacao.setSituacao(EnumSituacaoTransacao.APROVADO.getSituacao());
                break;
            case "R":
                transacao.setSituacao(EnumSituacaoTransacao.ANALISE.getSituacao());
                break;
            case "P":
                transacao.setSituacao(EnumSituacaoTransacao.APROVADO.getSituacao());
                break;
            case "N":
                transacao.setSituacao(EnumSituacaoTransacao.RECUSADO.getSituacao());
                transacao.setMensagem(obs);
                break;
            case "C":
                transacao.setSituacao(EnumSituacaoTransacao.CANCELADO.getSituacao());
                transacao.setMensagem(obs);
                break;
            default:
                break;
        }
        return salvar(transacao);
    }

    public boolean enviarTransacaoPendente(Venda venda) {
        try {
            smsService.enviarLinkPagamento(venda.getTelefoneCliente(), getTransacaoId(venda.getIdTransacao()));
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TransacaoGetnetService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private Integer getProximoPagamento() {
        return repositorio.quantidadePagamentoSistema() + 1;
    }

    public List<ItemVendaDTO> itensPagamentoMensalidade(PagamentoMensalidade pm) {
        return pm.getPagamentoMensalidadeModuloList().stream()
                .map(pmm -> new ItemVendaDTO(new ProdutoServicoDTO(pmm), pmm.getIdModulo().getValorMensalidade()))
                .collect(Collectors.toList());
    }

}
