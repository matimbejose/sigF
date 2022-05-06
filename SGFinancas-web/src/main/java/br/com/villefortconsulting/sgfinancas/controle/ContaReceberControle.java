package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ContaControleBase;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CardContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.BoletoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.EmailService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.NFSService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.util.EnumContentType;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoListagemConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaReceberControle extends ContaControleBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BoletoService boletoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private NFSService nfsService;

    @EJB
    private EmailService emailService;

    @Inject
    private NotaFiscalServicoControle notaFiscalServicoControle;

    private Cliente clienteSelecionado;

    private LazyDataModel<ContaParcela> modelAntecipacao;

    private EstatisticaContaDTO estisticaContaDTO;

    private List<ContaParcela> parcelasSelecionadas;

    private Map<Cliente, String> subtituloPorCliente;

    private List<ContaParcela> contasSelecionadas;

    private EmailDTO emailDTO;

    @Override
    public void cleanCache() {
        clienteSelecionado = null;
    }

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                contaService::quantidadeParcelasFiltradas,
                contaService::getListaParcelaFiltrada,
                filter -> {
                    filter.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
                    filter.setTipoListagem(tipoListagem);
                    if (clienteSelecionado != null) {
                        filter.setCliente(clienteSelecionado);
                    }
                });
        modelAntecipacao = new VillefortDataModel<>(
                filtro,
                contaService::quantidadeParcelasAntecipacaoFiltradas,
                contaService::getListaParcelaAntecipacaoFiltrada,
                filter -> {
                    filter.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
                    filter.setTipoListagem(tipoListagem);
                    if (clienteSelecionado != null) {
                        filter.setCliente(clienteSelecionado);
                    }
                });
    }

    public Double totalParcelasAntecipacao() {
        return modelAntecipacao.getWrappedData().stream()
                .mapToDouble(ContaParcela::getValorTotal)
                .sum();
    }

    public String doStartCharge() {
        tipoListagem = "atraso";
        filtro.setTipoListagem(tipoListagem);
        subtituloPorCliente = new HashMap<>();
        filtro.setFormaPagamento(null);
        emailDTO = new EmailDTO();
        return "listaParaCobranca.xhtml";
    }

    public void clearClienteSelecionado() {
        clienteSelecionado = null;
    }

    public String doSelectClientForCharge(Cliente cliente) {
        clienteSelecionado = cliente;
        return "geraCobranca.xhtml";
    }

    public String doFinishCharge(String tipo) {
        switch (tipo) {
            case "email":
                if (StringUtils.isEmpty(emailDTO.getAssunto())) {
                    createFacesErrorMessage("Informe um assunto");
                }
                if (StringUtils.isEmpty(emailDTO.getRemetente())) {
                    createFacesErrorMessage("Informe um remetente");
                }
                if (StringUtils.isEmpty(emailDTO.getTituloMensagem())) {
                    createFacesErrorMessage("Informe um título");
                }
                if (contasSelecionadas.isEmpty()) {
                    createFacesErrorMessage("Selecione no mínimo uma parcela para gerar a cobrança");
                }
                if (existsErrorMessages()) {
                    return "geraCobranca.xhtml";
                }
                List<Usuario> usuarios = new ArrayList<>();
                Usuario user = new Usuario();
                user.setEmail(filtro.getCliente().getEmail());
                usuarios.add(user);
                Double valorTotal = contasSelecionadas.stream()
                        .mapToDouble(ContaParcela::getValor).sum();
                StringBuilder table = new StringBuilder();
                table.append("<table style=\"width: 100%;text-align: center;\">")
                        .append("<tr><th>Observação</th><th>Origem</th><th>Vencimento</th><th>Valor</th></tr>");
                contasSelecionadas.forEach(cp -> table
                        .append("<tr aria-hidden=\"true\"><td colspan=\"5\" style=\"background-color: #ccc;padding: 0;height: 1px;\"></td></tr>")
                        .append("<tr><td>")
                        .append(StringUtil.nullCoalesce("", cp.getObservacao()))
                        .append("</td><td>")
                        .append(mostraOrigem(cp.getIdConta()))
                        .append("</td><td>")
                        .append(DataUtil.dateToString(cp.getDataVencimento()))
                        .append("</td><td style=\"text-align: right;\">R$ ")
                        .append(NumeroUtil.formatarCasasDecimais(cp.getValor(), 2, false))
                        .append("</td></tr>"));
                table.append("<tr aria-hidden=\"true\"><td colspan=\"5\" style=\"background-color: #ccc;padding: 0;height: 1px;\"></td></tr>")
                        .append("<tr><td colspan=\"4\" style=\"text-align:right;\">Total:</td><td style=\"text-align: right;\">R$ ")
                        .append(NumeroUtil.formatarCasasDecimais(valorTotal, 2, false))
                        .append("</td></tr></table>");

                emailDTO.setDestinatarios(usuarios);
                String msg = emailDTO.getMensagem();
                emailDTO.setMensagem(msg + "<br><br><span>Contas em aberto:</span><br>" + table.toString());
                try {
                    emailService.enviarEmailMS(emailDTO, "S");
                    emailDTO.setMensagem(msg);
                    createFacesSuccessMessage("Mensagem enviada para " + user.getEmail());
                } catch (EmailException ex) {
                    createFacesErrorMessage("Não foi possível enviar o email");
                }
                break;
            case "lista":
                // converter para DTO
                // gerar um PDF
                break;
            default:
                return "geraCobranca.xhtml";
        }
        if (existsErrorMessages()) {
            return "geraCobranca.xhtml";
        }
        return "listaParaCobranca.xhtml";
    }

    public String mostrarAjudaRecebimentoParcial() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CONTA_RECEBER_PARCELA_PARCIAL.getChave()).getDescricao());
        return "cadastroContaReceber.xhtml";
    }

    public String mostrarAjudaCancelamento() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CANCELAMENTO_PARCELA_RECEBER.getChave()).getDescricao());
        return "cancelarParcela.xhtml";
    }

    @Override
    public void preencherEstatisticaDTO() {
        cardAtraso = contaService.buscarEstatisticasConta(filtro, EnumTipoListagemConta.ATRASO);
        cardRecebido = contaService.buscarEstatisticasConta(filtro, EnumTipoListagemConta.RECEBIDO);
        cardReceber = contaService.buscarEstatisticasConta(filtro, EnumTipoListagemConta.RECEBER);

        Long quantidadeTotal = cardReceber.getQuantidade() + cardRecebido.getQuantidade() + cardAtraso.getQuantidade();
        Double valorTotal = cardReceber.getValor() + cardRecebido.getValor() + cardAtraso.getValor();
        cardTotal = new CardContaDTO(valorTotal, quantidadeTotal);
        createPieModel2();
    }

    @Override
    public void calcularImpostos() {
        contaService.calcularImpostosParcelaPorCliente(contaParcelaSelecionada);
    }

    @Override
    public List<Conta> getListaConta() {
        return contaService.listarContaReceber();
    }

    public boolean contasRecebidas() {
        return contaSelecionada != null && "S".equals(contaSelecionada.getInformarPagamento());
    }

    public String doStartContaPorCliente() {
        telaAnterior = "contaPorCliente";

        return doStartAlterarConta();
    }

    public StreamedContent emitirBoleto() {
        try {
            Integer numeroVia = boletoService.buscarNumeroViaBoleto(contaParcelaSelecionada);
            contaParcelaSelecionada = boletoService.emitirBoletoParcela(contaParcelaSelecionada, empresaService.getEmpresa(), getUsuarioLogado(), numeroVia);

            return downloadBoleto();
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());

            return null;
        }
    }

    public StreamedContent downloadBoleto() {
        try {
            DocumentoAnexo fatura = documentoAnexoService.buscarUltimoAnexoDocumento(contaParcelaSelecionada.getIdBoleto().getIdDocumento());

            return FileUtil.downloadFile(fatura.readFromFile(), fatura.getContentType(), fatura.getNomeArquivo() + "." + EnumContentType.retornaExtensao(fatura.getContentType()));
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());

            return null;
        }
    }

    public boolean parcelaPossuiNFServico(ContaParcela contaParcela) {
        return contaService.parcelaPossuiNFServico(contaParcela);
    }

    public String salvarEEmitir(NotaFiscalServicoControle nfsControle) {
        String retornoAdd = doFinishAdd();
        if (!existsErrorMessages()) {
            return nfsControle.doStartAdd();
        } else {
            return retornoAdd;
        }
    }

    public String agruparEmNota() {
        if (filtro.getCliente() == null) {
            createFacesErrorMessage("É necessário selecionar um cliente para criar uma nota de serviço agrupada.");
            return "listaContaReceber.xhtml";
        } else {
            return "selecionaContasParaNota.xhtml";
        }
    }

    public String gerarNFSAgrupada() {
        NFS nota = new NFS();
        nota.setDataEmissao(DataUtil.getHoje());
        nota.setIdCliente(filtro.getCliente());
        nota.setContaParcelaList(parcelasSelecionadas);
        nota.setValorTotal(parcelasSelecionadas.stream()
                .mapToDouble(ContaParcela::getValor)
                .sum());
        if (nota.getValorTotal() > 0) {
            // Recalcular os impostos
            nota = nfsService.preencherNfsPorCliente(nota);
            notaFiscalServicoControle.setNfsSelecionado(nota);
            return "/restrito/notaFiscalServico/cadastroNotaFiscalServico.xhtml";
        } else {
            createFacesErrorMessage("Selecione no mínimo uma parcela");
            return "selecionaContasParaNota.xhtml";
        }
    }

    public String getSubtituloContaReceber(Cliente cliente) {
        return subtituloPorCliente.get(cliente);
    }

    public void calcularSubtituloContaReceber(Object o) {
        int qteParcelas = 0;
        double valorAReceber = 0d;
        if (o instanceof Cliente) {
            // The loop should find the sortBy value rows in all dataTable data.
            for (ContaParcela p : model.getWrappedData()) {
                if (o.equals(p.getIdConta().getIdCliente())) {
                    ++qteParcelas;
                    valorAReceber += p.getValor();
                }
            }
            String subtitulo = "Parcelas " + qteParcelas + ", valor R$ " + NumeroUtil.formatarCasasDecimais(valorAReceber, 2, false);
            subtituloPorCliente.put((Cliente) o, subtitulo);
        }
    }

    public void doShowNFSVinculada() {
        notaFiscalServicoControle.setNfsSelecionado(contaParcelaSelecionada.getIdNFS());
        notaFiscalServicoControle.doStartAlterar();
        PrimeFaces.current().executeScript("window.open('/restrito/notaFiscalServico/cadastroNotaFiscalServico.xhtml', '_BLANK');");
    }

    public void doStartNFSBond() {
        contaParcelaSelecionada.setIdNFS(new NFS());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 800);
        options.put("height", 600);
        showModal("modals/buscaNFS.xhtml", options);
    }

    public void loadNFS() {
        String num = contaParcelaSelecionada.getIdNFS().getNumeroNotaFiscal();
        if (num.length() != 15) {
            String[] partes = num.split("/");
            num = partes[0] + StringUtil.adicionarCaracterEsquerda(partes[1], "0", 15 - partes[0].length());
        }
        NFS nfs = nfsService.buscarNFSPorNumeroNotaFiscal(num);
        if (nfs != null) {
            contaParcelaSelecionada.setIdNFS(nfs);
        }
    }

    public void saveBond() {
        try {
            if (contaService.buscarParcelaPorNFS(contaParcelaSelecionada.getIdNFS()) != null) {
                createFacesErrorMessage("NFS já vinculada a outra parcela!");
                return;
            }
            contaService.salvarContaParcela(contaParcelaSelecionada);
            // Recarrega o dataGrid com a listagem de parcelas
            PrimeFaces.current().executeScript("parent.location.reload();");
            closeModal();
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao tentar salvar o vínculo.");
        }
    }

    public void cancelBond() {
        contaParcelaSelecionada.setIdNFS(null);
        closeModal();
    }

    public List<ContaParcela> getModelRows() {
        return model.getWrappedData();
    }

    @Override
    public EnumConta getTipoConta() {
        return EnumConta.RECEBER;
    }

    public String retornarTelaListaContaReceber() {
        iniciaFiltroPeriodo();
        return "/restrito/contaReceber/listaContaReceber.xhtml?faces-redirect=true";
    }

}
