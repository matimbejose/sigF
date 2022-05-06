package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaContatoCliente;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaContatoClienteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.EmpresaContatoClienteRepositorio;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.EmpresaRepositorio;
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
import org.hibernate.criterion.Criterion;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpresaContratoClienteService extends BasicService<EmpresaContatoCliente, EmpresaContatoClienteRepositorio, BasicFilter<EmpresaContatoCliente>> {

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
        repositorio = new EmpresaContatoClienteRepositorio(em, adHocTenant);
    }

    public int getQuantidadeRegistrosFiltradosEmpresaContatoCliente(List<Criterion> criterions, Boolean usaTenant) {
        return repositorio.getQuantidadeRegistrosFiltradosEmpresaContatoCliente(criterions, EmpresaRepositorio.NAO_USA_TENANT);
    }

    public List<EmpresaContatoCliente> getListaFiltradaEmpresaContatoCliente(List<Criterion> criterions, EmpresaContatoClienteFiltro filtro, Boolean usaTenant) {
        return repositorio.getListaFiltradaEmpresaContatoCliente(criterions, filtro, EmpresaRepositorio.NAO_USA_TENANT);
    }

}
