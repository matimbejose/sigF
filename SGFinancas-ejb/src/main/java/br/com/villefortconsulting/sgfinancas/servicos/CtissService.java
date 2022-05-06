package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CtissFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CtissRepositorio;
import java.util.List;
import javax.ejb.EJB;
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
public class CtissService extends BasicService<Ctiss, CtissRepositorio, CtissFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private CnaeService cnaeService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CtissRepositorio(em);
    }

    public List<Ctiss> listar(Cidade cidade) {
        return repositorio.listar(cidade);
    }

    @Override
    public Criteria getModel(CtissFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "idCidade", filter.getCidade());
        addIlikeRestrictionTo(criteria, "subitem", filter.getSubItem(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "aliquota", filter.getAliquota());

        return criteria;
    }

    public List<Ctiss> listarCtissRelacionadoCnaeEmpresa(Empresa empresa, Cidade cidade) {
        return repositorio.listar(cidade, cnaeService.listarTodosCnaesEmpresa(empresa));
    }

    public boolean empresaPossuiCtiss(Empresa empresa) {
        List<Cnae> listCnae = cnaeService.listarTodosCnaesEmpresa(empresa);

        if (!listCnae.isEmpty()) {
            return repositorio.empresaPossuiCtiss(listCnae);
        } else {
            return false;
        }
    }

}
