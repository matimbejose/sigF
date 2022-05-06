package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TipoPagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.PagamentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TipoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TipoPagamentoRepositorio;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoPagamentoService extends BasicService<TipoPagamento, TipoPagamentoRepositorio, TipoPagamentoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    PagamentoMapper pagamentoMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new TipoPagamentoRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(TipoPagamentoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    @Override
    public TipoPagamento salvar(TipoPagamento tipoPagamento) {
        boolean jaCadastrado = findByDescricao(tipoPagamento.getDescricao()).stream()
                .anyMatch(tp -> !tp.getId().equals(tipoPagamento.getId()));
        boolean isDelete = "N".equals(tipoPagamento.getAtivo());
        if (jaCadastrado && !isDelete) {
            throw new CadastroException("Existe um tipo de pagamento cadastrado para a descrição informada.", null);
        }

        return super.salvar(tipoPagamento);
    }

    public TipoPagamento importDto(TipoPagamentoDTO dto) {
        TipoPagamento tp = pagamentoMapper.toEntity(dto);
        tp.setAtivo("S");
        tp.setTenatID(adHocTenant.getTenantID());
        return salvar(tp);
    }

    public List<TipoPagamento> findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }
    
    public List<TipoPagamento> listarTiposAtivos() {
        return repositorio.listar();
    }

    public void populaTipoPagamento(Empresa empresa) {
        TipoPagamento tp = new TipoPagamento();
        tp.setAtivo("S");
        tp.setDescricao("À vista");
        tp.setPermiteParcelamento("N");
        tp.setTenatID(empresa.getTenatID());
        TipoPagamento tp1 = new TipoPagamento();
        tp1.setAtivo("S");
        tp1.setDescricao("Parcelado");
        tp1.setPermiteParcelamento("S");
        tp1.setTenatID(empresa.getTenatID());
        salvar(tp);
        salvar(tp1);
    }

}
