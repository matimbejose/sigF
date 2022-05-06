package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormaPagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.FormaPagamentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormaPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FormaPagamentoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import java.util.Arrays;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormaPagamentoService extends BasicService<FormaPagamento, FormaPagamentoRepositorio, FormaPagamentoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    transient FormaPagamentoMapper formaPagamentoMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new FormaPagamentoRepositorio(em, adHocTenant);
    }

    public List<FormaPagamento> listarParaNFe() {
        return repositorio.listarParaNFe();
    }

    @Override
    public Criteria getModel(FormaPagamentoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        if (filter.getCodigoNfe() != null) {
            if (filter.getCodigoNfe().equals("NV")) {
                criteria.add(Restrictions.isNull("codigoNfe"));
            } else {
                criteria.add(Restrictions.eq("codigoNfe", filter.getCodigoNfe()));
            }
        }

        return criteria;
    }

    public FormaPagamento importDto(FormaPagamentoDTO formaPagamentoDTO, String tenatID) {
        FormaPagamento formaPagamento = formaPagamentoMapper.toEntity(formaPagamentoDTO);
        formaPagamento.setTenatID(tenatID);
        formaPagamento.setAtivo("S");
        return salvar(formaPagamento);
    }

    @Override
    public FormaPagamento salvar(FormaPagamento formaPagamento) {

        String descricao = formaPagamento.getDescricao();
        Integer id = formaPagamento.getId();
        boolean nomeRepetido = listarFormaDePagamentoAtivo().stream()
                .filter(n -> descricao.equalsIgnoreCase(n.getDescricao()))
                .anyMatch(n -> id == null || !id.equals(n.getId()));
        if (nomeRepetido) {
            throw new CadastroException("Existe uma forma de pagamento cadastrada para a descrição informada.", null);
        }
        return super.salvar(formaPagamento);
    }

    public List<FormaPagamento> listarFormaDePagamentoAtivo() {
        return repositorio.listarFormaDePagamentoAtivo();
    }

    public void popularFormaPagamento(final String tenatID) {
        Arrays.asList(EnumMeioDePagamento.values()).stream()
                .map(meio -> {
                    FormaPagamento fp = new FormaPagamento();
                    fp.setAtivo("S");
                    fp.setCodigoNfe(meio.getCodigo());
                    fp.setDescricao(meio.getDescricao());
                    fp.setTenatID(tenatID);
                    return fp;
                })
                .forEach(this::salvar);
    }

    public FormaPagamento findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }

}
