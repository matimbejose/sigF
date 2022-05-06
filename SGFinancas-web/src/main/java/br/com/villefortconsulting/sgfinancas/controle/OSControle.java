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
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOS;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.PrimeFaces;

@Named("osControle")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class OSControle extends VendaControleBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                vendaService::quantidadeRegistrosFiltradosOS,
                vendaService::getListaFiltradaOS,
                filter -> {
                    filter.setUsuarioLogado(getUsuarioLogado());
                    filter.setAtivo("S");
                });
        tiposSelecionados = new ArrayList<>();
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    public void doPrintOSVeiculo() {
        try {
            gerarPdf(relatorioService.gerarImpressaoOSVeiculo(vendaSelecionada), "Ordem de Serviço");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public void doPrintOS() {
        try {
            gerarPdf(relatorioService.gerarImpressaoOS(vendaSelecionada), "Ordem de Serviço");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public void doPrintGarantia() {
        try {
            gerarPdf(relatorioService.gerarImpressaoGarantia(vendaSelecionada), "Termo de Garantia");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public void doPrintPermanencia() {
        try {
            gerarPdf(relatorioService.gerarImpressaoPermanencia(vendaSelecionada), "Declaração de permanencia");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public String addOS() {
        initItensVendaDTO();
        listaAnexos = new ArrayList<>();
        viewOnly = false;
        cleanCache();
        vendaSelecionada.setIdUsuarioVendedor(getUsuarioLogado());
        vendaSelecionada.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
        vendaSelecionada.setValorDesconto(0d);
        listFormaDePagamento = new ArrayList<>();
        formaDePagamentoSelecionada = new FormaPagamento();
        vendaSelecionada.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao());
        return "cadastroOS.xhtml";
    }

    @Override
    public String getPaginaCadastro() {
        return "cadastroOS.xhtml";
    }

    public String doFinishAdd() {
        try {
            itemVendaSelecionado = new ItemVendaDTO();
            preencherFormaPagamento();
            populateVendaProdServ();
            if ((!listVendaProduto.isEmpty() || !listVendaServico.isEmpty() || !listVendaOS.isEmpty()) || !(vendaSelecionada.getListParcelaDTO() == null || vendaSelecionada.getListParcelaDTO().isEmpty())) {
                vendaSelecionada.setVendaProdutoList(listVendaProduto);
                vendaSelecionada.setVendaServicoList(listVendaServico);
                vendaSelecionada.setVendaItemOrdemDeServicoList(listVendaOS);
                vendaSelecionada.setVendaFormaPagamentoList(listFormaDePagamento);
                vendaSelecionada.setListParcelaDTO(getListParcelasPorFormasPagamento());
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
                vendaSelecionada.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
                if (vendaSelecionada.getStatusOS() == null) {
                    vendaSelecionada.setStatusOS(EnumStatusOS.AGUARDANDO_EXECUCAO.getCodigo());
                }
                ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
                vendaSelecionada.setIdPlanoConta(parametroSistemaSelecionado.getIdPlanoContaVendaPadrao());
                if (vendaSelecionada.getIdPlanoConta() == null) {
                    createFacesErrorMessage("Paramentro de plano de contas padrão não foi definido. Não será possivel salvar a ordem de serviço.");
                    return "cadastroOS.xhtml";
                }
                vendaService.salvar(vendaSelecionada);

                createFacesSuccessMessage("Ordem de serviço salva com sucesso!");
                return "listaOS.xhtml";
            } else {
                createFacesErrorMessage("Informe ao menos um produto/serviço para criar a ordem de serviço");
                return "cadastroOS.xhtml";
            }
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Erro ao salvar ordem de serviço.", ex);
            return "cadastroOS.xhtml";
        }

    }

    public String salvarVendaOrcamento(String tipo) {
        try {
            doFinishAdd();
            doStartAlterar();
            preencherFormaPagamento();
            formaDePagamentoSelecionada = vendaSelecionada.getIdFormaPagamento();
            vendaSelecionada.setStatusNegociacao(tipo.equals("venda") ? EnumTipoVenda.VENDA.getSituacao() : EnumTipoVenda.ORDEM_SERVICO.getSituacao());
            vendaSelecionada.setDataFinalizacao(new Date());
            if (vendaSelecionada.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
                vendaSelecionada.setListParcelaDTO(getListParcelasPorFormasPagamento());
                if (vendaSelecionada.getListParcelaDTO().isEmpty()) {
                    throw new CadastroException("Ordem de serviço não possui forma de pagamento e ou condição de pagamento definida", null);
                }

            }
            if (vendaSelecionada.getIdContaBancaria() == null) {
                ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
                ContaBancaria cb = parametroSistemaSelecionado.getAppContaBancariaPadrao();
                if (cb == null && contaBancariaService.listar().size() == 1) {
                    cb = contaBancariaService.listar().get(0);
                }
                if (cb == null) {
                    throw new CadastroException("Não foi definida uma conta bancária padrão, não será possível gerar a venda.", null);
                }
                vendaSelecionada.setIdContaBancaria(cb);
            }
            vendaSelecionada.setListParcelaDTO(getListParcelasPorFormasPagamento());
            vendaSelecionada.setVendaProdutoList(listVendaProduto);
            vendaSelecionada.setVendaServicoList(listVendaServico);
            vendaSelecionada.setVendaItemOrdemDeServicoList(listVendaOS);
            vendaSelecionada.setVendaFormaPagamentoList(listFormaDePagamento);

            boolean vendaProdutoIsEmpty = listVendaProduto.isEmpty();
            boolean vendaServicoIsEmpty = listVendaServico.isEmpty();
            boolean vendaItemOrdemDeServicoIsEmpty = listVendaOS.isEmpty();

            if ((!vendaProdutoIsEmpty || !vendaServicoIsEmpty || !vendaItemOrdemDeServicoIsEmpty)) {
                vendaService.validaQuantidadeEmEstoque(vendaSelecionada);
                if (vendaSelecionada.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
                    vendaService.aprovarOrcamento(vendaSelecionada, formaDePagamentoSelecionada);
                }
                createFacesSuccessMessage("Venda gerada com sucesso!");
                return "/restrito/os/listaOS.xhtml";
            } else {
                throw new CadastroException("Informe ao menos um produto para efetuar a venda", null);
            }
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoControle.class.getName()).log(Level.SEVERE, null, ex);
            boolean venda = !listVendaProduto.isEmpty() || !listVendaServico.isEmpty();
            String mensagem = "Não foi possível salvar a ";
            if (venda) {
                mensagem += "venda";
            }
            createFacesErrorMessage(mensagem + ". " + ex.getMessage());
        }
        return "/restrito/os/cadastroOS.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaOS.xhtml";
    }

    public String nomearStatusOS(String status) {
        if (status == null) {
            return "";
        }
        return EnumStatusOS.retornaEnumSelecionado(status).getDescricao();
    }

    public void doFinishCancelarOS() {
        try {
            vendaSelecionada.setDataCancelamento(new Date());
            vendaSelecionada.setStatusOS(EnumStatusOS.CANCELADA.getCodigo());
            vendaService.alterar(vendaSelecionada);
            closeModal();
            PrimeFaces.current().executeScript("window.parent.PF(`tbl`).filter()");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage("Não foi possível cancelar esta ordem de serviço.");
        }

    }

    public String doStartOS() {
        try {
            vendaSelecionada.setStatusOS(EnumStatusOS.EM_EXECUCAO.getCodigo());
            vendaSelecionada.setDataInicioExecucao(new Date());
            vendaService.alterar(vendaSelecionada);
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível iniciar a produção desta ordem de serviço.");
        }
        return "listaOS.xhtml";
    }

    public Boolean podeReabrirOS(Venda os) {
        return vendaService.podeReabrirOS(os);
    }

    public String reabrirOS() {
        try {
            vendaSelecionada.setStatusOS(EnumStatusOS.AGUARDANDO_EXECUCAO.getCodigo());
            vendaService.alterar(vendaSelecionada);
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível reabri a ordem de serviço.");
        }
        return "listaOS.xhtml";
    }

    public String getNumeroOrcamento(Venda orcamento) {
        String n = "OS";
        return n + orcamento.getId();
    }

    public List<FormaPagamento> getFullListFormasDePagamento() {
        return formaPagamentoService.listarFormaDePagamentoAtivo();
    }

    public void addListaParcelaPorFormaDePagamento() {
        if (formaDePagamentoSelecionada == null) {
            formaDePagamentoSelecionada = new FormaPagamento();
        }
        boolean jaTemFormaDePagamento = formaDePagamentoSelecionada.getId() == null
                || listFormaDePagamento.stream()
                        .anyMatch(vfp -> vfp.getIdFormaPagamento().equals(formaDePagamentoSelecionada));

        if (!jaTemFormaDePagamento) {
            VendaFormaPagamento vfp = new VendaFormaPagamento();
            vfp.setDesconto(0d);
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

    public void enviarOSPorEmail() {
        try {
            FormaPagamento fp = null;
            fp = vendaSelecionada.getIdFormaPagamento();
            vendaService.enviarOSPorEmail(vendaSelecionada, fp, getNumeroOrcamento());
            createFacesInfoMessage("E-mail enviado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OSControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao enviar o email");
        }
    }

    public void enviarOSMecanicaPorEmail() {
        try {
            FormaPagamento fp = null;
            fp = vendaSelecionada.getIdFormaPagamento();
            vendaService.enviarOSMecanicaPorEmail(vendaSelecionada, fp, getNumeroOrcamento());
            createFacesInfoMessage("E-mail enviado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OSControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao enviar o email");
        }
    }

    public void enviarGarantiaOSPorEmail() {
        try {
            vendaService.enviarGarantiaPorEmail(vendaSelecionada);
            createFacesInfoMessage("E-mail enviado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OSControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao enviar o email");
        }
    }

    public void enviarDeclaracaoPermanenciaPorEmail() {
        try {
            vendaService.enviarDeclaracaoPermanenciaPorEmail(vendaSelecionada);
            createFacesInfoMessage("E-mail enviado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(OSControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao enviar o email");
        }
    }

    public void doStartCancelarOS() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("modal", true);
        options.put("width", 600);
        options.put("height", 220);
        options.put("closeable", true);
        showModal("modals/cancelamento.xhtml", options);
    }

    public Boolean verificaTipoDeCliente(String particular) {
        if (particular.equals("S")) {
            vendaSelecionada.setIdVendaSeguradora(null);
            return true;
        }
        if (vendaSelecionada.getIdVendaSeguradora() == null) {
            vendaSelecionada.setIdVendaSeguradora(new VendaSeguradora());
        }
        return false;
    }

    public void doShowPrintModal() {
        HashMap<String, Object> options = new HashMap<>();
        options.put("height", "500px");
        options.put("width", "300px");
        showModal("modals/impressao.xhtml", options);
    }

    public void doPrintForClient(FormaPagamento formaDePagamento) {
        try {
            gerarPdf(relatorioService.gerarImpressaoOSCusto(vendaSelecionada, formaDePagamento), "Ordem de Serviço com valores");
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

}
