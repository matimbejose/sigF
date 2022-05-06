package br.com.villefortconsulting.sgfinancas.controle.apoio;

import br.com.villefortconsulting.sgfinancas.controle.BasicControl;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ArquivoImportacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FaturaCallbackDto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoMovimentoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteMovimentacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFinalidadeNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.ejb.EJB;
import javax.xml.bind.JAXBException;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.UploadedFile;

@Getter
@Setter
public abstract class ImportacaoNFBase extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    protected CentroCustoService centroCustoService;

    @EJB
    protected ClienteService clienteService;

    @EJB
    protected ClienteMovimentacaoService clienteMovimentacaoService;

    @EJB
    protected ContaService contaService;

    protected transient UploadedFile file;

    protected transient List<ImportacaoMovimentoDTO> listaImportacao;

    protected abstract String getPage();

    protected abstract ImportacaoMovimentoDTO convertNFe(String arquivo) throws JAXBException;

    protected String modelo;

    protected boolean exibirComCliente;

    protected boolean exibirSemErroImportacao;

    @Override
    public void cleanCache() {
        file = null;
        listaImportacao = null;
    }

    public String doCancel() {
        cleanCache();
        return getPage();
    }

    public String doReadFile() {
        exibirComCliente = false;
        exibirSemErroImportacao = false;
        if (file.getContents() == null) {
            cleanCache();
            createFacesErrorMessage("Informe um arquivo para alterar.");
            return getPage();
        }
        List<ArquivoImportacaoDTO> arquivos = new ArrayList<>();
        List<String> erros = new ArrayList<>();
        switch (file.getContentType()) {
            case "text/xml":
                arquivos.add(new ArquivoImportacaoDTO(file.getFileName(), file.getContents()));
                break;
            case "application/zip":
            case "application/x-zip-compressed":
                openZip(erros, arquivos);
                break;
            default:
                createFacesErrorMessage("Tipo de arquivo não suportado.");
                cleanCache();
                return getPage();
        }
        final Cliente diverso = clienteService.findClienteByNomeAndCreate("Clientes diversos");
        listaImportacao = arquivos.stream()
                .filter(ai -> ai.getContent() != null && ai.getContent().length > 0)
                .map(ai -> {
                    try {
                        ImportacaoMovimentoDTO dto = convertNFe(new String(ai.getContent(), StandardCharsets.UTF_8));
                        if (dto == null) {
                            erros.add("Não foi possível ler o arquivo \"" + ai.getName() + "\". O layout do arquivo não é válido para a importação selecionada.");
                        }
                        return dto;
                    } catch (JAXBException ex) {
                        erros.add("Não foi possível ler o arquivo \"" + ai.getName() + "\". Ocorreu um erro durante a conversão do arquivo.");
                    } catch (CadastroException ex) {
                        erros.add("Não foi possível ler o arquivo \"" + ai.getName() + "\". " + ex.getMessage());
                    } catch (Exception ex) {
                        erros.add("Não foi possível ler o arquivo \"" + ai.getName() + "\". ");
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(im -> {
                    if (im.getCentroCusto() == null) {
                        if (!im.getOrigem().isCancelamento()) {
                            im.setMotivoErro("Registro não será importado, centro de custo não identificado.");
                            im.setImportar(false);
                        }
                        return im;
                    }
                    final String obs = im.getObservacao() != null ? im.getObservacao().toUpperCase() : "";
                    Arrays.stream(ImportacaoMovimentoDTO.PlanoContaEnum.values())
                            .filter(pc -> obs.contains(pc.name()))
                            .findAny().ifPresent(pc -> {
                                im.setTipoPlanoConta(pc);
                                im.setPlanoConta(pc.getGetter().apply(im.getCentroCusto()));
                            });
                    if (im.getPlanoConta() == null) {
                        im.setPlanoConta(im.getCentroCusto().getIdPlanoContaOutros());
                    }
                    im.setGeraFinanceiro(im.getTipoPlanoConta() != ImportacaoMovimentoDTO.PlanoContaEnum.IUGU);
                    if (im.getObservacao() != null) {
                        im.setObservacao(im.getObservacao().replace("#xD#xA", "\n"));
                    }
                    Cliente cliente;
                    if (im.getCpfCnpjDestinatario() != null && !im.getCpfCnpjDestinatario().trim().isEmpty()) {
                        cliente = clienteService.buscarPorCpfCnpj(im.getCpfCnpjDestinatario());
                        if (cliente != null) {
                            im.setTipoVinculo(ImportacaoMovimentoDTO.TipoVinculoClienteEnum.OK);
                        }
                    } else {
                        cliente = diverso;
                        im.setTipoVinculo(ImportacaoMovimentoDTO.TipoVinculoClienteEnum.CLIENTE_DIVERSO);
                    }
                    im.setCliente(cliente);
                    return im;
                })
                .sorted((a, b) -> {
                    if (a.getOrigem().isCancelamento() != b.getOrigem().isCancelamento()) {
                        return a.getOrigem().isCancelamento() ? 1 : -1;
                    }
                    if (a.getFinalidade() != b.getFinalidade()) {
                        Integer intA = Integer.parseInt(a.getFinalidade().getTipo());
                        Integer intB = Integer.parseInt(b.getFinalidade().getTipo());
                        return intA.compareTo(intB);
                    }
                    return a.getDataMovimentacao().compareTo(b.getDataMovimentacao());
                })
                .collect(Collectors.toList());
        listaImportacao.stream()
                .filter(im -> im.getOrigem() == EnumTipoClienteMovimentacao.NOTA_FISCAL_PRODUTO)
                .filter(im1 -> listaImportacao.stream()
                .anyMatch(im2 -> im1 != im2
                && im1.getCliente() != null && im1.getCliente().equals(im2.getCliente())
                && im1.getDataMovimentacao().compareTo(im2.getDataMovimentacao()) == 0
                && im1.getValor().compareTo(im2.getValor()) == 0))
                .forEach(im -> {
                    im.setImportar(false);
                    if (im.getMotivoErro() != null) {
                        im.setMotivoErro(im.getMotivoErro() + " Registro duplicado.");
                    } else {
                        im.setMotivoErro("Registro não será importado, registro duplicado.");
                    }
                });
        if (!erros.isEmpty()) {
            createFacesErrorMessage("Alguns arquivos não puderam ser lidos.");
            erros.stream()
                    .map(e -> " - " + e)
                    .forEach(this::createFacesErrorMessage);
        }
        if (listaImportacao.isEmpty()) {
            cleanCache();
            createFacesErrorMessage("Nenhum dos arquivos informados pôde ser lido.");
        }

        return getPage();
    }

    private void openZip(List<String> erros, List<ArquivoImportacaoDTO> arquivos) {
        try (ZipFile zipFile = new ZipFile(FileUtil.convertByteToFile(file.getContents(), "temp.zip"), StandardCharsets.UTF_8)) {
            readZip(zipFile.entries(), zipFile, erros, arquivos);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public String doImport() {
        final Date primeiroDoSete = DataUtil.converterStringParaDate("01/07/2021");
        listaImportacao.stream()
                .filter(im -> Boolean.TRUE.equals(im.getImportar()) && im.getOrigem().isCancelamento() || im.getCentroCusto() != null)
                .map(im -> {
                    Cliente cliente = im.getCliente();
                    try {
                        if (im.getOrigem().isCancelamento()) {
                            if (!clienteMovimentacaoService.cancelarMovimentacao(im)) {
                                im.setMotivoErro("Erro ao cancelar a movimentação " + im.getIdIntegracao());
                            }
                            return null;
                        }
                        if (im.getFinalidade() == EnumFinalidadeNF.NF_COMPLEMENTAR || im.getFinalidade() == EnumFinalidadeNF.NF_AJUSTE) {
                            im.setMotivoErro("A finalidade da operação (" + im.getFinalidade().getDescricao() + ") não suportada " + im.getIdIntegracao());
                            return null;
                        }
                        if (im.getFinalidade() == EnumFinalidadeNF.NF_DEVOLUCAO_MERCADORIA && im.getNotaReferencia() != null) {
                            return processaDevolucaoNFe(im);
                        }
                        cliente = validaCliente(cliente, im);
                        boolean ehBH = im.getCentroCusto().getDescricao().contains("BH");
                        if (!ehBH && Boolean.TRUE.equals(im.getGeraFinanceiro()) && im.getDataMovimentacao().before(primeiroDoSete)) {
                            geraContaParcelaParaMovimentacao(im, cliente);
                        } else {
                            ClienteMovimentacao cm = clienteMovimentacaoService.buscaMovimentobyIdIntegracao(cliente, im.getIdIntegracao());
                            if (cm == null) {
                                clienteMovimentacaoService.lancarMovimentacao(cliente, im);
                            } else {
                                clienteMovimentacaoService.editarMovimentacao(cm, (im.getValor() - im.getValorDesconto()));
                            }
                        }
                    } catch (Exception ex) {
                        if (cliente != null) {
                            final String nome = cliente.getNome();
                            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex, () -> "Erro ao lançar movimentação para o cliente " + nome + ",chave" + im.getIdIntegracao());
                        }
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, ex, () -> "Erro ao lançar movimentação para o cliente. Chave: " + im.getIdIntegracao());
                        im.setMotivoErro(ex.getMessage());
                    }
                    return cliente;
                })
                .filter(Objects::nonNull)
                .distinct()
                .forEach(clienteMovimentacaoService::atualizarSaldo);
        listaImportacao.removeIf(im -> im.getMotivoErro() == null);
        if (listaImportacao.isEmpty()) {
            createFacesSuccessMessage("Arquivo importado com sucesso.");
            cleanCache();
        } else {
            createFacesErrorMessage("Alguns registros não puderam ser importados.");
        }
        return getPage();
    }

    private void geraContaParcelaParaMovimentacao(ImportacaoMovimentoDTO im, Cliente cliente) throws ContaException {
        FaturaCallbackDto fatura = new FaturaCallbackDto();
        fatura.setDataCriacao(im.getDataMovimentacao());
        fatura.setDataVencimento(im.getDataMovimentacao());
        fatura.setTotalFatura(im.getValor());
        fatura.setDataPagamento(im.getDataMovimentacao());
        fatura.setTotalDesconto(im.getValorDesconto());
        fatura.setTotalPago(im.getValor() - im.getValorDesconto());
        fatura.setIdFatura(im.getIdIntegracao());
        ContaParcela parcela = contaService.buscarContaParcelaPorNumNF(im.getIdIntegracao());
        if (parcela == null) {
            Conta conta = contaService.preencheContaIUGU(cliente, fatura, im.getCentroCusto(), null, im.getPlanoConta());
            contaService.listarContaParcela(conta).stream()
                    .map(cp -> {
                        cp.setNumNf(im.getIdIntegracao());
                        return cp;
                    })
                    .forEach(contaService::salvarContaParcela);
        } else {
            parcela.setDesconto(im.getValorDesconto());
            parcela.setValor(im.getValor());
            contaService.salvarContaParcela(parcela);
            Conta conta = contaService.buscar(parcela.getIdConta().getId());
            contaService.salvar(conta);
        }
    }

    private Cliente validaCliente(Cliente cliente, ImportacaoMovimentoDTO im) {
        if (cliente != null) {
            return cliente;
        }
        if (im.getNomeDestinatario() == null) {
            throw new CadastroException("O nome do cliente não foi informado.", null);
        }
        Cliente aux = clienteService.buscarPorCpfCnpj(im.getCpfCnpjDestinatario());
        if (aux == null) {
            cliente = new Cliente();
            cliente.setNome(im.getNomeDestinatario());
            cliente.setCpfCNPJ(im.getCpfCnpjDestinatario());
            cliente.setTipo(CpfCnpjUtil.removerMascaraCnpj(im.getCpfCnpjDestinatario()).length() == 14 ? "PJ" : "PF");
            im.setCliente(clienteService.salvar(cliente));
        } else {
            im.setCliente(aux);
        }
        return im.getCliente();
    }

    private Cliente processaDevolucaoNFe(ImportacaoMovimentoDTO im) {
        ClienteMovimentacao cm = clienteMovimentacaoService.buscaMovimentobyIdIntegracao(im.getNotaReferencia());
        ContaParcela parcela = contaService.buscarContaParcelaPorNumNF(im.getNotaReferencia());
        if (parcela == null && cm == null) {
            im.setMotivoErro("Não foi possível realizar a devolução pois não foi encontrada a nota de referência " + im.getNotaReferencia());
            return null;
        }
        if (cm != null) {
            Double restante = cm.getValorPrevisto() - im.getValor();
            ClienteMovimentacao cm2 = clienteMovimentacaoService.buscaMovimentobyIdIntegracao(im.getIdIntegracao());
            if (cm2 == null) {
                im.setMotivoErro("Não foi possível encontrar a transação de origem para ser cancelada.");
                return null;
            }
            if (restante < 0.01d) {
                clienteMovimentacaoService.cancelarMovimentacao(cm2.getIdIntegracao(), im.getDataMovimentacao());
            } else {
                clienteMovimentacaoService.editarMovimentacao(cm, (cm.getValorPrevisto() - im.getValor() + im.getValorDesconto()));
            }
            return cm.getIdCliente();
        }
        if (parcela == null) {
            return null;
        }
        Double restante = parcela.getValor() - im.getValor();
        if (restante < 0.01d) {
            contaService.cancelamentoBaixaEConta(parcela);
        } else {
            parcela.setValor(parcela.getValor() - im.getValor() + im.getValorDesconto());
            contaService.salvarContaParcela(parcela);
            contaService.salvar(contaService.buscar(parcela.getIdConta().getId()));
        }
        ContaParcela parcela2 = contaService.buscarContaParcelaPorNumNF(im.getIdIntegracao());
        if (parcela2 != null) {
            contaService.cancelamentoBaixaEConta(parcela2);
        }
        return parcela.getIdConta().getIdCliente();
    }

    private void readZip(Enumeration<? extends ZipEntry> entries, final ZipFile zipFile, List<String> erros, List<ArquivoImportacaoDTO> arquivos) {
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            try (InputStream stream = zipFile.getInputStream(entry)) {
                int availableSize = stream.available();
                byte[] targetArray = new byte[availableSize];
                int readSize = stream.read(targetArray);
                if (availableSize > readSize) {
                    erros.add("Não foi possível ler o arquivo \"" + entry.getName() + "\". O tamanho esperado do arquivo difere do tamanho lido.");
                } else {
                    arquivos.add(new ArquivoImportacaoDTO(entry.getName(), targetArray));
                }
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public List<ImportacaoMovimentoDTO> getListaImportacao() {
        if (listaImportacao == null || (exibirComCliente && exibirSemErroImportacao)) {
            return listaImportacao;
        }
        return listaImportacao.stream()
                .filter(im -> {
                    if (!exibirSemErroImportacao && !exibirComCliente) {
                        return im.getCliente() == null || im.getMotivoErro() != null;
                    }
                    if (!exibirSemErroImportacao) {
                        return im.getCliente() == null;
                    }
                    if (!exibirComCliente) {
                        return im.getMotivoErro() != null;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void processaCliente(ImportacaoMovimentoDTO im) {
        if (im.getCliente() == null) {
            return;
        }
        im.setTipoVinculo(ImportacaoMovimentoDTO.TipoVinculoClienteEnum.AJUSTADO);
        if ("Clientes diversos".equals(im.getCliente().getNome())) {
            im.setTipoVinculo(ImportacaoMovimentoDTO.TipoVinculoClienteEnum.CLIENTE_DIVERSO);
        }

    }

}
