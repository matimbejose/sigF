package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Classificacao;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CnaeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CnaeRepositorio;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CnaeService extends BasicService<Cnae, CnaeRepositorio, CnaeFiltro> {

    @EJB
    private ClassificacaoService classificacaoService;

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CnaeRepositorio(em);
    }

    public Cnae buscarPorCodigo(String codigo) {
        codigo = codigo.replace(".", "").replace("-", "").replace("/", "");
        codigo = codigo.substring(0, 4) + "-" + codigo.substring(4, 5) + "/" + codigo.substring(5);
        return repositorio.buscarPorCodigo(codigo);
    }

    public Cnae buscarCnaePorCodigo(String codigo, String descricao) {
        Cnae cnae = buscarPorCodigo(codigo);
        if (cnae == null) {
            Cnae cnaeAux = new Cnae();
            String codigoAux = codigo;
            codigoAux = codigoAux.replace(".", "").replace("-", "").replace("/", "");
            codigoAux = codigoAux.substring(0, 4) + "-" + codigoAux.substring(4, 5) + "/" + codigoAux.substring(5);
            cnaeAux.setCodigo(codigoAux);
            cnaeAux.setDescricao(descricao);
            cnaeAux.setIdClassificacao(classificacaoService.buscarClassificacaoPorNumero(Integer.parseInt(codigoAux.substring(0, 2))));
            if (cnaeAux.getIdClassificacao() != null) {
                cnae = adicionar(cnaeAux);
            }
        }
        return cnae;
    }

    public List<Cnae> listar(String descricao) {
        return repositorio.listar(descricao);
    }

    public List<Cnae> listar(Integer codigo) {
        return repositorio.listar(codigo);
    }

    public List<Cnae> listarCnaePorClassificacao(Classificacao classificacao) {
        return repositorio.listarCnaePorClassificacao(classificacao);
    }

    public List<Cnae> listarCnaePorClassificacao(List<Classificacao> classificacoes) {
        if (classificacoes == null || classificacoes.isEmpty()) {
            return new LinkedList<>();
        } else {
            return repositorio.listarCnaePorClassificacao(classificacoes);
        }
    }

    public List<Cnae> listarCnaes(Empresa empresa) {
        return repositorio.listarCnae(empresa);
    }

    @Override
    public Criteria getModel(CnaeFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idClassificacao", "idClassificacao", JoinType.LEFT_OUTER_JOIN);
        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "idClassificacao.descricao", filter.getClassificacao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public List<Cnae> listarTodosCnaesEmpresa(Empresa empresa) {
        List<Cnae> cnaes = new LinkedList<>();

        if (empresa.getIdCnae() != null) {
            cnaes.add(empresa.getIdCnae());
        }

        List<Cnae> cnaesSecundarios = listarCnaes(empresa);

        if (!cnaesSecundarios.isEmpty()) {
            cnaes.addAll(cnaesSecundarios);
        }

        return cnaes;
    }

}
