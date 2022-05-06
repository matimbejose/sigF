package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.ItensOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.StatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.OrdemDeServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.OrdemDeServicoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrdemDeServicoService extends BasicService<OrdemDeServico, OrdemDeServicoRepositorio, OrdemDeServicoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @EJB
    private PlanoContaLancamentoService planoContaLancamentoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new OrdemDeServicoRepositorio(em, adHocTenant);
    }

    public List<OrdemDeServico> listarPorFunctionarioEntrada(Funcionario funcionario) {
        return repositorio.listarPorFunctionarioEntrada(funcionario);
    }

    public List<OrdemDeServico> listarPorFunctionarioSaida(Funcionario funcionario) {
        return repositorio.listarPorFunctionarioSaida(funcionario);
    }

    public List<OrdemDeServico> listarPorEmail(String email) {
        return repositorio.listarPorEmail(email);
    }

    public List<ItensOrdemDeServico> listarItens(OrdemDeServico os) {
        return repositorio.listarItens(os);
    }

    public List<ItensOrdemDeServico> listarItensPorDescricao(OrdemDeServico os, String descricao) {
        return repositorio.listarItensPorDescricao(os, descricao);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatus(OrdemDeServico os) {
        return repositorio.listarMudancasDeStatus(os);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatusPorFuncionario(OrdemDeServico os, Funcionario funcionario) {
        return repositorio.listarMudancasDeStatusPorFuncionario(os, funcionario);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatusPorStatus(OrdemDeServico os, String status) {
        return repositorio.listarMudancasDeStatusPorStatus(os, status);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatusPorStatus(OrdemDeServico os, EnumStatusOrdemDeServico status) {
        return repositorio.listarMudancasDeStatusPorStatus(os, status);
    }

    @Override
    public Criteria getModel(OrdemDeServicoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);
        addEqRestrictionTo(criteria, "numero", filter.getNumero());
        addEqRestrictionTo(criteria, "idCliente", filter.getCliente());
        addIlikeRestrictionTo(criteria, "idCliente.email", filter.getEmail(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "idCliente.telefoneCelular", filter.getTelefone(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "formaPagamento", filter.getFormaPagamento(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "funcionarioEntrada", filter.getFuncionarioEntrada());
        addEqRestrictionTo(criteria, "funcionarioSaida", filter.getFuncionarioSaida());

        addRestrictionTo(criteria, "valor", filter.getValor());
        addRestrictionTo(criteria, "horario", filter.getData());

        return criteria;
    }

    @Override
    public void remover(OrdemDeServico os) {
        List<ContaParcela> listParcela = contaService.listarContaParcela(os.getIdConta());

        PlanoContaLancamentoContabil plano = planoContaLancamentoService.buscarLancamentoContabilPorConta(os.getIdConta());
        if (plano != null) {
            planoContaLancamentoService.remover(plano);
        }

        // Remove primeiro todas as parcelas .
        if (!listParcela.isEmpty()) {
            listParcela.forEach(contaService::removerContaParcela);
        }

        // Remove o contrato (OBS.: deleta conta por cascade)
        super.remover(os);
    }

    public OrdemDeServico salvar(final OrdemDeServico os, String status, Usuario usuarioLogado) {
        os.getItensOrdemDeServico().stream().forEach(item -> {
            item.setIdOrdemDeServico(os);
            item.setTenatID(adHocTenant.getTenantID());
        });
        os.getStatusOrdemDeServico().stream().forEach(item -> item.setTenatID(adHocTenant.getTenantID()));

        Conta conta = os.getIdConta();
        conta.setIdCliente(os.getIdCliente());
        conta.setNumeroParcelas(1);
        if (os.getFormaPagamento().matches("\\d+x")) {
            Integer quantidade = Integer.parseInt(os.getFormaPagamento().substring(0, os.getFormaPagamento().length() - 1));
            conta.setNumeroParcelas(quantidade);
        }
        conta.setValor(os.getValor());
        conta.setDataVencimento(DataUtil.adicionarMes(new Date(), 1));
        conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
        conta = contaService.salvarConta(conta);

        os.setIdConta(conta);
        salvar(os);// Tem que salvar antes de chamar o listarMudancasDeStatus
        List<StatusOrdemDeServico> listaStatus = listarMudancasDeStatus(os);
        boolean atualizarStatus = true;
        if (!listaStatus.isEmpty()) {
            String statusAtual = listaStatus.get(listaStatus.size() - 1).getStatus();
            atualizarStatus = !status.equals(statusAtual);
        }
        if (atualizarStatus) {
            listaStatus.add(new StatusOrdemDeServico(status, usuarioLogado.getIdFuncionario(), os, adHocTenant.getTenantID()));
            os.setStatusOrdemDeServico(listaStatus);
        }
        os.setHorario(new Date());
        return salvar(os);
    }

}
