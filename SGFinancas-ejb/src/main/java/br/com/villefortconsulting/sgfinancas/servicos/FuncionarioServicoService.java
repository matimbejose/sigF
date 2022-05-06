package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncionarioServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FuncionarioServicoRepositorio;
import java.util.List;
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
import org.hibernate.criterion.Example;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionarioServicoService extends BasicService<FuncionarioServico, FuncionarioServicoRepositorio, FuncionarioServicoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new FuncionarioServicoRepositorio(em);
    }

    public List<FuncionarioServico> getListaByFuncionario(Funcionario funcionario) {
        return repositorio.getListaByFuncionario(funcionario);
    }

    public List<FuncionarioServico> getListaByServico(Servico servico) {
        return repositorio.getListaByServico(servico);
    }

    @Override
    public Criteria getModel(FuncionarioServicoFiltro filter) {
        Criteria criteria = super.getModel(filter);
        criteria.add(Example.create(filter.getExample()));
        return criteria;
    }

}
