package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Curso;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import java.util.List;
import javax.persistence.EntityManager;

public class TurmaRepositorio extends BasicRepository<Turma> {

    public TurmaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TurmaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Turma buscar(String descricao) {
        String jpql = "select b from Turma b where b = ?1 ";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Turma> listar() {
        String jpql = "select b from Turma b";
        return getPureList(jpql);
    }

    public List<Turma> listar(Curso curso) {
        String jpql = "select b from Turma b where b.idCurso = ?1 ";
        return getPureList(jpql, curso);
    }

    public boolean verificarTurmaPossuiAlunos(Turma turma) {
        String jpql = "select st from SolicitanteTurma st where st.idTurma = ?1 ";
        return getPurePojoTop1(jpql, turma) != null;
    }

}
