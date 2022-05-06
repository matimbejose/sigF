package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioResposta;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioRespostaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VistoriaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormularioRespostaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FormularioRespostaRepositorio;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormularioRespostaService extends BasicService<FormularioResposta, FormularioRespostaRepositorio, FormularioRespostaFiltro> {

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
        repositorio = new FormularioRespostaRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(FormularioRespostaFiltro filter) {
        Criteria criteria = super.getModel(filter);
        addEqRestrictionTo(criteria, "idCliente", filter.getCliente());

        return criteria;
    }

    public boolean temRespostaParaO(Formulario formulario) {
        return repositorio.temRespostaParaO(formulario);
    }

    public FormularioResposta importDto(FormularioRespostaDTO dto, String tenatId) {
        FormularioResposta formularioResposta = vistoriaMapper.toEntity(dto);
        formularioResposta.setTenatID(tenatId);
        formularioResposta.getFormularioRespostaItemSecaoList()
                .forEach(fris -> fris.setTenatID(tenatId));
        return salvar(formularioResposta);
    }

}
