package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.MoradorUnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.Unidade;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeOcupacao;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class UnidadeRepositorio extends BasicRepository<Unidade> {

    public UnidadeRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public UnidadeRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Unidade> listar() {
        String jpql = "select u from Unidade u order by b.descricao";
        return getPureList(jpql);
    }

    public List<UnidadeOcupacao> listarOcupacoes(Unidade unidade) {
        return getPureList("select um from UnidadeOcupacao um where um.idUnidade = ?1", unidade);
    }

    public UnidadeOcupacao adicionarUnidadeOcupacao(UnidadeOcupacao unidadeMorador) {
        return addEntity(unidadeMorador);
    }

    public UnidadeOcupacao alterarUnidadeOcupacao(UnidadeOcupacao unidadeMorador) {
        return setEntity(unidadeMorador);
    }

    public void removerUnidadeOcupacao(UnidadeOcupacao unidadeMorador) {
        removeEntity(unidadeMorador);
    }

    public UnidadeOcupacao buscarUnidadeOcupacao(int id) {
        return getEntity(UnidadeOcupacao.class, id);
    }

    public List<UnidadeOcupacao> listarUnidadeOcupacao() {
        String jpql = "select u from UnidadeOcupacao u ";
        return getPureList(jpql);
    }

    public List<UnidadeOcupacao> listarUnidadeOcupacaoSemMensalidade(Date competencia, String tenant) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select u from UnidadeOcupacao u ");
        jpql.append(" where u.dataEntrada <= ?1 and (u.dataSaida is null or u.dataSaida > ?1) ");
        jpql.append(" and u.tenatID = ?4");
        jpql.append(" and u.id not in( ");
        jpql.append(" select a.idUnidadeOcupacao from ContaReceber a where month(a.dataVencimento) = ?2 ");
        jpql.append(" and year(a.dataVencimento) = ?3 and a.tenatID = ?4 ");
        jpql.append(" ) ");
        return getPureList(jpql.toString(), competencia, DataUtil.getMes(competencia), DataUtil.getAno(competencia), tenant);
    }

    public UnidadeOcupacao buscarUnidadeOcupacao(Unidade unidade) {
        String jpql = "select u from UnidadeOcupacao u where u.idUnidade = ?1 and u.dataSaida is null";
        return getPurePojo(jpql, unidade);
    }

    public MoradorUnidadeOcupacao buscarMoradorResponsavel(UnidadeOcupacao unidadeOcupacao) {
        String jpql = "select m from MoradorUnidadeOcupacao m where m.idUnidadeOcupacao = ?1 and m.responsavel = 'S'";
        return getPurePojo(jpql, unidadeOcupacao);
    }

    public List<MoradorUnidadeOcupacao> buscarListaMoradores(UnidadeOcupacao unidadeOcupacao) {
        String jpql = "select m from MoradorUnidadeOcupacao m where m.idUnidadeOcupacao = ?1";
        return getPureList(jpql, unidadeOcupacao);
    }

}
