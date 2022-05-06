package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.PontoVenda;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PontoVendaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PontoVendaRepositorio;
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
public class PontoVendaService extends BasicService<PontoVenda, PontoVendaRepositorio, PontoVendaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    private AdHocTenant adHocTenant;

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PontoVendaRepositorio(em, adHocTenant);
    }

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

}
