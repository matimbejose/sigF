package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncionarioAtendimentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FuncionarioAtendimentoRepositorio;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionarioAtendimentoService extends BasicService<FuncionarioAtendimento, FuncionarioAtendimentoRepositorio, FuncionarioAtendimentoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new FuncionarioAtendimentoRepositorio(em);
    }

    public List<FuncionarioAtendimento> getListaByFuncionario(Funcionario funcionario) {
        return repositorio.getListaByFuncionario(funcionario);
    }

}
