package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Curso;
import br.com.villefortconsulting.sgfinancas.entidades.CursoInteresse;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import java.util.List;
import javax.persistence.EntityManager;

public class CursoRepositorio extends BasicRepository<Curso> {

    public CursoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public CursoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Curso> listar() {
        String jpql = "select b from Curso b order by b.descricao";
        return getPureList(jpql);
    }

    public List<Curso> listarCursosSemInteresse(Solicitante solicitante) {
        String jpql = "select a from Curso a where a.tenatID = ?2 and a not in (select b.idCurso from SolicitanteHistorico b where b.idSolicitante = ?1 and b.tenatID = ?2 )  ";
        return getPureList(jpql, solicitante, adHocTenant.getTenantID());
    }

    public List<CursoInteresse> listarCursoInteresse(Solicitante solicitante) {
        String jpql = "select c from CursoInteresse c where c.idSolicitante = ?1 ";
        return getPureList(jpql, solicitante);
    }

}
