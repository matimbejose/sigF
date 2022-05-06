package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PagamentoMensalidadeModuloFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PagamentoMensalidadeModuloRepositorio;
import java.util.ArrayList;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PagamentoMensalidadeModuloService extends BasicService<PagamentoMensalidadeModulo, PagamentoMensalidadeModuloRepositorio, PagamentoMensalidadeModuloFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PagamentoMensalidadeModuloRepositorio(em, adHocTenant);
    }

    public List<PagamentoMensalidadeModulo> getModulosPor(PagamentoMensalidade pm) {
        return repositorio.getPagamentoMensalidadeModuloPor(pm);
    }

    public List<PagamentoMensalidadeModulo> getModulosPor(Empresa empresa) {
        if (empresa == null) {
            return new ArrayList<>();
        }
        return getModulosPor(pagamentoMensalidadeService.getUltimoPagamentoPor(empresa));
    }

    public List<PagamentoMensalidadeModulo> getPagamentoMensalidadeModuloPor(Modulo modulo) {
        return repositorio.getPagamentoMensalidadeModuloPor(modulo);
    }

}
