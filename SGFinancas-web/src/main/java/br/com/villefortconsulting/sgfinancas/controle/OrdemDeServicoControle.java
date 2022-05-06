package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ItensOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.StatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.OrdemDeServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.OrdemDeServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServicoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrdemDeServicoService ordemDeServicoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ContaService contaService;

    @EJB
    private RelatorioService relatorioService;

    private OrdemDeServico osSelecionada;

    private ItensOrdemDeServico itemSelecionado;

    private LazyDataModel<OrdemDeServico> model;

    private OrdemDeServicoFiltro filtro = new OrdemDeServicoFiltro();

    private Cliente cliente;

    private boolean cadastraCliente;

    private String status;

    private String controleEdicao;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                ordemDeServicoService::quantidadeRegistrosFiltrados,
                ordemDeServicoService::getListaFiltrada,
                filter -> filter.setUsuarioLogado(getUsuarioLogado()));
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_OS.getChave()).getDescricao());
        return "cadastroOrdemDeServico.xhtml";
    }

    public void cleanCliente() {
        cadastraCliente = false;
        cliente = new Cliente();
    }

    public void doStartPrint() {
        try {
            gerarPdf(relatorioService.gerarOrdemServico(getOsSelecionada()), "Ordem de serviço " + getOsSelecionada().getId());
            createFacesSuccessMessage("Ordem de serviço gerada com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public String doStartAdd() {
        itemSelecionado = new ItensOrdemDeServico();
        osSelecionada = new OrdemDeServico();
        osSelecionada.setIdFuncionarioEntrada(getUsuarioLogado().getIdFuncionario());
        osSelecionada.setItensOrdemDeServico(new ArrayList<>());
        Conta conta = new Conta();
        conta.setAdvemContrato("N");
        conta.setRepetirConta("N");
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setTipoConta(EnumTipoConta.ORDEM_DE_SERVICO.getTipo());
        osSelecionada.setIdConta(conta);
        return "cadastroOrdemDeServico.xhtml";
    }

    public String doStartAlterar() {
        itemSelecionado = new ItensOrdemDeServico();
        osSelecionada.setItensOrdemDeServico(ordemDeServicoService.listarItens(osSelecionada));
        osSelecionada.setStatusOrdemDeServico(ordemDeServicoService.listarMudancasDeStatus(osSelecionada));
        List<StatusOrdemDeServico> listaStatus = ordemDeServicoService.listarMudancasDeStatus(osSelecionada);
        if (!listaStatus.isEmpty()) {
            status = listaStatus.get(listaStatus.size() - 1).getStatus();
        }
        osSelecionada.getIdConta().setContaParcelaList(contaService.listarContaParcela(osSelecionada.getIdConta()));
        return "cadastroOrdemDeServico.xhtml";
    }

    public String doFinishAdd() {
        try {
            if (status == null || EnumStatusOrdemDeServico.retornaEnumSelecionado(status) == null) {
                throw new CadastroException("Selecione um status", null);
            }
            if (!osSelecionada.getItensOrdemDeServico().isEmpty()) {
                osSelecionada = ordemDeServicoService.salvar(osSelecionada, status, getUsuarioLogado());
                createFacesSuccessMessage("Ordem de servico salva com sucesso!");
                return "listaOrdemDeServico.xhtml";
            }
            createFacesErrorMessage("Informe ao menos um item para efetuar a ordem de serviço");
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "cadastroOrdemDeServico.xhtml";
    }

    public String doCloseOS() {
        if (getUsuarioLogado().getIdFuncionario() == null) {
            createFacesErrorMessage("O usuário atual não é um funcionário. Não será possível finalizar a OS.");
        } else {
            osSelecionada.setIdFuncionarioSaida(getUsuarioLogado().getIdFuncionario());
            ordemDeServicoService.salvar(osSelecionada);
            createFacesSuccessMessage("Ordem de serviço finalizada com sucesso.");
        }
        return "listaOrdemDeServico.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaOrdemDeServico.xhtml";
    }

    public String doFinishExcluir() {
        ordemDeServicoService.remover(osSelecionada);
        return "listaOrdemDeServico.xhtml";
    }

    public String doAddItem() {
        controleEdicao = null;
        osSelecionada.getItensOrdemDeServico().add(new ItensOrdemDeServico());
        return "cadastroOrdemDeServico.xhtml";// teste
    }

    public String doRemoveItem(ItensOrdemDeServico item) {
        if (osSelecionada.getItensOrdemDeServico().contains(item)) {
            osSelecionada.getItensOrdemDeServico().remove(item);
        }
        return "cadastroOrdemDeServico.xhtml";
    }

    public void doToggleAddCliente() {
        cadastraCliente = !cadastraCliente;
        this.cliente = new Cliente();
    }

    public void doFinishAddCliente() {
        if (cliente.getCpfCNPJ() == null) {
            createFacesErrorMessage("CPF/CNPJ inválido.");
            return;
        }
        try {
            cliente.setCpfCNPJ(CpfCnpjUtil.removerMascaraCnpj(cliente.getCpfCNPJ()));
            if (CpfCnpjUtil.validarCPF(cliente.getCpfCNPJ())) {
                cliente.setTipo("PF");
                cliente.setCpfCNPJ(CpfCnpjUtil.mascararCpf(cliente.getCpfCNPJ()));
            } else if (CpfCnpjUtil.validarCNPJ(cliente.getCpfCNPJ())) {
                cliente.setTipo("PJ");
                cliente.setCpfCNPJ(CpfCnpjUtil.mascararCnpj(cliente.getCpfCNPJ()));
            } else {
                throw new CadastroException("CPF/CNPJ inválido", null);
            }
            cliente = clienteService.salvar(cliente);
            osSelecionada.setIdCliente(cliente);
            cadastraCliente = false;
            cliente = null;
        } catch (CadastroException e) {
            createFacesErrorMessage("Não foi possível salvar o cliente.");
        }
    }

    public void doCancelAddCliente() {
        cleanCliente();
    }

    public List<Object> getDadosAuditoria() {
        return ordemDeServicoService.getDadosAuditoriaByID(osSelecionada);
    }

    public String getStatusLabel() {
        if (null == status || status.isEmpty()) {
            return "Sem status";
        }
        return EnumStatusOrdemDeServico.retornaEnumSelecionado(status).getDescricao();
    }

    public String getEnumFormaPagamentoDesc(String cod) {
        if (cod == null) {
            return "";
        }
        return EnumFormaPagamento.retornaEnumSelecionado(cod).getDescricao();
    }

    public String getEnumStatusOrdemDeServicoDesc(String cod) {
        if (cod == null) {
            return "";
        }
        return EnumStatusOrdemDeServico.retornaEnumSelecionado(cod).getDescricao();
    }

    public void addItem() {
        if (controleEdicao == null) {
            itemSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
            osSelecionada.getItensOrdemDeServico().add(itemSelecionado);
        } else {
            for (int i = 0; i < osSelecionada.getItensOrdemDeServico().size(); i++) {
                ItensOrdemDeServico item = osSelecionada.getItensOrdemDeServico().get(i);
                if (item.getControle().equals(controleEdicao)) {
                    osSelecionada.getItensOrdemDeServico().set(i, itemSelecionado);
                    controleEdicao = null;
                    break;
                }
            }
        }
        itemSelecionado = new ItensOrdemDeServico();
        controleEdicao = null;
    }

    public void editItem(ItensOrdemDeServico item) {
        controleEdicao = item.getControle();
        itemSelecionado = item;
    }

    public void limpaControle() {
        controleEdicao = null;
        itemSelecionado = new ItensOrdemDeServico();
    }

    public boolean existeParcelaPaga() {
        return contaService.existeParcelaPaga(osSelecionada);
    }

    public boolean existeParcelaPaga(OrdemDeServico os) {
        return contaService.existeParcelaPaga(os);
    }

}
