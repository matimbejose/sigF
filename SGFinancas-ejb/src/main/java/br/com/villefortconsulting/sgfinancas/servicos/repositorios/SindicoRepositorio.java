package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Sindico;
import br.com.villefortconsulting.sgfinancas.entidades.SindicoConselho;
import java.util.List;
import javax.persistence.EntityManager;

public class SindicoRepositorio extends BasicRepository<Sindico> {

    public SindicoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public SindicoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Sindico buscar(String descricao) {
        String jpql = "select b from Sindico b where b.descricao = ?1";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Sindico> listar() {
        String jpql = "select b from Sindico b order by b.descricao";
        return getPureList(jpql);
    }

    public boolean existeSindicoAtivo() {
        String jpql = "select count(s.dataInicio) from Sindico s where s.dataFim is null";
        return getPurePojo(Long.class, jpql) != 0;
    }

    public List<SindicoConselho> obterConselheiros(Sindico sindico) {
        String jpql = "select b from SindicoConselho b where b.idSindico = ?1";
        return getPureList(jpql, sindico);
    }

    public void atualizaTenat(SindicoConselho sindicoConselho) {
        super.atualizaTenat(sindicoConselho);
    }

}
