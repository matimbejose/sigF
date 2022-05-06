package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VistoriaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormularioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FormularioRepositorio;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormularioService extends BasicService<Formulario, FormularioRepositorio, FormularioFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    VistoriaMapper vistoriaMapper;

    @EJB
    private FormularioRespostaService formularioRespostaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new FormularioRepositorio(em, adHocTenant);
    }

    @Override
    public Formulario salvar(Formulario formulario) {
        boolean nomeRepetido = listar().stream()
                .filter(s -> s.getDescricao().equals(formulario.getDescricao()))
                .anyMatch(s -> formulario.getId() == null || !formulario.getId().equals(s.getId()));
        if (nomeRepetido) {
            throw new CadastroException("Formulario já cadastrado. Favor alterar o nome.", null);
        }

        if (formularioRespostaService.temRespostaParaO(formulario)) {
            throw new CadastroException("Não é possível alterar um formulário que já foi respondido.", null);
        }
        formulario.getFormularioSecaoList().stream()
                .forEach(secao -> {
                    secao.setTenatID(adHocTenant.getTenantID());
                    secao.getSecaoItemSecaoList().stream()
                            .forEach(item -> {
                                item.setTenatID(adHocTenant.getTenantID());
                                item.setIdSecao(secao);
                                item.getItemSecaoSubItemSecaoList().stream()
                                        .forEach(subItem -> {
                                            subItem.setTenatID(adHocTenant.getTenantID());
                                            subItem.setIdItemSecao(item);
                                        });
                            });
                });
        return super.salvar(formulario);
    }

    @Override
    public Criteria getModel(FormularioFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    public Formulario buscarFormularioPorDescricao(String descricao) {
        return repositorio.buscarFormularioPorDescricao(descricao);
    }

    public Formulario importDto(FormularioDTO dto, String tenatId) {
        Formulario formulario = vistoriaMapper.toEntity(dto);
        formulario.setTenatID(tenatId);
        if (formulario.getAtivo() == null) {
            formulario.setAtivo("S");
        }
        return salvar(formulario);
    }

}
