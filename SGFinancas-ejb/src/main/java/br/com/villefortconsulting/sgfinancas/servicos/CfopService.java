package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CfopFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CfopRepositorio;
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
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CfopService extends BasicService<Cfop, CfopRepositorio, CfopFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CfopRepositorio(em);
    }

    public List<Cfop> listarCfopDeDevolucao() {
        return repositorio.listarCfopDeDevolucao();
    }

    public List<Cfop> listarCfopDeTransporte() {
        return repositorio.listarCfopDeTransporte();
    }

    public List<Cfop> listarCfopDeComunicacao() {
        return repositorio.listarCfopDeComunicacao();
    }

    public List<Cfop> listarCfopDeRetorno() {
        return repositorio.listarCfopDeRetorno();
    }

    public List<Cfop> listarCfopDeAnulacao() {
        return repositorio.listarCfopDeAnulacao();
    }

    public List<Cfop> listarCfopDeRemessa() {
        return repositorio.listarCfopDeRemessa();
    }

    public List<Cfop> listarCfopDeVenda() {
        return repositorio.listarCfopVenda();
    }

    public List<Cfop> listarCfopDeServico() {
        return repositorio.listarCfopServico();
    }

    public List<Cfop> listarCfopDeTransferencia() {
        return repositorio.listarCfopDeTransferencia();
    }

    public List<Cfop> listarCfopDeCompra() {
        return repositorio.listarCfopCompra();
    }

    @Override
    public Criteria getModel(CfopFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "aplicacao", filter.getAplicacao(), MatchMode.ANYWHERE);

        return criteria;
    }

}
