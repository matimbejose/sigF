package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.controle.apoio.VendaControleBase;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaSeguradora;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemVendaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import br.com.villefortconsulting.util.operator.In;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class OrcamentoControle extends VendaControleBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> origem = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        origem.add(EnumOrigemVenda.SITE.getOrigem());
        origem.add(EnumOrigemVenda.APP.getOrigem());
        model = new VillefortDataModel<>(
                filtro,
                vendaService::quantidadeRegistrosFiltradosOrcamento,
                vendaService::getListaFiltradaOrcamento,
                filter -> {
                    filter.setUsuarioLogado(getUsuarioLogado());
                    filter.setOrigem(In.fromList(origem));
                    filter.setAtivo("S");
                });
        tiposSelecionados = new ArrayList<>();
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    public void doPrintForClient(FormaPagamento formaDePagamento) {
        try {
            gerarPdf(relatorioService.gerarImpressaoOrcamentoParaCliente(vendaSelecionada, formaDePagamento), "Orçamento");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public void doStartPrintForClient() {
        doPrintForClient(vendaSelecionada != null ? vendaSelecionada.getIdFormaPagamento() : null);
    }

    public String addOrcamento() {
        initItensVendaDTO();
        listaAnexos = new ArrayList<>();
        viewOnly = false;
        cleanCache();
        vendaSelecionada.setIdUsuarioVendedor(getUsuarioLogado());
        vendaSelecionada.setStatusNegociacao(EnumTipoVenda.ORCAMENTO.getSituacao());
        vendaSelecionada.setValorDesconto(0d);
        vendaSelecionada.setDataVenda(new Date());
        listFormaDePagamento = new ArrayList<>();
        formaDePagamentoSelecionada = new FormaPagamento();
        vendaSelecionada.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao());
        return "cadastroOrcamento.xhtml";
    }

    @Override
    public String getPaginaCadastro() {
        return "cadastroOrcamento.xhtml";
    }

    public String doFinishAdd() {
        try {
            if (vendaSelecionada.getIdUsuarioVendedor() == null) {
                vendaSelecionada.setIdUsuarioVendedor(getUsuarioLogado());
            }

            itemVendaSelecionado = new ItemVendaDTO();
            preencherFormaPagamento();
            populateVendaProdServ();
            if ((!listVendaProduto.isEmpty() || !listVendaServico.isEmpty() || !listVendaOS.isEmpty()) || (vendaSelecionada.getListParcelaDTO() == null || !vendaSelecionada.getListParcelaDTO().isEmpty())) {
                vendaSelecionada.setVendaProdutoList(listVendaProduto);
                vendaSelecionada.setVendaServicoList(listVendaServico);
                vendaSelecionada.setVendaItemOrdemDeServicoList(listVendaOS);
                vendaSelecionada.setVendaFormaPagamentoList(listFormaDePagamento);
                calcularDescontoTotal();
                calcularValorTotalLimparFormaPagamento();
                vendaSelecionada.setDataVenda(new Date());
                vendaSelecionada.setOrigem(EnumOrigemVenda.SITE.getOrigem());
                if (vendaSelecionada.getIdDocumento() != null) {
                    vendaSelecionada.setIdDocumento(documentoService.alterar(vendaSelecionada.getIdDocumento()));
                }
                if (listaAnexos != null) {
                    Documento doc;
                    if (vendaSelecionada.getIdDocumento() == null) {
                        doc = documentoService.criarDocumento(getUsuarioLogado(), "Imagens do produto");
                    } else {
                        doc = documentoService.buscar(vendaSelecionada.getIdDocumento().getId());
                    }
                    documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), listaAnexos);
                    vendaSelecionada.setIdDocumento(doc);

                }
                ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
                vendaSelecionada.setIdPlanoConta(parametroSistemaSelecionado.getIdPlanoContaVendaPadrao());
                if (vendaSelecionada.getIdPlanoConta() == null) {
                    createFacesErrorMessage("Paramentro de plano de contas padrão não foi definido. Não será possivel salvar venda.");
                    return "cadastroVenda.xhtml";
                }
                vendaService.salvar(vendaSelecionada);

                createFacesSuccessMessage("Orçamento salvo com sucesso!");
                return "listaOrcamento.xhtml";
            } else {
                createFacesErrorMessage("Informe ao menos um produto para criar o orçamento");
                return "cadastroOrcamento.xhtml";
            }
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Erro ao salvar orçamento.", ex);
            return "cadastroOrcamento.xhtml";
        }

    }

    public String salvarVendaOrcamento(String tipo) {
        if (vendaService.temVendaPorIdOrcamentoOsOrigem(vendaSelecionada)) {
            createFacesErrorMessage("Orçamento já aprovado.");
            return "cadastroOrcamento.xhtml";
        }
        if (vendaSelecionada.getIdFormaPagamento() == null) {
            List<VendaFormaPagamento> listaFormasPagamento = vendaService.listarVendaFormaPagamento(vendaSelecionada);
            if (listaFormasPagamento.isEmpty()) {
                createFacesErrorMessage("Não é possível aprovar um orçamento sem uma forma de pagamento definida.");
                return "cadastroOrcamento.xhtml";
            } else if (listaFormasPagamento.size() == 1) {
                vendaSelecionada.setIdFormaPagamento(listaFormasPagamento.get(0).getIdFormaPagamento());
            } else {
                createFacesErrorMessage("Selecione uma das formas de pagamento antes de salvar.");
                return "cadastroOrcamento.xhtml";
            }
        }
        doFinishAdd();
        doStartAlterar();
        preencherFormaPagamento();
        formaDePagamentoSelecionada = vendaSelecionada.getIdFormaPagamento();
        vendaSelecionada.setStatusNegociacao(tipo.equals("venda") ? EnumTipoVenda.VENDA.getSituacao() : EnumTipoVenda.ORDEM_SERVICO.getSituacao());
        return salvarVendaOrcamento();
    }

    public String salvarVendaOrcamento() {
        try {
            if (vendaSelecionada.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
                vendaSelecionada.setListParcelaDTO(getListParcelasPorFormasPagamento());
                if (vendaSelecionada.getListParcelaDTO().isEmpty()) {
                    throw new CadastroException("Orçamento não possui forma de pagamento e ou condição de pagamento definida", null);
                }

                if (vendaSelecionada.getIdContaBancaria() == null) {
                    ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
                    ContaBancaria cb = parametroSistemaSelecionado.getAppContaBancariaPadrao();
                    if (cb == null && contaBancariaService.listar().size() == 1) {
                        cb = contaBancariaService.listar().get(0);
                    }
                    if (cb == null) {
                        throw new CadastroException("Não foi definida uma conta bancária padrão, não será possível aprovar o orçamento.", null);
                    }
                    vendaSelecionada.setIdContaBancaria(cb);
                }
            }
            vendaSelecionada.setListParcelaDTO(getListParcelasPorFormasPagamento());
            vendaSelecionada.setVendaProdutoList(listVendaProduto);
            vendaSelecionada.setVendaServicoList(listVendaServico);
            vendaSelecionada.setVendaItemOrdemDeServicoList(listVendaOS);
            vendaSelecionada.setVendaFormaPagamentoList(listFormaDePagamento);

            boolean vendaProdutoIsEmpty = listVendaProduto.isEmpty();
            boolean vendaServicoIsEmpty = listVendaServico.isEmpty();
            boolean vendaItemOrdemDeServicoIsEmpty = listVendaOS.isEmpty();

            String tipoAprovacao;
            if (!vendaProdutoIsEmpty || !vendaServicoIsEmpty || !vendaItemOrdemDeServicoIsEmpty) {
                vendaService.validaQuantidadeEmEstoque(vendaSelecionada);
                if (vendaSelecionada.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
                    tipoAprovacao = "Venda";
                    vendaService.aprovarOrcamento(vendaSelecionada, formaDePagamentoSelecionada);
                } else {
                    tipoAprovacao = "Ordem de serviço";
                    vendaService.aprovarOS(vendaSelecionada);
                }
                createFacesSuccessMessage(tipoAprovacao + " gerada com sucesso!");
                return "/restrito/orcamento/listaOrcamento.xhtml";
            } else {
                createFacesErrorMessage("Informe ao menos um produto para efetuar o orçamento");
                return "cadastroOrcamento.xhtml";
            }
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroOrcamento.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoControle.class.getName()).log(Level.SEVERE, null, ex);
            boolean venda = !listVendaProduto.isEmpty() || !listVendaServico.isEmpty();
            boolean os = !listVendaOS.isEmpty();
            String mensagem = "Não foi possível salvar a ";
            if (venda) {
                mensagem += "venda";
            }
            if (os) {
                if (venda) {
                    mensagem += " e a ";
                }
                mensagem += "ordem de serviço";
            }
            createFacesErrorMessage(mensagem + ". " + ex.getMessage());
            return "cadastroOrcamento.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "listaAuditoriaOrcamento.xhtml";
    }

    public String doFinishExcluir() {
        try {
            vendaSelecionada.setDataCancelamento(new Date());
            vendaSelecionada.setSituacao("C");
            vendaService.alterar(vendaSelecionada);
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível excluir este orçamento.");
        }
        return "listaVenda.xhtml";
    }

    public String getNumeroOrcamento(Venda orcamento) {
        String n = "";

        if (vendaService.vendaPossuiProduto(orcamento)) {
            n += "P";
        }
        if (vendaService.vendaPossuiServico(orcamento)) {
            n += "S";
        }
        if (vendaService.vendaPossuiOrdemDeServico(orcamento)) {
            n += "O";
        }

        return n + orcamento.getId();
    }

    public List<EnumTipoVenda> getTiposDeOrcamento() {
        List<EnumTipoVenda> lista = new ArrayList<>();

        lista.add(EnumTipoVenda.ORCAMENTO);
        lista.add(EnumTipoVenda.ORCAMENTO_APROVADO);
        lista.add(EnumTipoVenda.ORCAMENTO_REJEITADO);

        return lista;
    }

    public List<FormaPagamento> getFullListFormasDePagamento() {
        List<FormaPagamento> lista = formaPagamentoService.listarFormaDePagamentoAtivo();
        if (isApproved()) {// Listar somente as formas de pagamento já salvas
            return lista.stream()
                    .filter(fp -> listFormaDePagamento.stream().anyMatch(forma -> forma.getIdFormaPagamento().equals(fp)))
                    .collect(Collectors.toList());
        }
        return lista;
    }

    public void addListaParcelaPorFormaDePagamento() {
        if (formaDePagamentoSelecionada == null) {
            formaDePagamentoSelecionada = new FormaPagamento();
        }
        boolean jaTemFormaDePagamento = formaDePagamentoSelecionada.getId() == null
                || listFormaDePagamento.stream()
                        .anyMatch(vfp -> vfp.getIdFormaPagamento().equals(formaDePagamentoSelecionada));

        if (!jaTemFormaDePagamento && !isApproved()) {
            VendaFormaPagamento vfp = new VendaFormaPagamento();
            vfp.setDesconto(getDescontoTotalDouble());
            vfp.setIdFormaPagamento(formaDePagamentoSelecionada);
            vfp.setIdVenda(vendaSelecionada);
            vfp.setFormaEscolhida("N");
            //mudança realizada para trabalhar apenas com uma forma de pagamento
            listFormaDePagamento.clear();

            listFormaDePagamento.add(vfp);
            vendaSelecionada.setFormaPagamento(vfp.getCondicaoPagamento());
            vendaSelecionada.setIdFormaPagamento(vfp.getIdFormaPagamento());
        } else if (formaDePagamentoSelecionada.getId() != null) {
            VendaFormaPagamento vfp = listFormaDePagamento
                    .stream()
                    .filter(fp -> fp.getIdFormaPagamento().equals(formaDePagamentoSelecionada))
                    .collect(Collectors.toList()).get(0);

            vendaSelecionada.setFormaPagamento(vfp.getCondicaoPagamento());
            vendaSelecionada.setIdFormaPagamento(vfp.getIdFormaPagamento());
        }
        setPageDataTable();
    }

    private String getUpdateCamposDePagamentoGeral() {
        String update = "form:pnlTotais, form:pnlParcelado, form:gridFormaPagamento";
        if (!listFormaDePagamento.isEmpty()) {
            update += ", form:gridFormaPagamento:0:tblParcelaPorFormaDePagamento";
        }
        return update;
    }

    public String getUpdateCamposDePagamentoProduto() {
        return "tblProdutos, " + getUpdateCamposDePagamentoGeral();
    }

    public String getUpdateCamposDePagamentServico() {
        return "tblServico, " + getUpdateCamposDePagamentoGeral();
    }

    public String getUpdateCamposDePagamentOS() {
        return /*"tblOS, " + */ getUpdateCamposDePagamentoGeral();
    }

    public boolean isApproved() {
        return !vendaSelecionada.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO.getSituacao());
    }

    public void enviarOrcamentoPorEmail() {
        try {
            FormaPagamento fp = null;
            fp = vendaSelecionada.getIdFormaPagamento();
            vendaService.enviarOrcamentoPorEmail(vendaSelecionada, fp, getNumeroOrcamento());
            createFacesInfoMessage("E-mail enviado com sucesso!");
        } catch (CadastroException | EmailException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao enviar o email");
        }
    }

    public void enviarOrcamentoVeiculoPorEmail() {
        try {
            FormaPagamento fp = null;
            fp = vendaSelecionada.getIdFormaPagamento();
            vendaService.enviarOrcamentoVeiculoPorEmail(vendaSelecionada, fp, getNumeroOrcamento());
            createFacesInfoMessage("E-mail enviado com sucesso!");
        } catch (CadastroException | EmailException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao enviar o email");
        }
    }

    public Boolean verificaTipoDeCliente(String particular) {
        if (particular.equals("S")) {
            vendaSelecionada.setIdVendaSeguradora(null);
            return true;
        }
        if (vendaSelecionada.getIdVendaSeguradora() == null && ehOficina(empresaService.getEmpresa())) {
            vendaSelecionada.setIdVendaSeguradora(new VendaSeguradora());
        }
        return false;
    }

    public void doShowPrintModal() {
        HashMap<String, Object> options = new HashMap<>();
        options.put("height", "250px");
        showModal("modals/impressao.xhtml", options);
    }

}
