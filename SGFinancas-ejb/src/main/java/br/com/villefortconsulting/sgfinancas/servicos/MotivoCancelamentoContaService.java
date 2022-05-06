package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.MotivoCancelamentoConta;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MotivoCancelamentoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.MotivoCancelamentoContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.MotivoCancelamentoContaRepositorio;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoCancelamentoContaService extends BasicService<MotivoCancelamentoConta, MotivoCancelamentoContaRepositorio, MotivoCancelamentoContaFiltro> {

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
        repositorio = new MotivoCancelamentoContaRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(MotivoCancelamentoContaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    public MotivoCancelamentoConta findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }

    public MotivoCancelamentoConta importDto(MotivoCancelamentoContaDTO dto, String tenat) {
        MotivoCancelamentoConta mcc = new MotivoCancelamentoConta();
        mcc.setAtivo("S");
        mcc.setDescricao(dto.getDescricao());
        return salvar(mcc);
    }

}
