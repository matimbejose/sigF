package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ItemSecao;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VistoriaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ItemSecaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ItemSecaoRepositorio;
import javax.ejb.LocalBean;
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
public class ItemSecaoService extends BasicService<ItemSecao, ItemSecaoRepositorio, ItemSecaoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    VistoriaMapper vistoriaMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new ItemSecaoRepositorio(em, adHocTenant);
    }

    @Override
    public ItemSecao salvar(ItemSecao avaria) {
        boolean nomeRepetido = listar().stream()
                .filter(s -> s.getDescricao().equals(avaria.getDescricao()))
                .anyMatch(s -> avaria.getId() == null || !avaria.getId().equals(s.getId()));
        if (nomeRepetido && avaria.getAtivo().equals("S")) {
            throw new CadastroException("Item de seção já cadastrado. Favor alterar o nome.", null);
        }
        return super.salvar(avaria);
    }

    @Override
    public Criteria getModel(ItemSecaoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

}
