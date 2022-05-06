package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.ServicoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ServicoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ServicoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoCadastro;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicoService extends BasicService<Servico, ServicoRepositorio, ServicoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    transient ServicoMapper servicoMapper;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private ProdutoService produtoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ServicoRepositorio(em, adHocTenant);
    }

    @Override
    public Servico salvar(Servico servico) {
        PlanoConta planoConta;

        if (servico.getAtivo() == null) {
            servico.setAtivo("S");
        }
        boolean nomeRepetido = repositorio.listar().stream()
                .filter(s -> s.getAtivo().equals("S"))
                .filter(s -> s.getDescricao().equals(servico.getDescricao()))
                .anyMatch(s -> servico.getId() == null || !servico.getId().equals(s.getId()));
        if (nomeRepetido && servico.getAtivo().equals("S")) {
            throw new CadastroException("Serviço já cadastrado. Favor alterar o nome.", null);
        }

        if (servico.getFuncionarioServicoList() != null) {
            servico.getFuncionarioServicoList().forEach(item -> item.setTenatID(adHocTenant.getTenantID()));
        }
        if (servico.getServicoProdutoList() != null) {
            servico.getServicoProdutoList().forEach(item -> {
                item.setTenatID(adHocTenant.getTenantID());
                item.setIdServico(servico);
            });
        }
        if (servico.getId() == null) {
            servico.setTenatID(adHocTenant.getTenantID());

            planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(servico.getDescricao(), EnumTipoCadastro.SERVICO.getTipo());
            servico.setIdPlanoConta(planoConta);

            return adicionar(servico);
        } else {
            if (servico.getIdPlanoConta() != null) {
                servico.getIdPlanoConta().setDescricao(servico.getDescricao());
                planoContaPadraoService.alterar(servico.getIdPlanoConta());
            } else {
                planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(servico.getDescricao(), EnumTipoCadastro.SERVICO.getTipo());
                servico.setIdPlanoConta(planoConta);
            }

            return alterar(servico);
        }
    }

    @Override
    public void remover(Servico servico) {
        //Busca um plano de conta com o id do mesmo no serviço
        PlanoConta planoConta = planoContaPadraoService.buscar(servico.getIdPlanoConta().getId());

        //Verifica se o mesmo está preenchido para ai sim, removê-lo.
        if (planoConta != null) {
            planoContaPadraoService.remover(planoConta);
        }

        super.remover(servico);
    }

    @Override
    public Criteria getModel(ServicoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        addEqRestrictionTo(criteria, "idProdutoCategoria", filter.getIdProdutoCategoria());
        addEqRestrictionTo(criteria, "custoServico", filter.getCustoServico());
        addEqRestrictionTo(criteria, "valorVenda", filter.getValorVenda());
        if (filter.getIdFuncionario() != null) {
            criteria.createAlias("funcionarioServicoList", "funcionarioServicoList");
            addEqRestrictionTo(criteria, "funcionarioServicoList.idFuncionario", filter.getIdFuncionario());
        }

        return criteria;
    }

    public Servico buscarServico(String descricao) {
        return repositorio.buscarServico(descricao);
    }

    public Servico importDto(ServicoDTO servicoDTO, String tenatID) {
        Servico servico = servicoMapper.toEntity(servicoDTO);
        ProdutoCategoria pc = produtoService.buscarCategoria(servicoDTO.getCategoriaImportacao());
        if (pc == null) {
            pc = new ProdutoCategoria();
            pc.setDescricao(servicoDTO.getCategoriaImportacao());
            pc.setTenatID(tenatID);
            pc.setAtivo("S");
            pc = produtoService.adicionarCategoria(pc);
        }
        servico.setIdProdutoCategoria(pc);
        servico.setTenatID(tenatID);
        servico.setAtivo("S");
        return salvar(servico);
    }

    public List<FuncionarioServico> listarFuncionarioServico(Servico servico) {
        return repositorio.listarFuncionarioServico(servico);
    }

    public List<ServicoProduto> listarServicoProduto(Servico servico) {
        return repositorio.listarServicoProduto(servico);
    }

    public List<Servico> listarServicosAtivos() {
        return repositorio.listarServicosAtivos();
    }

}
