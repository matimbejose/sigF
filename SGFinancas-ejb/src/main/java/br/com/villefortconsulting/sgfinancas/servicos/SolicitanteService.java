package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteHistorico;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitantePanorama;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteTurma;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PanoramaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitantePanaromaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteResumoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitanteFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitanteHistoricoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitanteTurmaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.SolicitanteRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitante;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SolicitanteService extends BasicService<Solicitante, SolicitanteRepositorio, SolicitanteFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteService clienteService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new SolicitanteRepositorio(em, adHocTenant);
    }

    public Solicitante buscar(String nome) {
        if (nome != null) {
            return repositorio.buscar(nome);
        }
        return null;
    }

    public Solicitante buscar(String nome, String cpfCnpj) {
        if (nome != null) {
            if (cpfCnpj != null) {
                return repositorio.buscar(nome, cpfCnpj);
            }
            return repositorio.buscar(nome);
        }
        return null;
    }

    public List<Solicitante> listarSolicitantesIndicou(Solicitante solicitante) {
        return repositorio.listarSolicitantesIndicou(solicitante);
    }

    public List<PanoramaDTO> listarPanorama(Date dataInicio, Date dataFim) {
        return repositorio.listarPanorama(dataInicio, dataFim);
    }

    public List<SolicitantePanaromaDTO> listarSolicitantePanoramaDTO(Date dataInicio, Date dataFim) {
        List<PanoramaDTO> panoramas = listarPanorama(dataInicio, dataFim);

        if (panoramas == null) {
            return new ArrayList<>();
        }

        List<String> cursos = panoramas.stream().map(PanoramaDTO::getCurso).distinct().collect(Collectors.toList());

        Collections.sort(panoramas, (p1, p2) -> p1.getData().compareTo(p2.getData()));

        List<SolicitantePanaromaDTO> list = new LinkedList<>();

        cursos.stream().forEach(curso -> {
            Date dataControle = null;
            SolicitantePanaromaDTO sp = new SolicitantePanaromaDTO();

            for (PanoramaDTO panorama : panoramas) {
                if (!curso.equals(panorama.getCurso())) {
                    continue;
                }
                if (dataControle == null || !dataControle.equals(panorama.getData())) {
                    sp = new SolicitantePanaromaDTO();
                    list.add(sp);

                    dataControle = panorama.getData();

                    sp.setCurso(panorama.getCurso());
                    sp.setData(panorama.getData());
                    sp.setMatriculas(0);
                    sp.setInteressadissimos(0);
                    sp.setTotalReal(0);
                    sp.setInteressadosAConfirmar(0);
                    sp.setPrognostico(0);
                    sp.setInteressados(0);
                    sp.setSolicitantes(0);
                }

                if (panorama.getStatus().equals(EnumStatusSolicitante.MATRICULA_EFETVADA.getChave())) {
                    sp.setMatriculas(sp.getMatriculas() + panorama.getQuantidade().intValue());
                    sp.setTotalReal(sp.getTotalReal() + panorama.getQuantidade().intValue());
                    sp.setPrognostico(sp.getPrognostico() + panorama.getQuantidade().intValue());
                }

                if (panorama.getStatus().equals(EnumStatusSolicitante.INTERESSADISSIMO.getChave())) {
                    sp.setInteressadissimos(sp.getInteressadissimos() + panorama.getQuantidade().intValue());
                    sp.setTotalReal(sp.getTotalReal() + panorama.getQuantidade().intValue());
                    sp.setPrognostico(sp.getPrognostico() + panorama.getQuantidade().intValue());
                }

                if (panorama.getStatus().equals(EnumStatusSolicitante.INTERESSADO_CONFIRMAR.getChave())) {
                    sp.setInteressadosAConfirmar(sp.getInteressadosAConfirmar() + panorama.getQuantidade().intValue());
                    sp.setPrognostico(sp.getPrognostico() + panorama.getQuantidade().intValue());
                }

                if (panorama.getStatus().equals(EnumStatusSolicitante.INTERESSADO.getChave())) {
                    sp.setInteressados(sp.getInteressados() + panorama.getQuantidade().intValue());
                }

                if (panorama.getStatus().equals(EnumStatusSolicitante.SOLICITANTE.getChave())) {
                    sp.setSolicitantes(sp.getSolicitantes() + panorama.getQuantidade().intValue());
                }
            }
        });

        return list;
    }

    public List<SolicitanteDTO> listarSolicitanteDTO() {
        return repositorio.listarSolicitanteDTO();
    }

    public List<SolicitanteDTO> listaPresencaAlunos(Turma turma) {
        return repositorio.listaPresencaAlunos(turma);
    }

    public List<SolicitanteDTO> gerarRelacaoProfissoes(Turma turma) {
        return repositorio.gerarRelacaoProfissoes(turma);
    }

    public SolicitantePanorama salvarSolicitantePanorama(SolicitantePanorama sp) {
        if (sp.getId() == null) {
            return repositorio.adicionarSolicitantePanorama(sp);
        } else {
            return repositorio.alterarSolicitantePanorama(sp);
        }
    }

    public SolicitantePanorama adicionarSolicitanteHistorico(SolicitantePanorama solicitante) {
        return repositorio.adicionarSolicitantePanorama(solicitante);
    }

    public SolicitantePanorama alterarSolicitantePanorama(SolicitantePanorama solicitante) {
        return repositorio.alterarSolicitantePanorama(solicitante);
    }

    public void removerSolicitantePanorama(SolicitantePanorama solicitante) {
        repositorio.removerSolicitantePanorama(solicitante);
    }

    public SolicitantePanorama buscarSolicitantePanorama(int id) {
        return repositorio.buscarSolicitantePanorama(id);
    }

    public List<SolicitanteResumoDTO> gerarRelacaoProfissoesResumo(Turma turma) {
        return repositorio.gerarRelacaoProfissoesResumo(turma);
    }

    public SolicitanteHistorico adicionarSolicitanteHistorico(SolicitanteHistorico solicitante) {
        return repositorio.adicionarSolicitanteHistorico(solicitante);
    }

    public SolicitanteHistorico alterarSolicitanteHistorico(SolicitanteHistorico solicitante) {
        return repositorio.alterarSolicitanteHistorico(solicitante);
    }

    public void removerSolicitanteHistorico(SolicitanteHistorico solicitante) {
        repositorio.removerSolicitanteHistorico(solicitante);
    }

    public SolicitanteHistorico buscarSolicitanteHistorico(int id) {
        return repositorio.buscarSolicitanteHistorico(id);
    }

    public SolicitanteHistorico buscarSolicitanteHistorico(SolicitanteTurma solicitanteTurma) {
        return repositorio.buscarSolicitanteHistorico(solicitanteTurma);
    }

    public List<SolicitanteHistorico> listarSolicitanteHistorico() {
        return repositorio.listarSolicitanteHistorico();
    }

    @Override
    public Criteria getModel(SolicitanteFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "status", filter.getStatus());

        return criteria;
    }

    public int quantidadeRegistrosSolicitanteTurmaFiltrados(SolicitanteFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getSolicitanteTurmaModel(filtro));
    }

    public List<SolicitanteTurma> getListaSolicitanteTurmaFiltrada(SolicitanteFiltro filtro) {
        Criteria criteria = getSolicitanteTurmaModel(filtro);

        filtro.addPropriedadeOrdenacao("idSolicitante.nome");
        filtro.addPropriedadeOrdenacao("idCurso.descricao");

        return repositorio.getListaFiltrada(criteria, filtro);
    }

    public Criteria getSolicitanteTurmaModel(SolicitanteFiltro filtro) {
        Criteria criteria = super.getModel(filtro);

        criteria.createAlias("idSolicitante", "idSolicitante");
        criteria.createAlias("idTurma", "idTurma");
        criteria.createAlias("idTurma.idCurso", "idCurso");

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            criteria.add(Restrictions.ilike("idSolicitante.nome", filtro.getDescricao(), MatchMode.ANYWHERE));
        }

        return criteria;
    }

    public int quantidadeRegistrosSolicitanteFiltrados(SolicitanteHistoricoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getSolicitanteHistoricoModel(filtro));
    }

    public List<SolicitanteHistorico> getListaSolicitanteHistoricoFiltrada(SolicitanteHistoricoFiltro filtro) {
        return repositorio.getListaFiltrada(getSolicitanteHistoricoModel(filtro), filtro);
    }

    public Criteria getSolicitanteHistoricoModel(SolicitanteHistoricoFiltro filtro) {

        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(SolicitanteHistorico.class);

        criteria.add(Restrictions.eq("idSolicitante", filtro.getSolicitante()));

        if (filtro.getSolicitante().getCursosInteresse() == null) {
            criteria.add(Restrictions.isNull("id"));
        } else {
            criteria.add(Restrictions.eq("idCurso", filtro.getSolicitante().getIdCurso()));
        }

        return criteria;
    }

    public List<Object> getDadosAuditoriaSolicitanteHistoricoByID(SolicitanteHistorico solicitante) {
        return repositorio.getDadosAuditoriaByID(SolicitanteHistorico.class, solicitante.getId());
    }

    public SolicitanteTurma salvarSolicitanteTurma(SolicitanteTurma solicitanteTurma, Usuario usuarioLogado) {
        if (solicitanteTurma.getId() == null) {
            ativarSolicitante(solicitanteTurma, usuarioLogado);
            return repositorio.adicionarSolicitanteTurma(solicitanteTurma);
        } else {
            return repositorio.alterarSolicitanteTurma(solicitanteTurma);
        }
    }

    public Solicitante ativarSolicitante(SolicitanteTurma solicitanteTurma, Usuario usuarioLogado) {

        Solicitante solicitante = solicitanteTurma.getIdSolicitante();
        if (solicitante.getIdCliente() == null) {

            if (solicitante.getTipoPessoa() == null || solicitante.getCpfCnpj() == null) {
                throw new CadastroException(" Aluno sem documento de identificação (CPF/CNPJ), verificar no cadastro de solicitante. ", null);
            }

            Cliente cliente = new Cliente();
            cliente.getEndereco().setIdCidade(solicitante.getIdCidade());
            cliente.setNome(solicitante.getNome());
            cliente.setTipo(solicitante.getTipoPessoa());
            cliente.setCpfCNPJ(solicitante.getCpfCnpj());
            cliente.setTelefoneCelular(solicitante.getTelefoneCelular());
            cliente.setTelefoneResidencial(solicitante.getTelefoneFixo());

            cliente.setTenatID(adHocTenant.getTenantID());

            cliente = clienteService.salvar(cliente);

            solicitante.setIdCliente(cliente);
        }

        salvarHistoricoPorTurma(solicitanteTurma, usuarioLogado);

        return solicitante;
    }

    public void salvarHistoricoPorTurma(SolicitanteTurma solicitanteTurma, Usuario usuarioLogado) {
        SolicitanteHistorico solicitanteHistorico = new SolicitanteHistorico();
        solicitanteHistorico.setIdCurso(solicitanteTurma.getIdTurma().getIdCurso());
        solicitanteHistorico.setIdSolicitante(solicitanteTurma.getIdSolicitante());
        solicitanteHistorico.setIdUsuarioContato(usuarioLogado);
        solicitanteHistorico.setData(solicitanteTurma.getDataInscricao());
        solicitanteHistorico.setSituacaoSolicitante(EnumStatusSolicitante.MATRICULA_EFETVADA.getChave());
        solicitanteHistorico.setComentario(EnumStatusSolicitante.MATRICULA_EFETVADA.getDescricao());
        solicitanteHistorico.setTenatID(adHocTenant.getTenantID());

        salvarSolicitanteHistorico(solicitanteHistorico);
    }

    public SolicitanteHistorico salvarSolicitanteHistorico(SolicitanteHistorico sh) {
        if (sh.getId() == null) {
            repositorio.adicionarSolicitanteHistorico(sh);
        } else {
            repositorio.alterarSolicitanteHistorico(sh);
        }

        // FIX-ME: Verificar logico de atualizacao de status do solicitante
        Solicitante s = sh.getIdSolicitante();
        s.setStatus(sh.getSituacaoSolicitante());

        salvar(s);

        return sh;
    }

    public SolicitanteTurma adicionarSolicitanteTurma(SolicitanteTurma solicitanteTurma) {
        return repositorio.adicionarSolicitanteTurma(solicitanteTurma);
    }

    public SolicitanteTurma alterarSolicitanteTurma(SolicitanteTurma solicitanteTurma) {
        return repositorio.alterarSolicitanteTurma(solicitanteTurma);
    }

    public void removerSolicitanteTurma(SolicitanteTurma solicitanteTurma) {

        // Removendo historico de matricula
        SolicitanteHistorico solicitanteHistorico = buscarSolicitanteHistorico(solicitanteTurma);
        if (solicitanteHistorico != null) {
            repositorio.removerSolicitanteHistorico(solicitanteHistorico);
        }

        // Alternando status de aluno de volta pra solicitante
        Solicitante solicitante = solicitanteTurma.getIdSolicitante();
        if (solicitantePossuiOutraTurma(solicitanteTurma)) {
            solicitante.setStatus(EnumStatusSolicitante.SOLICITANTE.getChave());
            alterar(solicitante);
        }

        repositorio.removerSolicitanteTurma(solicitanteTurma);
    }

    public SolicitanteTurma buscarSolicitanteTurma(int id) {
        return repositorio.buscarSolicitanteTurma(id);
    }

    public int quantidadeRegistrosSolicitanteTurmaFiltrados(SolicitanteTurmaFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getSolicitanteTurmaModel(filtro));
    }

    public List<SolicitanteTurma> getListaSolicitanteTurmaFiltrada(SolicitanteTurmaFiltro filtro) {
        return repositorio.getListaFiltrada(getSolicitanteTurmaModel(filtro), filtro);
    }

    public Criteria getSolicitanteTurmaModel(SolicitanteTurmaFiltro filtro) {

        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(SolicitanteTurma.class);

        if (filtro.getTurma() != null) {
            criteria.add(Restrictions.eq("idTurma", filtro.getTurma()));
        }

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {

            criteria.createAlias("idSolicitante", "idSolicitante");

            criteria.add(Restrictions.ilike("idSolicitante.nome", filtro.getDescricao(), MatchMode.ANYWHERE));
        }

        return criteria;
    }

    public List<Solicitante> listarAlunosDisponiveis(Turma turma) {
        List<Solicitante> solicitantes = repositorio.listarAlunosDisponiveis(turma);

        Collections.sort(solicitantes, (p1, p2) -> p1.getNome().compareTo(p2.getNome()));

        return solicitantes;
    }

    public List<Object> getDadosAuditoriaSolicitanteTurmaByID(SolicitanteTurma obj) {
        return repositorio.getDadosAuditoriaByID(SolicitanteTurma.class, obj.getId());
    }

    public List<Solicitante> listarAlunos(Turma turma) {
        return repositorio.listarAlunos(turma);
    }

    public List<SolicitanteTurma> listarSolicitanteTurma(Turma turma) {
        return repositorio.listarSolicitanteTurma(turma);
    }

    public SolicitanteTurma buscarAlunoPorContrato(Contrato contrato) {
        return repositorio.buscarAlunoPorContrato(contrato);
    }

    public boolean solicitantePossuiOutraTurma(SolicitanteTurma solicitanteTurma) {
        return repositorio.solicitantePossuiOutraTurma(solicitanteTurma);
    }

}
