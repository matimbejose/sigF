package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteHistorico;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitantePanorama;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteTurma;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PanoramaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteResumoDTO;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTurma;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitante;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class SolicitanteRepositorio extends BasicRepository<Solicitante> {

    public SolicitanteRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public SolicitanteRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Solicitante buscar(String nome) {
        String jpql = "select b from Solicitante b where b.nome = ?1 ";
        return getPurePojoTop1(jpql, nome);
    }

    public Solicitante buscar(String nome, String cpfCpnj) {
        String jpql = "select b from Solicitante b where b.nome = ?1 and b.cpfCpnj = ?2 ";
        return getPurePojoTop1(jpql, nome, cpfCpnj);
    }

    public List<Solicitante> listar() {
        String jpql = "select b from Solicitante b";
        return getPureList(jpql);
    }

    public List<Solicitante> listarSolicitantesIndicou(Solicitante solicitante) {
        if (solicitante.getId() != null) {
            String jpql = "select s from Solicitante s join s.idCliente where s.id <> ?1 order by s.nome ";
            return getPureList(jpql, solicitante.getId());
        } else {
            String jpql = "select s from Solicitante s join s.idCliente order by s.nome ";
            return getPureList(jpql);
        }
    }

    public List<PanoramaDTO> listarPanorama(Date dataInicio, Date dataFim) {
        String jpql = " select new br.com.villefortconsulting.sgfinancas.entidades.dto.PanoramaDTO("
                + " s.situacaoSolicitante,s.data,count(s.id),s.idCurso.descricao "
                + ") from SolicitanteHistorico s "
                + " where s.data >= ?1 and s.data <= ?2 and s.tenatID = ?3 "
                + " group by s.situacaoSolicitante,s.data,s.idCurso.descricao "
                + " order by s.idCurso.descricao ";
        return getPureList(jpql, dataInicio, dataFim, adHocTenant.getTenantID());
    }

    public List<SolicitanteDTO> listarSolicitanteDTO() {
        String jpql = " select new br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteDTO("
                + "s.id,s.nome,s.status,s.origem,s.email,s.telefoneCelular,s.cpfCnpj,s.empresa,s.cargo,a.descricao,c.descricao,u.descricao,si.nome"
                + ") from Solicitante s "
                + " left join s.idAreaAtuacao a "
                + " left join s.idCidade c "
                + " left join c.idUF u "
                + " left join s.idSolicitanteIndicou si "
                + " order by s.nome ";
        return getPureList(jpql);
    }

    public List<SolicitanteDTO> listaPresencaAlunos(Turma turma) {
        String jpql = " select new br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteDTO("
                + " st.idSolicitante.id,st.idSolicitante.nome"
                + " ) from SolicitanteTurma st "
                + " where st.idTurma = ?1 "
                + " order by st.idSolicitante.nome ";
        return getPureList(jpql, turma);
    }

    public List<SolicitanteDTO> gerarRelacaoProfissoes(Turma turma) {
        String jpql = " select new br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteDTO("
                + " st.idSolicitante.id,st.idSolicitante.nome, st.idSolicitante.empresa, st.idSolicitante.cargo, a.descricao"
                + " ) from SolicitanteTurma st "
                + " left join st.idSolicitante.idAreaAtuacao a "
                + " where st.idTurma = ?1 "
                + " order by a.descricao, st.idSolicitante.cargo, st.idSolicitante.empresa, st.idSolicitante.nome ";
        return getPureList(jpql, turma);
    }

    public List<SolicitanteResumoDTO> gerarRelacaoProfissoesResumo(Turma turma) {
        String jpql = " select new br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteResumoDTO("
                + " isNull(a.descricao, 'Não informado'), count(st.tenatID)"
                + " ) from SolicitanteTurma st "
                + " left join st.idSolicitante.idAreaAtuacao a "
                + " where st.idTurma = ?1 and st.tenatID = ?2"
                + " group by a.descricao "
                + " order by a.descricao ";
        return getPureList(jpql, turma, adHocTenant.getTenantID());
    }

    public SolicitanteHistorico adicionarSolicitanteHistorico(SolicitanteHistorico solicitante) {
        addEntity(solicitante);
        return solicitante;
    }

    public SolicitanteHistorico alterarSolicitanteHistorico(SolicitanteHistorico solicitante) {
        return setEntity(solicitante);
    }

    public void removerSolicitanteHistorico(SolicitanteHistorico solicitante) {
        removeEntity(solicitante);
    }

    public SolicitanteHistorico buscarSolicitanteHistorico(int id) {
        return getEntity(SolicitanteHistorico.class, id);
    }

    public SolicitanteHistorico buscarSolicitanteHistorico(SolicitanteTurma solicitanteTurma) {
        String jpql = " select s from SolicitanteHistorico b where b.idCurso =?1 and b.data = ?2 and b.situacaoSolicitante = ?3 ";
        return getPurePojo(jpql, solicitanteTurma.getIdTurma().getIdCurso(), solicitanteTurma.getDataInscricao(), EnumStatusSolicitante.MATRICULA_EFETVADA.getChave());
    }

    public List<SolicitanteHistorico> listarSolicitanteHistorico() {
        String jpql = "select b from SolicitanteHistorico b";
        return getPureList(jpql);
    }

    public SolicitanteTurma adicionarSolicitanteTurma(SolicitanteTurma solicitanteTurma) {
        addEntity(solicitanteTurma);
        return solicitanteTurma;
    }

    public SolicitanteTurma alterarSolicitanteTurma(SolicitanteTurma solicitanteTurma) {
        return setEntity(solicitanteTurma);
    }

    public void removerSolicitanteTurma(SolicitanteTurma solicitanteTurma) {
        removeEntity(solicitanteTurma);
    }

    public SolicitanteTurma buscarSolicitanteTurma(int id) {
        return getEntity(SolicitanteTurma.class, id);
    }

    public SolicitantePanorama adicionarSolicitantePanorama(SolicitantePanorama solicitantePanorama) {
        addEntity(solicitantePanorama);
        return solicitantePanorama;
    }

    public SolicitantePanorama alterarSolicitantePanorama(SolicitantePanorama solicitantePanorama) {
        return setEntity(solicitantePanorama);
    }

    public void removerSolicitantePanorama(SolicitantePanorama solicitantePanorama) {
        removeEntity(solicitantePanorama);
    }

    public SolicitantePanorama buscarSolicitantePanorama(int id) {
        return getEntity(SolicitantePanorama.class, id);
    }

    public SolicitantePanorama buscarSolicitantePanorama(Solicitante s, Date data) {
        String jpql = "select s from SolicitantePanorama s where s.idSolicitante = ?1 and s.data = ?2 ";
        return getPurePojo(jpql, s, data);
    }

    public List<Solicitante> listarAlunosDisponiveis(Turma turma) {
        String jpql = " select s from Solicitante s ";
        jpql += " where s not in (select ct.idSolicitante from SolicitanteTurma ct where ct.idTurma = ?1 and ct.tenatID = ?2 ) ";
        jpql += " and s.tenatID = ?2 ) ";
        jpql += " order by s.nome ";
        return getPureList(jpql, turma, adHocTenant.getTenantID());
    }

    public List<Solicitante> listarAlunos(Turma turma) {
        String jpql = " select st.idSolicitante from SolicitanteTurma st where st.idTurma = ?1 ";

        return getPureList(jpql, turma);
    }

    public List<SolicitanteTurma> listarSolicitanteTurma(Turma turma) {
        String jpql = " select st from SolicitanteTurma st where st.idTurma = ?1 ";

        return getPureList(jpql, turma);
    }

    public SolicitanteTurma buscarAlunoPorContrato(Contrato contrato) {
        String jpql = " select st from SolicitanteTurma st where st.idContrato = ?1 ";
        return getPurePojo(jpql, contrato);
    }

    public boolean solicitantePossuiOutraTurma(SolicitanteTurma solicitanteTurma) {
        String jpql = "select st from SolicitanteTurma st where st.idTurma <> ?1 and st.idTurma.situacao = ?2 and st.idSolicitante = ?3 ";
        return getPurePojoTop1(jpql, solicitanteTurma.getIdTurma(), EnumSituacaoTurma.ABERTA.getSituacao(), solicitanteTurma.getIdSolicitante()) != null;
    }

    public List<Object> getDadosAuditoriaByID(Class<?> aClass, Integer id) {
        return getDadosAuditoria(aClass, id);
    }

}
