package br.com.villefortconsulting.sgfinancas.controle.apoio;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.controle.BasicControl;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoServico;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteTurma;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContratoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteVeiculoService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContratoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.NFSService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.SolicitanteService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContratoException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumParcelaContrato;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;

@Getter
@Setter
public abstract class ContratoControleBase extends BasicControl implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    protected EmpresaService empresaService;

    @EJB
    protected ContratoService contratoService;

    @EJB
    protected FuncaoAjudaService funcaoAjudaService;

    @EJB
    protected ClienteService clienteService;

    @EJB
    protected FornecedorService fornecedorService;

    @EJB
    protected ContaBancariaService contaBancariaService;

    @EJB
    protected ContaService contaService;

    @EJB
    protected ClienteVeiculoService clienteVeiculoService;

    @EJB
    protected DocumentoService documentoService;

    @EJB
    protected NFSService nfsService;

    @EJB
    protected DocumentoAnexoService documentoAnexoService;

    @EJB
    protected SolicitanteService solicitanteService;

    @EJB
    protected PlanoContaService planoContaService;

    @Inject
    protected DocumentoMapper documentoMapper;

    protected Contrato contratoSelecionado;

    protected transient Part part;

    protected LazyDataModel<Contrato> model;

    protected ContratoFiltro filtro = new ContratoFiltro();

    protected Boolean contratosExpirando = false;

    protected transient List<AnexoDTO> listaAnexos;

    protected SolicitanteTurma solicitanteTurmaSelecionado;

    protected abstract EnumTipoContrato getTipoContrato();

    protected ClienteVeiculo veiculo;

    protected ContratoServico contratoServicoSelecionado;

    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                contratoService::quantidadeRegistrosFiltrados,
                contratoService::getListaFiltrada,
                filter -> filter.setTipoContrato(getTipoContrato().getTipo()));
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CONTRATO_ENTRADA.getChave()).getDescricao());
        return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public List<Contrato> getContratos() {
        return contratoService.listar();
    }

    public String getLabelSituacaoTxInstalacao() {
        return getSituacaoContratoETipoDeConta(contratoSelecionado, EnumTipoConta.TAXA_INSTALACAO.getTipo());
    }

    public String getLabelSituacaoTxAdesao() {
        return getSituacaoContratoETipoDeConta(contratoSelecionado, EnumTipoConta.TAXA_ADESAO.getTipo());
    }

    public String getSituacaoContratoETipoDeConta(Contrato contrato, String tipo) {
        for (Conta conta : contaService.contaPorContrato(contrato)) {
            if (tipo.equals(conta.getTipoConta())) {
                List<ContaParcela> lista = contaService.listarContaParcela(conta);
                if (!lista.isEmpty()) {
                    return EnumSituacaoConta.getDescricao(lista.get(0).getSituacao());
                }
            }
        }
        return "";
    }

    public List<DocumentoAnexo> buscarListaAnexo(Contrato contrato) {
        return contratoService.buscarDocumentosPorContrato(contrato);
    }

    public EnumParcelaContrato[] getParcelas() {
        return EnumParcelaContrato.values();
    }

    public void preencherParcela() {
        contratoSelecionado = contaService.parcelarContrato(contratoSelecionado);
    }

    public void alteraContaPorTaxa(Contrato contrato) {
        contaService.alteraContaPorTx(contrato);
    }

    public boolean existeParcelaPaga() {
        return contaService.existeParcelaPaga(contratoSelecionado);
    }

    public String cancelarContrato() {
        try {
            contratoService.cancelarContrato(contratoSelecionado);
            createFacesSuccessMessage("Contrato cancelado com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return contratoService.getDadosAuditoriaByID(contratoSelecionado);
    }

    public void calcularValorTotal(ParcelaDTO parcelaDTO) {
        Double somaParcelasNaoAlteradas = contratoSelecionado.getListParcelaDTO().stream()
                .filter(parcela -> !parcela.equals(parcelaDTO))
                .map(ParcelaDTO::getValor)
                .filter(Objects::nonNull)
                .reduce(0d, (a, v) -> a + v);

        if (Double.compare(somaParcelasNaoAlteradas, 0d) == 0) {
            List<ParcelaDTO> parcelas = contratoSelecionado.getListParcelaDTO();
            parcelas.forEach(parcela -> parcela.setValor(parcelaDTO.getValor()));
            contratoSelecionado.setListParcelaDTO(parcelas);
        }

        Double valorTotal = contratoService.calcularValorTotal(contratoSelecionado.getListParcelaDTO());
        contratoSelecionado.setValorContrato(valorTotal);
    }

    protected void updateDescricaoDocumento() {
        contratoSelecionado.getIdDocumento().setDescricao(
                contratoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                        .map(anexo -> anexo.getNomeArquivo() + " - " + DataUtil.dateToString(anexo.getDataEnvio()))
                        .reduce((doc1, doc2) -> doc1 + "; " + doc2)
                        .orElse("")
        );
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedorService.listarFornecedorPorEmpresa(empresaService.getEmpresa());
    }

    public void deletarAnexo() {
        contratoSelecionado.setIdDocumento(null);
        part = null;
    }

    public List<ContaBancaria> contasBancarias() {
        return contaBancariaService.listarAtivas();
    }

    public String removerArquivos() {
        listaAnexos.clear();
        return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public String removerArquivo(AnexoDTO anexo) {
        if (listaAnexos.contains(anexo)) {
            listaAnexos.remove(anexo);
        }
        return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public String listAnexos(Contrato contrato) {
        contratoSelecionado = contrato;
        listaAnexos = new ArrayList<>();
        if (contratoSelecionado.getIdDocumento() != null && !contratoSelecionado.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = contratoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return "listAnexos" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public Integer countAnexos(Contrato contrato) {
        int qte = contratoService.buscarDocumentosPorDocumento(contrato.getIdDocumento()).size();
        if (qte > 1) {
            return qte;
        }
        qte += nfsService.buscarNFSPorContrato(contrato).size();
        return qte;
    }

    public void setPart(FileUploadEvent event) {
        AnexoDTO anexo = new AnexoDTO();
        try {
            anexo.setConteudo("data:" + event.getFile().getContentType() + ";base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(event.getFile().getInputstream())));
            anexo.setDataEnvio(new Date());
            anexo.setIdUsuarioEnvio(getUsuarioLogado().getId());
            anexo.setNomeUsuarioEnvio(getUsuarioLogado().getNome());
            anexo.setMimeType(event.getFile().getContentType());
            anexo.setNome(event.getFile().getFileName());
            listaAnexos.add(anexo);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ClienteVeiculo> getVeiculos() {
        return clienteVeiculoService.listaVeiculosPor(contratoSelecionado.getIdCliente());
    }

    public List<Cliente> getClientes() {
        return clienteService.listarClienteAtivoPorEmpresa(empresaService.getEmpresa());
    }

    public void setContratosExpirando(Boolean contratosExpirando) {
        this.contratosExpirando = contratosExpirando;
        filtro.setExpirando(contratosExpirando);
    }

    public void removerAnexo(AnexoDTO adto) {
        listaAnexos.remove(adto);
    }

    public Integer getNumeroParcelasVencidas() {
        Long qtdParcelasVencidas = contratoSelecionado.getListParcelaDTO().stream().filter(parcela -> parcela.getDataVencimento().compareTo(DataUtil.getHoje()) < 0).count();
        return qtdParcelasVencidas.intValue();
    }

    public String doShowAuditoria() {
        return "listaAuditoriaContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public Double totalizaValorBruto() {
        Double val = contratoSelecionado.getListParcelaDTO().stream()
                .map(ParcelaDTO::getValor)
                .filter(Objects::nonNull)
                .reduce(0d, (a, v) -> a + v);
        contratoSelecionado.setValorContrato(val);
        return val;
    }

    public String doStartAdd() {
        cleanCache();
        contratoSelecionado = new Contrato();
        contratoSelecionado.setValorContrato(0d);
        listaAnexos = new ArrayList<>();
        contratoSelecionado.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao());
        return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    @Override
    public void cleanCache() {
        contratoSelecionado = new Contrato();
        contratoServicoSelecionado = null;
        part = null;
        solicitanteTurmaSelecionado = null;
    }

    public boolean podeSalvar() {
        return !"IT".equals(contratoSelecionado.getSituacao()) && contratoSelecionado.getListParcelaDTO().stream()
                .allMatch(parcela -> parcela.getFechada() == null || parcela.getFechada().equals("N"));
    }

    public String doFinishExcluir() {
        try {
            if (getTipoContrato() == EnumTipoContrato.CLIENTE && contratoService.verificarSeExisteMovimentacaoFinanceira(contratoSelecionado)) {
                return "confirmaRemoverContrato" + getTipoContrato().getDescricao() + ".xhtml";
            }
            contratoService.remover(contratoSelecionado);
            createFacesSuccessMessage("Contrato excluído com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public boolean podeSelecionarAParcela(EnumParcelaContrato parcela) {
        return contratoSelecionado.getListParcelaDTO().stream()
                .filter(parcelaDTO -> parcelaDTO.getDataPagamento() != null)
                .count() >= parcela.getForma();
    }

    public void preencheContaPorTaxa(Contrato contrato, String taxaAdesao) {
        contaService.preencheContaPorTx(contrato, taxaAdesao, getTipoContrato().getTipoTransacao().getTipo());
    }

    public String retornarTela() {
        cleanCache();
        return "listaContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public String doFinishAdd() {
        try {
            Date data = contratoSelecionado.getListParcelaDTO().stream()
                    .map(ParcelaDTO::getDataVencimento)
                    .min(Date::compareTo)
                    .orElseGet(() -> contratoSelecionado.getDataVencimentoPagamento());
            contratoSelecionado.setDataVencimentoPagamento(data);
            if (contratoSelecionado.getListParcelaDTO().stream().anyMatch(parcela -> parcela.getValor() < 0d)) {
                createFacesErrorMessage("Uma ou mais parcelas estão com o valor negativo. Não será possível salvar o contrato.");
                return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
            }
            if (veiculo != null) {
                ContratoClienteVeiculo ccv = new ContratoClienteVeiculo();
                ccv.setIdContrato(contratoSelecionado);
                ccv.setIdClienteVeiculo(veiculo);
                if (contratoSelecionado.getContratoClienteVeiculoList() == null) {
                    contratoSelecionado.setContratoClienteVeiculoList(new ArrayList<>());

                }
                contratoSelecionado.getContratoClienteVeiculoList().add(ccv);
            }
            Documento removerDocumento = null;
            if (!listaAnexos.isEmpty()) {
                Documento doc;
                if (contratoSelecionado.getIdDocumento() == null) {
                    doc = documentoService.criarDocumento(getUsuarioLogado(), "Anexos do contrato");
                } else {
                    doc = documentoService.buscar(contratoSelecionado.getIdDocumento().getId());
                }
                doc = documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), listaAnexos);
                contratoSelecionado.setIdDocumento(doc);
                updateDescricaoDocumento();
                documentoService.salvar(doc);
            } else if (contratoSelecionado.getIdDocumento() != null) {
                removerDocumento = contratoSelecionado.getIdDocumento();
                contratoSelecionado.setIdDocumento(null);
            }
            if (contratoSelecionado.getId() == null) {
                contratoSelecionado = contratoService.salvarContrato(contratoSelecionado, getTipoContrato());
                if (contratoSelecionado.getTaxaAdesao() != null && contratoSelecionado.getDataVencimentoAdesao() != null) {
                    preencheContaPorTaxa(contratoSelecionado, EnumTipoConta.TAXA_ADESAO.getTipo());
                }

                if (contratoSelecionado.getTaxaInstalacao() != null && contratoSelecionado.getDataVencimentoInstalacao() != null) {
                    preencheContaPorTaxa(contratoSelecionado, EnumTipoConta.TAXA_INSTALACAO.getTipo());
                }

                if (solicitanteTurmaSelecionado != null) {
                    solicitanteTurmaSelecionado.setIdContrato(contratoSelecionado);
                    solicitanteService.alterarSolicitanteTurma(solicitanteTurmaSelecionado);
                }
            } else {
                contratoSelecionado = contratoService.salvarContrato(contratoSelecionado, getTipoContrato());
                if (contratoSelecionado.getTaxaAdesao() != null || contratoSelecionado.getTaxaInstalacao() != null) {
                    alteraContaPorTaxa(contratoSelecionado);
                }
            }
            if (removerDocumento != null) {
                documentoService.remover(removerDocumento);
            }

            createFacesSuccessMessage("Contrato salvo com sucesso!");
            return retornarTela();
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
        }
    }

    public String doShowContratosVencendo() {
        return "/restrito/" + getTipoContrato().getNomePasta() + "/listaContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            contratoSelecionado.setIdDocumento(documentoAnexoService.criarDocumentoAnexo(contratoSelecionado.getIdDocumento(), getUsuarioLogado(), event).getIdDocumento());
            updateDescricaoDocumento();
        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String doStartAlterar() {
        if (!podeSalvar()) {
            createFacesInfoMessage("Uma ou mais parcelas deste contrato estão fechadas, não será possível alterá-lo!");
        }
        contratoServicoSelecionado = null;
        listaAnexos = new ArrayList<>();
        if (contratoSelecionado.getIdDocumento() != null && !contratoSelecionado.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = contratoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        contratoSelecionado.setListParcelaDTO(contaService.listarContaParcela(contratoSelecionado.getIdConta()).stream()
                .map(contaReceberParcela -> {
                    ParcelaDTO parcelaDTO = new ParcelaDTO();
                    parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
                    parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
                    parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
                    parcelaDTO.setValor(contaReceberParcela.getValor());
                    parcelaDTO.setValorLiquido(contaReceberParcela.getValorTotal());
                    parcelaDTO.setDataCancelamento(contaReceberParcela.getDataCancelamento());
                    parcelaDTO.setDataPagamento(contaReceberParcela.getDataPagamento());
                    parcelaDTO.setValorPago(contaReceberParcela.getValorPago());
                    parcelaDTO.setSituacao(contaReceberParcela.getSituacao());
                    return parcelaDTO;
                })
                .collect(Collectors.toList()));
        return "cadastroContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public String doStartInterrupt() {
        contratoSelecionado.setListParcelaDTO(contaService.obterParcelaDTO(contratoSelecionado.getIdConta()));
        return "interromperContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public String doFinishInterrupt() {
        try {
            Double valorDesconto = contratoSelecionado.getDesconto();
            List<ParcelaDTO> parcelasPagas = contratoSelecionado.getListParcelaDTO().stream()
                    .filter(parcela -> parcela.getDataPagamento() != null)
                    .collect(Collectors.toList());
            Double valorPago = parcelasPagas.stream()
                    .mapToDouble(ParcelaDTO::getValorPago)
                    .sum();
            Double valorTaxas = NumeroUtil.somar(contratoSelecionado.getTaxaAdesao(), contratoSelecionado.getTaxaInstalacao());
            contratoSelecionado.setListParcelaDTO(parcelasPagas);
            contratoSelecionado.setNumeroParcelas(contratoSelecionado.getListParcelaDTO().size());
            contratoSelecionado.setValorContrato(valorPago - NumeroUtil.somar(valorDesconto, valorTaxas));
            contratoSelecionado.setSituacao(EnumSituacaoConta.INTERROMPIDO.getSituacao());
            contratoService.salvarContrato(contratoSelecionado, getTipoContrato());
            return "listaContrato" + getTipoContrato().getDescricao() + ".xhtml";
        } catch (ContratoException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível interromper este contrato.");
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível interromper este contrato.");
        }
        return "interromperContrato" + getTipoContrato().getDescricao() + ".xhtml";
    }

    public List<PlanoConta> planoContas() {
        if (getTipoContrato() == EnumTipoContrato.CLIENTE) {
            return planoContaService.listarPlanosContaParaTransacoesContaReceber();
        } else {
            return planoContaService.listarPlanosContaParaTransacoesCompra();
        }
    }

    public void doStartAdicionarServico() {
        contratoServicoSelecionado = new ContratoServico();
        contratoServicoSelecionado.setIdContrato(contratoSelecionado);
        contratoServicoSelecionado.setDesconto(0d);
        contratoServicoSelecionado.setQuantidade(0d);
        contratoServicoSelecionado.setValor(0d);
        contratoServicoSelecionado.setAtivo("S");
        PrimeFaces.current().executeScript("PF('modal').show();");
    }

    public void doStartEditar() {
        contratoServicoSelecionado.setEdicao(true);
    }

    public void doSelecionarServico() {
        contratoServicoSelecionado.setValor(contratoServicoSelecionado.getIdServico().getValorVenda());
    }

    public void doAtivarServico() {
        contratoServicoSelecionado.setAtivo("S");
        preencherParcela();
    }

    public void doInativarServico() {
        contratoServicoSelecionado.setAtivo("N");
        preencherParcela();
    }

    public void doFinishAdicionar() {
        if (!contratoServicoSelecionado.isEdicao()) {
            contratoSelecionado.getContratoServicoList().add(contratoServicoSelecionado);
            contratoServicoSelecionado.setEdicao(false);
        }
        preencherParcela();
        PrimeFaces.current().executeScript("PF('modal').hide();");
    }

    public void doShowAuditoriaServico() {
    }

    public Boolean getTemListaDeServicos() {
        List csl = contratoSelecionado.getContratoServicoList();
        return csl != null && !csl.isEmpty();
    }

}
