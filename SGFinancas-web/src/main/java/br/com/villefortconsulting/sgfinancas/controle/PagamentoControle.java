package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CartaoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemVendaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.BoletoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.TransacaoGetnetService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTransacaoGetnet;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BoletoService boletoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private VendaService vendaService;

    @EJB
    private TransacaoGetnetService transacaoGetnetService;

    private String statusEmpresa;

    private Venda venda;

    private List<VendaProduto> produtoList;

    private List<VendaServico> servicoList;

    private List<ItemVendaDTO> itensList;

    private TransacaoGetnet transacao;

    private CartaoGetnetDTO cartao;

    private Empresa empresa;

    private String id;

    private EnumMeioDePagamento formaSelecionada;

    @PostConstruct
    @PostActivate
    public void iniciar() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        id = request.getParameter("id");
        if (id != null) {
            transacao = transacaoGetnetService.buscarAES(id);
        }
        if (id == null || transacao == null) {
            redirect("/link/invalido.xhtml");
            return;
        }

        Venda tVenda = transacao.getIdVenda();
        PagamentoMensalidade pm = null;
        if (transacao.getIdPagamentoSistema() != null && !transacao.getPagamentoMensalidadeList().isEmpty()) {
            pm = transacao.getPagamentoMensalidadeList().get(transacao.getPagamentoMensalidadeList().size() - 1);
        }
        if (tVenda != null && (tVenda.getIdConta().getSituacao().equals("PG") || tVenda.getIdConta().getSituacao().equals("PP"))) {
            createFacesErrorMessage("A venda selecionada possui pagamento realizado.");
        } else if (pm != null && pm.getDataPagamento() != null) {
            createFacesErrorMessage("O pagamento selecionado já foi pago.");
        }

        String urlRetorno = "/link/retorno.xhtml";
        String urlPagamento = "/link/pagamento.xhtml";
        if (!EnumSituacaoTransacaoGetnet.PENDENTE.getSitaucao().equals(transacao.getSituacao()) && !request.getRequestURI().endsWith(urlRetorno)) {
            redirect(urlRetorno + "?id=" + id);
            return;
        } else if (EnumSituacaoTransacaoGetnet.PENDENTE.getSitaucao().equals(transacao.getSituacao())) {
            if (transacao.getLinkBoleto() != null && !request.getRequestURI().endsWith(urlRetorno)) {
                redirect(urlRetorno + "?id=" + id);
                return;
            } else if (!request.getRequestURI().endsWith(urlPagamento) && transacao.getLinkBoleto() == null) {
                redirect(urlPagamento + "?id=" + id);
                return;
            }
        }
        if (transacao.getIdVenda() != null) {
            empresa = empresaService.buscar(Integer.parseInt(transacao.getIdVenda().getTenatID()));
            itensList = transacaoGetnetService.itensVenda(transacao.getIdVenda());
        } else if (pm != null) {
            empresa = transacao.getPagamentoMensalidadeList().get(0).getIdEmpresa();
            itensList = transacaoGetnetService.itensPagamentoMensalidade(pm);
        } else {
            createFacesErrorMessage("Link de pagamento inválido");
            return;
        }
        cartao = new CartaoGetnetDTO();
        if (transacao.getIdVenda() != null && transacao.getIdVenda().getIdFormaPagamento() != null) {
            formaSelecionada = transacao.getIdVenda().getIdFormaPagamento().getEnumNfe();
        } else if (transacao.getLinkBoleto() != null) {
            formaSelecionada = EnumMeioDePagamento.BOLETO;
        } else {
            formaSelecionada = EnumMeioDePagamento.CARTAO_DE_CREDITO;
        }
    }

    public void validaTransacaoDebito() {
        if (formaSelecionada == null || formaSelecionada != EnumMeioDePagamento.CARTAO_DE_DEBITO || transacao == null) {
            redirect("/link/invalido.xhtml");
        }
    }

    public void processaRetornoDebito() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String md = request.getParameter("MD");
        String paRes = request.getParameter("PaRes");

        try {
            transacao = transacaoGetnetService.finalizacaoDebito(transacaoGetnetService.buscarPorMd(md), paRes);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public String realizarPagamento() {
        try {
            switch (formaSelecionada) {
                case CARTAO_DE_CREDITO:
                    transacao = transacaoGetnetService.pagamentoCredito(transacao, cartao, null);
                    break;
                case CARTAO_DE_DEBITO:
                    transacao = transacaoGetnetService.pagamentoDebito(transacao, cartao);
                    return "frameDebito.xhtml";
                case BOLETO:
                    transacao = transacaoGetnetService.pagamentoBoleto(transacao, null);
                    break;
                default:
                    if (formaSelecionada.isPermiteLinkPagamento()) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "A forma de pagamento selecionada no link ({0}) n\u00e3o foi configurada", formaSelecionada.name());
                    }
                    createFacesErrorMessage("Forma de pagamento inválida!");
                    return "pagamento.xhtml?id=" + id;
            }

            createFacesSuccessMessage("Transação realizado com sucesso");
            return "retorno.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Erro ao salvar a transação.", ex);
            createFacesErrorMessage(ex.getMessage());
            return "pagamento.xhtml?id=" + id;
        }
    }

    public void baixarBoleto() {
        try {
            boletoService.gerarBoletoPara(transacao);
        } catch (Exception ex) {
            Logger.getLogger(PagamentoControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível gerar o download do boleto.");
        }
    }

    public String retornarDataHora() {
        return DataUtil.dataHoraToString(transacao.getDataSolicitacao());
    }

    public Double totalCompra() {
        PagamentoMensalidade pm = null;
        if (transacao.getIdPagamentoSistema() != null && !transacao.getPagamentoMensalidadeList().isEmpty()) {
            pm = transacao.getPagamentoMensalidadeList().get(transacao.getPagamentoMensalidadeList().size() - 1);
        }
        if (transacao.getIdVenda() != null) {
            Double desconto = NumeroUtil.somar(transacao.getIdVenda().getValorDesconto());
            return NumeroUtil.somar((transacao.getIdVenda().getValor() - desconto), transacao.getIdVenda().getValorFrete());
        } else if (pm != null) {
            return itensList.stream()
                    .mapToDouble(ItemVendaDTO::getValorTotal)
                    .sum();
        }

        return 0d;
    }

    private boolean redirect(String url) {
        try {
            ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse()).sendRedirect(url);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public String getTermUrl() {
        return SystemPreferencesUtil.getProp("sec.getnet.termUrl", "https://sgfinancas.com/link/retornoDebito.xhtml");
    }

    public List<EnumMeioDePagamento> getListaMeioPagamento() {
        return Arrays.asList(EnumMeioDePagamento.values()).stream()
                .filter(EnumMeioDePagamento::isPermiteLinkPagamento)
                .collect(Collectors.toList());
    }

    public Double getValor() {
        if (transacao.getIdVenda() != null) {
            return transacao.getIdVenda().getValor();
        }
        return transacao.getIdPagamentoSistema().getValor();
    }

}
