package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModuloFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ModuloRepositorio;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ModuloService extends BasicService<Modulo, ModuloRepositorio, ModuloFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ModuloRepositorio(em);
    }

    @Override
    public Criteria getModel(ModuloFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        addEqRestrictionTo(criteria, "permiteRenovacao", filter.getPermiteRenovacao());
        addEqRestrictionTo(criteria, "obrigatorio", filter.getObrigatorio());

        return criteria;
    }

    public Double valorAdesaoObrigatorio() {
        return repositorio.valorAdesaoObrigatorio();
    }

    public Double valorMensalidadeObrigatorio() {
        return repositorio.valorMensalidadeObrigatorio();
    }

    public List<Permissao> permissoesPor(Modulo modulo) {
        return repositorio.permissoesPor(modulo);
    }

}
