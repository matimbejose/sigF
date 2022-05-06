package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Avaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AvariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VistoriaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AvariaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.AvariaRepositorio;
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
public class AvariaService extends BasicService<Avaria, AvariaRepositorio, AvariaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    VistoriaMapper vistoriaMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new AvariaRepositorio(em, adHocTenant);
    }

    @Override
    public Avaria salvar(Avaria avaria) {
        boolean nomeRepetido = listar().stream()
                .filter(s -> s.getDescricao().equals(avaria.getDescricao()))
                .anyMatch(s -> avaria.getId() == null || !avaria.getId().equals(s.getId()));
        if (nomeRepetido && avaria.getAtivo().equals("S")) {
            throw new CadastroException("Avaria já cadastrada. Favor alterar o nome.", null);
        }
        return super.salvar(avaria);
    }

    @Override
    public Criteria getModel(AvariaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        addEqRestrictionTo(criteria, "codigo", filter.getCodigo());

        return criteria;
    }

    public Avaria buscarAvariaPorDescricao(String descricao) {
        return repositorio.buscarAvariaPorDescricao(descricao);
    }

    public Avaria importDto(AvariaDTO dto, String tenatId) {
        Avaria avaria = vistoriaMapper.toEntity(dto);
        avaria.setTenatID(tenatId);
        if (avaria.getAtivo() == null) {
            avaria.setAtivo("S");
        }
        return salvar(avaria);
    }

}
