package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteContato;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacaoAlteracao;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.ModeloInformacao;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteMovimentacaoAlteracaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteMovimentacaoAlteracaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteMovimentacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteVeiculoService;
import br.com.villefortconsulting.sgfinancas.servicos.CombustivelService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosPrivadosWebService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusClienteMovimentacaoAlteracao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacaoAlteracao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ClienteVeiculoService clienteVeiculoService;

    @EJB
    private ClienteMovimentacaoService clienteMovimentacaoService;

    @EJB
    private ClienteMovimentacaoAlteracaoService clienteMovimentacaoAlteracaoService;

    @EJB
    private CombustivelService combustivelService;

    @EJB
    private UFService ufService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ContaService contaService;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private ServicosPrivadosWebService servicosPrivadosWebService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @Inject
    private CadastroControle cadastroControle;

    private Cliente clienteSelecionado;

    private ClienteContato contatoSelecionado;

    private ClienteContato clienteContatoSelecionado;

    private List<ClienteContato> listClienteContato;

    private List<ClienteVeiculo> listClienteVeiculo;

    private List<ClienteMovimentacao> listClienteMovimentacao;

    private LazyDataModel<Cliente> model;

    private LazyDataModel<ClienteMovimentacaoAlteracao> modelSolicitacaoExclusao;

    private LazyDataModel<ClienteContato> modelClienteContato;

    private ClienteFiltro filtro = new ClienteFiltro();

    private ClienteMovimentacaoAlteracaoFiltro filtroSolicitacaoExclusao = new ClienteMovimentacaoAlteracaoFiltro();

    private UF ufSelecionado;

    private transient Part part;

    private transient UploadedFile file;

    private List<ContaParcela> listConta = new LinkedList<>();

    private ClienteCadastroDTO dtoCadastro;

    private Marca marcaSelecionada;

    private ClienteVeiculo clienteVeiculoSelecionado = new ClienteVeiculo();

    private ClienteMovimentacao clienteMovimentacaoSelecionado = new ClienteMovimentacao();

    private ClienteMovimentacaoAlteracao clienteMovimentacaoAlteracaoSelecionada = new ClienteMovimentacaoAlteracao();

    private String tipoMovimentacao;

    private boolean editarMovimentacao = false;

    private FormaPagamento formaPagamentoSelecionada;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, clienteService);
        modelSolicitacaoExclusao = new VillefortDataModel<>(filtroSolicitacaoExclusao, clienteMovimentacaoAlteracaoService);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CLIENTE.getChave()).getDescricao());
        return "/restrito/cliente/cadastroCliente.xhtml";
    }

    @Override
    public void cleanCache() {
        this.ufSelecionado = null;
        this.listClienteContato = new LinkedList<>();
        this.clienteSelecionado = new Cliente();
        clearCacheVeiculo();
        listClienteMovimentacao = new ArrayList<>();
        editarMovimentacao = false;
    }

    public boolean clientePossuiPontos(Cliente cliente) {
        if (cliente != null && cliente.getPontuacao() != null) {
            return cliente.getPontuacao() > 0;
        }
        return false;
    }

    public List<Cliente> listarClientes() {
        return clienteService.listar();
    }

    public List<Cliente> buscarClientes(String nome) {
        return clienteService.listar(nome);
    }

    public List<Cliente> getClientes() {
        return clienteService.listar();
    }

    public List<Cliente> getSeguradoras() {
        return clienteService.listarSeguradoras();
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public void addContato() {
        ClienteContato cc = new ClienteContato();
        cc.setIdCliente(clienteSelecionado);
        listClienteContato.add(cc);
    }

    public void removeContato(ClienteContato cc) {
        listClienteContato.remove(cc);
    }

    public void buscarEnderecoPorCep() {
        if (clienteSelecionado.getEndereco().getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(clienteSelecionado.getEndereco().getCep());

            ufSelecionado = cepDTO.getUf();
            clienteSelecionado.getEndereco().setIdCidade(cepDTO.getCidade());
            clienteSelecionado.getEndereco().setEndereco(cepDTO.getEndereco());
            clienteSelecionado.getEndereco().setBairro(cepDTO.getBairro());
        }
    }

    public String doStartAdd() {
        cleanCache();
        clienteVeiculoSelecionado = null;
        clienteMovimentacaoSelecionado = new ClienteMovimentacao();
        clienteMovimentacaoSelecionado.setDataMovimentacao(DataUtil.getHoje());
        clienteMovimentacaoSelecionado.setValorPrevisto(0d);
        return "/restrito/cliente/cadastroCliente.xhtml";
    }

    public String doStartAlterar() {
        clearCacheVeiculo();
        listClienteContato = clienteService.listarClienteContato(clienteSelecionado);
        clienteVeiculoSelecionado = null;
        clienteMovimentacaoSelecionado = new ClienteMovimentacao();
        clienteMovimentacaoSelecionado.setDataMovimentacao(DataUtil.getHoje());
        clienteMovimentacaoSelecionado.setValorPrevisto(0d);
        listClienteVeiculo = clienteVeiculoService.listaTodosVeiculosPor(clienteSelecionado);
        listClienteMovimentacao = clienteMovimentacaoService.listaTodasMovimentacoesPor(clienteSelecionado);
        listClienteContato.stream().forEach(clienteContato -> clienteContato.setControle(StringUtil.gerarStringAleatoria(8)));
        if (clienteSelecionado.getEndereco() == null) {
            clienteSelecionado.setEndereco(new Endereco());
        }

        if (clienteSelecionado.getEndereco().getIdCidade() != null) {
            ufSelecionado = clienteSelecionado.getEndereco().getIdCidade().getIdUF();
        }
        return "/restrito/cliente/cadastroCliente.xhtml";
    }

    public String doStartContasCliente() {
        try {
            listConta = contaService.parcelasPorCliente(clienteSelecionado);
            return "/restrito/cliente/contaPorCliente.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível buscar as contas deste cliente.");
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            return "/restrito/cliente/listaCliente.xhtml";
        }
    }

    public String doFinishAdd() {
        try {
            clienteSelecionado.setClienteContatoList(listClienteContato.stream().filter(c -> c.getNome() != null).collect(Collectors.toList()));
            if (!listClienteVeiculo.isEmpty()) {
                clienteSelecionado.setClienteVeiculoList(listClienteVeiculo);
            }
            clienteSelecionado.setClienteMovimentacaoList(listClienteMovimentacao);
            clienteService.salvar(clienteSelecionado);
            createFacesSuccessMessage("Cliente salvo com sucesso!");
            return "/restrito/cliente/listaCliente.xhtml";
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return "/restrito/cliente/cadastroCliente.xhtml";
    }

    public String doShowAuditoria() {
        return "/restrito/cliente/listaAuditoriaCliente.xhtml";
    }

    public String doFinishExcluir() {
        try {
            clienteSelecionado.setAtivo("N");
            clienteService.salvar(clienteSelecionado);
            createFacesSuccessMessage("Cliente removido com sucesso.");
            return "/restrito/cliente/listaCliente.xhtml";
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
            return "listaCliente.xhtml";
        }
    }

    public List<FormaPagamento> getFormasPagamento() {
        return formaPagamentoService.listar();
    }

    public void buscarDadosClientePorCpfCnpj() {
        try {
            switch (clienteSelecionado.getTipo()) {
                case "PF":
                    if (clienteSelecionado.getCpfCNPJ() != null && clienteSelecionado.getDataNascimento() != null) {
                        clienteService.buscarDadosEmpresaPorCpf(clienteSelecionado);
                    } else {
                        createFacesErrorMessage("Informe o CPF e a data de nascimento do cliente.");
                    }
                    break;
                case "PJ":
                    clienteService.buscarDadosEmpresaPorCNPJ(clienteSelecionado);
                    ufSelecionado = clienteSelecionado.getEndereco().getIdCidade().getIdUF();
                    break;
                default:
                    createFacesErrorMessage("Informe o tipo de pessoa.");
                    break;
            }
        } catch (NotFoundException ex) {
            createFacesInfoMessage(ex.getMessage());
        } catch (ValidationException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public List<Object> getDadosAuditoria() {
        return clienteService.getDadosAuditoriaByID(clienteSelecionado);
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(cliente -> clienteService.importDto((ClienteCadastroDTO) cliente, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("CLIENTE_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Cliente",
                    clienteService.hasAny(),
                    true,
                    this::doStartAdd,
                    this::doFinishImport,
                    ClienteCadastroDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new ClienteCadastroDTO();
    }

    public void addClienteVeiculo() {
        clienteVeiculoSelecionado = new ClienteVeiculo();
        clienteVeiculoSelecionado.setCambio("M");
        clienteVeiculoSelecionado.setPortas(2);
        clienteVeiculoSelecionado.setAtivo("S");
        marcaSelecionada = null;
    }

    private void clearCacheVeiculo() {
        marcaSelecionada = null;
        listClienteVeiculo = new ArrayList<>();
    }

    public int getIdade() {
        if (clienteSelecionado.getDataNascimento() != null) {

            Calendar cData = Calendar.getInstance();
            Calendar cHoje = Calendar.getInstance();
            cData.setTime(clienteSelecionado.getDataNascimento());
            cData.set(Calendar.YEAR, cHoje.get(Calendar.YEAR));
            int idade = cData.after(cHoje) ? -1 : 0;
            cData.setTime(clienteSelecionado.getDataNascimento());
            idade += cHoje.get(Calendar.YEAR) - cData.get(Calendar.YEAR);
            return idade;
        }
        return 0;
    }

    public void setClienteVeiculoSelecionado(ClienteVeiculo clienteVeiculo) {
        clienteVeiculoSelecionado = clienteVeiculo;
    }

    public void adicionarClienteVeiculo() {
        if (clienteVeiculoSelecionado.getId() == null) {
            listClienteVeiculo.add(clienteVeiculoSelecionado);
        }
        clienteVeiculoSelecionado = null;
        marcaSelecionada = null;
        PrimeFaces.current().executeScript("PF('modal').hide()");
    }

    public void adicionarClienteMovimentacao() {
        if (editarMovimentacao) {
            try {
                if ("IUGU".equals(clienteMovimentacaoSelecionado.getOrigem())) {
                    Conta conta = contaService.buscarContaByCliemteMovimentacao(clienteMovimentacaoSelecionado);
                    List<ContaParcela> parcelas = contaService.listarContaParcela(conta);
                    for (ContaParcela parcela : parcelas) {
                        contaService.alterarContaParcela(parcela);
                        if (clienteMovimentacaoSelecionado.getDataCancelamento() != null) {
                            contaService.cancelamentoBaixaEConta(parcela);
                        } else {

                            if (parcela.getDataPagamento() != null && (clienteMovimentacaoSelecionado.getDataPagamento() == null
                                    || (clienteMovimentacaoSelecionado.getValorPago() != null && clienteMovimentacaoSelecionado.getValorPago() > 0))) {
                                contaService.desfazerBaixa(parcela);
                            }
                            parcela.setDataVencimento(clienteMovimentacaoSelecionado.getDataVencimento());
                            parcela.setDataPagamento(clienteMovimentacaoSelecionado.getDataPagamento());
                            parcela.setValor(clienteMovimentacaoSelecionado.getValorPrevisto());
                            parcela.setIdFormaPagamento(formaPagamentoSelecionada);
                            parcela.setValorTotal(NumeroUtil.somar(clienteMovimentacaoSelecionado.getValorPrevisto(),
                                    clienteMovimentacaoSelecionado.getValorJuros(), clienteMovimentacaoSelecionado.getValorTaxa()));
                            parcela.setValorPago(clienteMovimentacaoSelecionado.getValorPago());
                            if (clienteMovimentacaoSelecionado.getValorJuros() != null) {
                                parcela.setJuros(clienteMovimentacaoSelecionado.getValorJuros());
                            }
                            if (clienteMovimentacaoSelecionado.getValorTaxa() != null) {
                                parcela.setTarifa(clienteMovimentacaoSelecionado.getValorTaxa());
                            }
                            if (parcela.getValorPago() != null) {
                                if (parcela.getValorPago().equals(parcela.getValorTotal())) {
                                    parcela.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
                                } else if (parcela.getValorPago() != null) {
                                    parcela.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
                                } else {
                                    parcela.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                                }
                            }
                            contaService.alterarParcelaDiretoRepositorio(parcela);
                        }
                    }
                    if (clienteMovimentacaoSelecionado.getDataCancelamento() == null) {
                        conta.setDataVencimento(clienteMovimentacaoSelecionado.getDataVencimento());
                        conta.setDataPagamento(clienteMovimentacaoSelecionado.getDataPagamento());
                        conta.setIdCentroCusto(clienteMovimentacaoSelecionado.getIdCentroCusto());
                        if (clienteMovimentacaoSelecionado.getIdCentroCusto().getIdContaBancaria() != null) {
                            conta.setIdContaBancaria(clienteMovimentacaoSelecionado.getIdCentroCusto().getIdContaBancaria());
                        }
                        conta.setValor(clienteMovimentacaoSelecionado.getValorPrevisto());
                        conta.setJuros(clienteMovimentacaoSelecionado.getValorJuros());
                        conta.setTarifa(clienteMovimentacaoSelecionado.getValorTaxa());
                        conta.setValorPago(clienteMovimentacaoSelecionado.getValorPago());
                        conta.setValorTotal(NumeroUtil.somar(clienteMovimentacaoSelecionado.getValor(), clienteMovimentacaoSelecionado.getValorJuros(), clienteMovimentacaoSelecionado.getValorTaxa()));
                        if (clienteMovimentacaoSelecionado.getDataPagamento() != null) {
                            contaService.salvarConta(conta, clienteMovimentacaoSelecionado.getDataPagamento(), clienteMovimentacaoSelecionado.getValorPago());
                        } else {
                            contaService.salvarConta(conta);
                        }
                    }
                }
                editarMovimentacao = false;
                clienteMovimentacaoService.alterar(clienteMovimentacaoSelecionado);
            } catch (Exception ex) {
                createFacesErrorMessage("Não foi possível alterar movimentação.");
                Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);

            }
        } else {
            clienteMovimentacaoSelecionado.setOrigem(tipoMovimentacao);
            ClienteMovimentacao clienteMovimentacao = clienteMovimentacaoService.lancarMovimentacao(clienteSelecionado, clienteMovimentacaoSelecionado.getValorPrevisto(),
                    clienteMovimentacaoSelecionado.getDataMovimentacao(), EnumTipoClienteMovimentacao.retornaEnumSelecionado(tipoMovimentacao), null,
                    clienteMovimentacaoSelecionado.getDataMovimentacao(), clienteMovimentacaoSelecionado.getIdCentroCusto(), clienteMovimentacaoSelecionado.getTipoMovimentacao());
            if (tipoMovimentacao.equals("SL")) {
                clienteMovimentacaoService.pagar(clienteMovimentacao, clienteMovimentacao.getDataMovimentacao(), clienteMovimentacao.getValorPrevisto(),
                        0d, 0d, EnumTipoClienteMovimentacao.retornaEnumSelecionado(tipoMovimentacao));
            }
            clienteMovimentacaoSelecionado = new ClienteMovimentacao();
        }
        PrimeFaces.current().executeScript("PF('modalClienteMovimentacao').hide();");
        clienteMovimentacaoService.atualizarSaldo(clienteSelecionado);
        clienteSelecionado = clienteService.buscar(clienteSelecionado.getId());
        listClienteMovimentacao = clienteMovimentacaoService.listaTodasMovimentacoesPor(clienteSelecionado);
    }

    public void doCancelExcluirMovimentacao() {
        clienteMovimentacaoAlteracaoSelecionada.setStatus(EnumStatusClienteMovimentacaoAlteracao.FECHADO.getStatus());
        clienteMovimentacaoAlteracaoSelecionada.setDataFinalizacao(new Date());
        clienteMovimentacaoAlteracaoSelecionada.setIdUsuarioFinalizacao(getUsuarioLogado());
        clienteMovimentacaoAlteracaoService.salvar(clienteMovimentacaoAlteracaoSelecionada);
    }

    public void doConfirmExcluirMovimentacao() {
        clienteMovimentacaoAlteracaoSelecionada.setStatus(EnumStatusClienteMovimentacaoAlteracao.FECHADO.getStatus());
        clienteMovimentacaoAlteracaoSelecionada.setDataFinalizacao(new Date());
        clienteMovimentacaoAlteracaoSelecionada.setIdUsuarioFinalizacao(getUsuarioLogado());
        clienteMovimentacaoAlteracaoService.salvar(clienteMovimentacaoAlteracaoSelecionada);
        clienteMovimentacaoService.remover(clienteMovimentacaoAlteracaoSelecionada.getIdClienteMovimentacao());
    }

    public void doShowAuditoriaSolicitacaoAlteracao() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("height", 216);
        options.put("width", 600);
        showModal("modals/auditoriaClienteMovimentacaoAlteracao.xhtml", options);
    }

    public void doFinishExcluirMovimentacao() {
        clienteMovimentacaoService.remover(clienteMovimentacaoSelecionado);
        updateClienteMovimetacao();
    }

    public void doRequestExcluirMovimentacao() {
        ClienteMovimentacaoAlteracao cma = new ClienteMovimentacaoAlteracao();
        cma.setDataAlteracao(new Date());
        cma.setIdClienteMovimentacao(clienteMovimentacaoSelecionado);
        cma.setIdUsuarioSolicitacao(getUsuarioLogado());
        cma.setStatus(EnumStatusClienteMovimentacaoAlteracao.ABERTO.getStatus());
        cma.setTipo(EnumTipoClienteMovimentacaoAlteracao.EXCLUSAO.getTipo());
        cma.setTenatID(clienteMovimentacaoSelecionado.getTenatID());
        clienteMovimentacaoSelecionado.getClienteMovimentacaoAlteracaoList().add(cma);
        clienteMovimentacaoService.salvar(clienteMovimentacaoSelecionado);
    }

    private void updateClienteMovimetacao() {
        clienteMovimentacaoService.atualizarSaldo(clienteSelecionado);
        clienteSelecionado = clienteService.buscar(clienteSelecionado.getId());
        listClienteMovimentacao = clienteMovimentacaoService.listaTodasMovimentacoesPor(clienteSelecionado);
    }

    public void doStartEditarVeiculo() {
        marcaSelecionada = clienteVeiculoSelecionado.getIdModelo().getIdMarca();
    }

    public void doStartEditarMovimentacao() {
        editarMovimentacao = true;
        PrimeFaces.current().executeScript("PF('modalClienteMovimentacao').show();");
    }

    public String retornaOrigem() {
        if (clienteMovimentacaoSelecionado.getOrigem() != null) {
            return EnumTipoClienteMovimentacao.retornaEnumSelecionado(clienteMovimentacaoSelecionado.getOrigem()).getDescricao();
        }
        return "";
    }

    public boolean retornaMovimentacaoSaida() {
        return "IUGU".equals(clienteMovimentacaoSelecionado.getOrigem()) || "SL".equals(clienteMovimentacaoSelecionado.getOrigem());
    }

    public void doStartInativarVeiculo() {
        clienteVeiculoSelecionado.setAtivo("N");
        clienteVeiculoSelecionado = null;
    }

    public void doStartAtivarVeiculo() {
        clienteVeiculoSelecionado.setAtivo("S");
        clienteVeiculoSelecionado = null;
    }

    public void atualizaVeiculoModelo() {
        ModeloInformacao mi = clienteVeiculoSelecionado.getIdModelo().getAnos().stream()
                .filter(ano -> ano.getAno().equals(clienteVeiculoSelecionado.getAnoModelo()))
                .findAny()
                .orElse(null);
        if (mi != null) {
            clienteVeiculoSelecionado.setValorProtegido(mi.getPreco());
        }
    }

    public boolean ehCpf() {
        return dtoCadastro.getCpfCnpj() != null && dtoCadastro.getCpfCnpj().length() > 11;
    }

    public void geraSaldoInicial() {
        clienteMovimentacaoService.realizaMovimentacaoInicial(clienteSelecionado.getSaldoInicial(), clienteSelecionado);
        listClienteMovimentacao = clienteMovimentacaoService.listaTodasMovimentacoesPor(clienteSelecionado);
    }

    public String placasDo(Cliente cliente) {
        return StringUtil.listaParaTexto(clienteVeiculoService.listaVeiculosPor(cliente).stream()
                .map(ClienteVeiculo::getPlaca)
                .filter(placa -> placa != null && !placa.isEmpty())
                .collect(Collectors.toList()));
    }

    public void atualizarDados() {
        servicosPrivadosWebService.atulizarDadosCliente();
    }

    public void addClienteMovimentacao() {
        clienteMovimentacaoSelecionado = new ClienteMovimentacao();
        tipoMovimentacao = "";
        editarMovimentacao = false;
    }

    public void atualizaListaMovimentacao() {
        clienteMovimentacaoService.atualizarSaldo(clienteSelecionado);
        listClienteMovimentacao = clienteMovimentacaoService.listaTodasMovimentacoesPor(clienteSelecionado);
    }

    public String doStartReviewDeleteRequests() {
        return "/restrito/cliente/listaSolicitacoesExclusao.xhtml";
    }

    public boolean getCriaUsuarioParaCliente() {
        return "S".equals(parametroSistemaService.getParametro().getCriarUsuarioParaClienteCadastrado());
    }

}
