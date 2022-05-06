package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControleFinanceiroDTO;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FinanceiroRepositorio;
import java.util.List;
import javax.annotation.PostConstruct;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FinanceiroService extends BasicService<EntityId, FinanceiroRepositorio, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new FinanceiroRepositorio(em, adHocTenant);
    }

    public List<ControleFinanceiroDTO> listarRecebimentos(Integer mes, Integer ano, String nome) {
        return repositorio.listar(mes, ano, nome, "C");
    }

    public List<ControleFinanceiroDTO> listarGastos(Integer mes, Integer ano, String nome) {
        return repositorio.listar(mes, ano, nome, "D");
    }

}
