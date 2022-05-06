package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.CategoriaEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModuloPacoteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ModuloPacoteRepositorio;
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
public class ModuloPacoteService extends BasicService<ModuloPacote, ModuloPacoteRepositorio, ModuloPacoteFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ModuloPacoteRepositorio(em);
    }

    @Override
    public Criteria getModel(ModuloPacoteFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    public ModuloPacote buscarPor(CategoriaEmpresa categoriaEmpresa) {
        return repositorio.buscarPor(categoriaEmpresa);
    }

}
