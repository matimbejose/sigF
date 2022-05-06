package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.*;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaBancariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ContaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaBancariaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ContaBancariaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoContaBancaria;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoCadastro;
import br.com.villefortconsulting.util.DataUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaBancariaService extends BasicService<ContaBancaria, ContaBancariaRepositorio, ContaBancariaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    ContaMapper contaMapper;

    @EJB
    private ExtratoContaCorrenteService extratoService;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private BancoService bancoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")

    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ContaBancariaRepositorio(em, adHocTenant);
    }

    @Override
    public ContaBancaria salvar(ContaBancaria contaBancaria) {
        PlanoConta planoConta;

        String descricao = contaBancaria.getDescricao();
        Integer id = contaBancaria.getId();
        boolean nomeRepetido = listar().stream()
                .filter(c -> descricao.equals(c.getDescricao()))
                .anyMatch(c -> id == null || !id.equals(c.getId()));
        if (nomeRepetido) {
            throw new CadastroException("Existe uma conta bancária cadastrada para a descrição informada.", null);
        }

        if (contaBancaria.getId() == null) {
            planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(contaBancaria.getDescricao(), EnumTipoCadastro.CONTA_BANCARIA.getTipo());
            planoContaPadraoService.criaPlanoContaTransferenciaCreditoDebito(planoConta);
            contaBancaria.setIdPlanoConta(planoConta);
            contaBancaria.setSituacao(EnumSituacaoContaBancaria.ATIVA.getSituacao());
            adicionar(contaBancaria);

            contaBancaria = findByDescricao(contaBancaria.getDescricao());

            extratoService.criarExtratoSaldoInicial(contaBancaria);

            return super.salvar(contaBancaria);
        } else {
            extratoService.alterarSaldoInicialExtrato(contaBancaria);

            if (contaBancaria.getIdPlanoConta() != null) {
                contaBancaria.getIdPlanoConta().setDescricao(contaBancaria.getDescricao());
                if (!planoContaPadraoService.verificaExistenciaPlanoContaTransferenciaECria(contaBancaria.getIdPlanoConta())) {
                    planoContaPadraoService.criaPlanoContaTransferenciaCreditoDebito(contaBancaria.getIdPlanoConta());
                }
                planoContaPadraoService.alterar(contaBancaria.getIdPlanoConta());
            } else {
                planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(contaBancaria.getDescricao(), EnumTipoCadastro.CONTA_BANCARIA.getTipo());
                planoContaPadraoService.criaPlanoContaTransferenciaCreditoDebito(planoConta);
                contaBancaria.setIdPlanoConta(planoConta);
            }

            return alterar(contaBancaria);
        }
    }

    public void alteraExtrato(ContaBancaria contaBancaria) {
        extratoService.alterarSaldoInicialExtrato(contaBancaria);
    }

    public List<ContaBancaria> listarAtivas() {
        return repositorio.listarAtivas();
    }

    public List<ContaBancaria> listarContasBancarias() {
        return repositorio.listarContasBancarias();
    }

    public void cancelarConta(ContaBancaria conta) {
        conta.setSituacao(EnumSituacaoContaBancaria.CANCELADA.getSituacao());
        conta.setDataCancelamento(DataUtil.getHoje());

        alterar(conta);

        //Após cancelar a conta bancaria, removerá a mesma do intem de plano de conta.
        PlanoConta planoConta = planoContaPadraoService.buscar(conta.getIdPlanoConta().getId());

        if (planoConta != null) {
            planoContaPadraoService.remover(planoConta);
        }
    }

    @Override
    public Criteria getModel(ContaBancariaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        }
        if (filter.getBanco() != null && filter.getBanco().getId() != null) {
            addEqRestrictionTo(criteria, "idBanco", filter.getBanco());
        }
        if (StringUtils.isNotEmpty(filter.getTipoConta())) {
            addEqRestrictionTo(criteria, "tipoConta", filter.getTipoConta());
        }
        if (StringUtils.isNotEmpty(filter.getSituacao())) {
            addEqRestrictionTo(criteria, "situacao", filter.getSituacao());
        }
        if (StringUtils.isNotEmpty(filter.getAgencia())) {
            addIlikeRestrictionTo(criteria, "agencia", filter.getAgencia(), MatchMode.ANYWHERE);
        }
        if (StringUtils.isNotEmpty(filter.getConta())) {
            addIlikeRestrictionTo(criteria, "conta", filter.getConta(), MatchMode.ANYWHERE);
        }
        return criteria;
    }

    public boolean existeContaCorrenteInformada() {
        return repositorio.existeContaCorrenteInformada();
    }

    public List<ContaBancaria> listarContasPorEmpresa(Empresa empresa) {
        return repositorio.listarPorEmpresa(empresa);
    }

    public ContaBancaria getContaBancaria() {
        return repositorio.obterContaBancaria();
    }

    public ContaBancaria buscarContaBancariaById(Integer id) {
        return repositorio.buscarContaBancariaById(id);
    }

    public ContaBancaria buscarContaBancaria(String agencia, String conta) {
        return repositorio.buscarContaBancaria(agencia.split("-")[0], conta.split("-")[0]);
    }

    public List<ContaBancaria> listarContasByBanco(Banco banco) {
        return repositorio.listarContasByBanco(banco);
    }

    public ContaBancaria buscarContaBancaria(String agencia, String conta, String tenant) {
        return repositorio.buscarContaBancaria(agencia.split("-")[0], conta.split("-")[0], tenant);
    }

    public ContaBancaria getContaBancariaPadrao() {
        return getContaBancariaPadrao((Integer) null);
    }

    public ContaBancaria getContaBancariaPadrao(CompraDTO obj) {
        return getContaBancariaPadrao(obj != null ? obj.getPlanoConta() : null);
    }

    public ContaBancaria getContaBancariaPadrao(VendaCadastroRestDTO obj) {
        return getContaBancariaPadrao(obj != null ? obj.getContaBancaria() : null);
    }

    public ContaBancaria getContaBancariaPadrao(PlanoContaDTO obj) {
        return getContaBancariaPadrao(obj != null ? obj.getId() : null);
    }

    public ContaBancaria getContaBancariaPadrao(Integer id) {
        ParametroSistema ps = parametroSistemaService.getParametro();
        ContaBancaria cb = null;
        if (id == null) {
            cb = ps.getAppContaBancariaPadrao();
            if (cb == null) {
                cb = buscarContaBancaria("001", "001");
            }
        } else {
            try {
                cb = buscar(id);
            } catch (Exception ex) {
                Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (cb == null) {
            List<ContaBancaria> contas = listar();
            if (contas.size() == 1) {
                cb = contas.get(0);
            }
        }
        if (cb == null) {
            throw new CadastroException("Conta bancária não encontrada ou não cadastrada no sistema.", null);
        }
        return cb;
    }

    public ContaBancaria findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }

    public void populaContaBancariaPadrao(Empresa empresa) {
        ContaBancaria conta = new ContaBancaria();
        Banco banco = bancoService.buscarBancoByDescricao("Caixa Interno");
        conta.setAgencia("001");
        conta.setConta("001");
        conta.setSaldoInicial(0d);
        conta.setDescricao("Caixa Interno");
        conta.setDataSaldo(DataUtil.getHoje());
        conta.setTipoConta("C");
        conta.setIdBanco(banco);
        conta.setTenatID(empresa.getTenatID());

        salvar(conta);

    }

    public ContaBancaria importDto(ContaBancariaDTO contaDTO, String tenat) {
        ContaBancaria conta = contaMapper.toEntity(contaDTO);
        if (conta.getIdBanco() == null) {
            conta.setIdBanco(bancoService.buscar(contaDTO.getIdBanco()));
        }
        conta.setTenatID(tenat);

        return salvar(conta);
    }

    public List<ContaBancaria> listarContasByCentroCusto(CentroCusto centro) {
        if (centro == null) {
            return new ArrayList<>();
        }
        return repositorio.listarContasByCentroCusto(centro);
    }

}
