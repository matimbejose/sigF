package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.ContabilidadePlanoConta;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ContabilidadePlanoContaRepositorio;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContabilidadePlanoContaService extends BasicService<ContabilidadePlanoConta, ContabilidadePlanoContaRepositorio, BasicFilter<ContabilidadePlanoConta>> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new ContabilidadePlanoContaRepositorio(em);
    }

    public void salvarContabilidadePlanoConta(ContabilidadePlanoConta contabilidade) {
        salvar(contabilidade);
    }

    public List<ContabilidadePlanoConta> listarPorContabilidade(Contabilidade contabilidade) {
        return repositorio.listarContabilidadePlanoConta(contabilidade);
    }

}
