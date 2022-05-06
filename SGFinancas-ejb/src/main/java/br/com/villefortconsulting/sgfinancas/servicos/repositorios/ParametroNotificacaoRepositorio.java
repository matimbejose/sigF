package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroNotificacao;
import java.util.List;
import javax.persistence.EntityManager;

public class ParametroNotificacaoRepositorio extends BasicRepository<ParametroNotificacao> {

    public ParametroNotificacaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ParametroNotificacaoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<ParametroNotificacao> listar() {
        String jpql = "select b from ParametroNotificacao b order by b.email";
        return getPureList(jpql);
    }

    public List<ParametroNotificacao> listar(String tipo) {
        String jpql = "select b from ParametroNotificacao b where b.tipo = ?1 order by b.email";
        return getPureList(jpql, tipo);
    }

    public List<String> listarEmails(String tipo) {
        String jpql = "select n.email from ParametroNotificacao n where n.tipo = ?1 order by n.email";
        return getPureList(jpql, tipo);
    }

    public List<String> listarEmails(String tipo, String tenatID) {
        String jpql = "select n.email from ParametroNotificacao n where n.tipo = ?1 and n.tenatID = ?2 order by n.email";
        return getPureList(jpql, tipo, tenatID);
    }

    public List<ParametroNotificacao> listarParametroNotificacaoNaoEncontrado(String tipo, List<String> emails) {
        String jpql = "select n from ParametroNotificacao n where n.tipo = ?1 and n.email not in (?2)";
        return getPureList(jpql, tipo, emails);
    }

    public List<ParametroNotificacao> listarParametroNotificacaoNaoEncontrado(String tipo) {
        String jpql = "select n from ParametroNotificacao n where n.tipo = ?1 ";
        return getPureList(jpql, tipo);
    }

}
