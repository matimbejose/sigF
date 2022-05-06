package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Fabricante;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FabricanteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FabricanteMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FabricanteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FabricanteRepositorio;
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
public class FabricanteService extends BasicService<Fabricante, FabricanteRepositorio, FabricanteFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    private FabricanteMapper fabricanteMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new FabricanteRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(FabricanteFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    @Override
    public Fabricante salvar(Fabricante fabricante) {
        boolean nomeRepetido = listar().stream()
                .filter(s -> s.getAtivo().equals("S"))
                .filter(s -> s.getDescricao().equals(fabricante.getDescricao()))
                .anyMatch(s -> fabricante.getId() == null || !fabricante.getId().equals(s.getId()));
        if (nomeRepetido && fabricante.getAtivo().equals("S")) {
            throw new CadastroException("Fabricante já cadastrado. Favor alterar o nome.", null);
        }
        fabricante.setTenatID(adHocTenant.getTenantID());
        return super.salvar(fabricante);
    }

    public Fabricante findByDescricao(String descricao) {
        FabricanteFiltro ff = new FabricanteFiltro();
        ff.setDescricao(descricao);
        List<Fabricante> lista = getModel(ff).list();
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Fabricante importDto(FabricanteDTO clienteDTO, String tenat) {
        Fabricante fabricante = fabricanteMapper.toEntity(clienteDTO);
        fabricante.setTenatID(tenat);
        fabricante.setAtivo("S");
        return salvar(fabricante);
    }

}
