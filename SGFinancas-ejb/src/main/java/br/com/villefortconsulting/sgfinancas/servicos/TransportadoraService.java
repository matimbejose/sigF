package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.Transportadora;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TransportadoraFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TransportadoraRepositorio;
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
public class TransportadoraService extends BasicService<Transportadora, TransportadoraRepositorio, TransportadoraFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private NFService nfService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new TransportadoraRepositorio(em, adHocTenant);
    }

    @Override
    public Transportadora salvar(Transportadora transportadora) {
        PlanoConta planoConta;
        transportadora.setAtivo("S");
        if (transportadora.getId() == null) {
            planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(transportadora.getDescricao(), EnumTipoCadastro.TRANSPORTADORA.getTipo());
            transportadora.setIdPlanoConta(planoConta);

            return adicionar(transportadora);
        } else {

            if (transportadora.getIdPlanoConta() != null) {
                transportadora.getIdPlanoConta().setDescricao(transportadora.getDescricao());
                planoContaPadraoService.alterar(transportadora.getIdPlanoConta());
            } else {
                planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(transportadora.getDescricao(), EnumTipoCadastro.TRANSPORTADORA.getTipo());
                transportadora.setIdPlanoConta(planoConta);
            }

            return alterar(transportadora);
        }
    }

    @Override
    public void remover(Transportadora transportadora) {
        transportadora.setAtivo("N");
        super.salvar(transportadora);
    }

    public Cidade buscarCidade(int id) {
        return repositorio.buscarCidade(id);
    }

    public List<Cidade> listarCidade() {
        return repositorio.listarCidade();
    }

    @Override
    public Criteria getModel(TransportadoraFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "cnpj", filter.getCnpj(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    public Transportadora findByCnpjDescricao(String cnpj, String descricao) {
        return repositorio.findByCnpjDescricao(cnpj, descricao);
    }

}
