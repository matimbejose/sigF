package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.ContabilidadePlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaPadrao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContabilidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ContabilidadeRepositorio;
import br.com.villefortconsulting.util.FileUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContabilidadeService extends BasicService<Contabilidade, ContabilidadeRepositorio, ContabilidadeFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private GrupoService grupoService;

    @EJB
    private PlanoContaPadraoService planoContaService;

    @EJB
    private ContabilidadePlanoContaService contabilidadePlanoContaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new ContabilidadeRepositorio(em);
    }

    public void salvarLancamentos(List<ContabilidadePlanoConta> listPrevisao, Contabilidade contabilidade) {

        //Compara se a lista está vazia
        if (!listPrevisao.isEmpty()) {
            //Intera a lista para em seguida, salvar os que tem o código diferente de null
            for (ContabilidadePlanoConta cont : listPrevisao) {
                if (StringUtils.isNotEmpty(cont.getCodigo()) && cont.getCodigo() != null) {
                    cont.setIdContabilidade(contabilidade);

                    contabilidadePlanoContaService.salvarContabilidadePlanoConta(cont);

                } else if ((StringUtils.isEmpty(cont.getCodigo())) && cont.getId() != null) {
                    contabilidadePlanoContaService.remover(cont);
                }
            }
        }
    }

    public List<ContabilidadePlanoConta> obterContabilidadePlanoConta(Contabilidade cont) {
        ContabilidadePlanoConta contabilidade;
        List<PlanoContaPadrao> listaPlanoConta = planoContaService.obterTodosItensPlanoConta();
        List<ContabilidadePlanoConta> listaAtualizada = new ArrayList<>();
        List<ContabilidadePlanoConta> listaComLancamentos = new ArrayList<>();

        if (cont.getId() != null) {
            listaComLancamentos = contabilidadePlanoContaService.listarPorContabilidade(cont);
        }

        for (PlanoContaPadrao planoConta : listaPlanoConta) {
            contabilidade = listaComLancamentos.stream().filter(p -> p.getIdPlanoContaPadrao().equals(planoConta)).findFirst().orElse(null);

            // verifica se ja existe a contabilidade
            if (contabilidade != null) {
                listaAtualizada.add(contabilidade);
            } else {
                // precisa criar a contabilidade
                contabilidade = new ContabilidadePlanoConta();
                contabilidade.setIdPlanoContaPadrao(planoConta);
                contabilidade.setCodigo("");
                listaAtualizada.add(contabilidade);
            }
        }

        //Ordena a lista antes de retorna-lá.
        Collections.sort(listaAtualizada, (p1, p2) -> p1.getIdPlanoContaPadrao().getCodigo().compareTo(p2.getIdPlanoContaPadrao().getCodigo()));

        return listaAtualizada;
    }

    public byte[] getLogo(Part part, Contabilidade contabilidade) throws FileException {
        if (part != null) {
            return FileUtil.convertPartToBytes(part);
        }
        return contabilidade.getLogo();
    }

    @Override
    public Criteria getModel(ContabilidadeFiltro filter) {
        Criteria criteria = super.getModel(filter);

        if (!Boolean.TRUE.equals(filter.getUsuario().getIdPerfil().getEhSuporte()) && filter.getUsuario().getIdContabilidade() != null) {
            criteria.add(Restrictions.eq("id", filter.getUsuario().getIdContabilidade().getId()));
        }

        return criteria;
    }

    public List<Object> getDadosAuditoriaContaParcelaByID(ContaParcela obj) {
        return repositorio.getDadosAuditoriaByID(obj.getId());
    }

    public String buscarCodigoContabilidadePlanoConta(Contabilidade contabilidade, PlanoConta planoConta) {
        PlanoContaPadrao planoContaPadrao = planoContaService.buscarPlanoContaPadrao(planoConta.getCodigo());

        if (planoContaPadrao != null) {
            return repositorio.buscarCodigoContabilidadePlanoConta(contabilidade, planoContaPadrao);
        }

        return null;
    }

    public List<Contabilidade> listarContabilidade() {
        return repositorio.listarContabilidade();
    }

    public void atualizarAcessoUsuariosContabilidade(Empresa empresa) {
        removerAcessoUsuariosContabilidadeAntiga(empresa);

        if (empresa.getIdContabilidade() != null) {
            criarAcessoUsuariosContabilidade(empresa);
        }
    }

    public void criarAcessoUsuariosContabilidade(Empresa empresa) {
        List<Usuario> usuarios = usuarioService.listarUsuarioSemAcessoEmpresaPorContabilidade(empresa, empresa.getIdContabilidade());
        for (Usuario usuario : usuarios) {

            // Cria acesso ao usuario
            loginAcessoService.adicionarLoginAcesso(empresa, usuario);

            // Associa um usuario a um grupo
            Grupo grupo = grupoService.getGrupoPorTipo(usuario.getIdPerfil().getTipo());
            if (grupo != null) {
                GrupoEmpresa grupoEmpresa = grupoService.getGrupoEmpresa(grupo, empresa);
                if (grupoEmpresa != null) {
                    UsuarioGrupoEmpresa uge = new UsuarioGrupoEmpresa();
                    uge.setIdUsuario(usuario);
                    uge.setIdGrupoEmpresa(grupoEmpresa);

                    usuarioService.addUsuarioGrupoEmpresa(uge);
                }
            }
        }
    }

    public void removerAcessoUsuariosContabilidadeAntiga(Empresa empresa) {
        usuarioService.listarUsuarioComAcessoEmpresaComContabilidade(empresa).stream()
                .filter(usuario -> empresa.getIdContabilidade() == null || !empresa.getIdContabilidade().equals(usuario.getIdContabilidade()))
                .map(usuario -> {
                    // Remove associacao do usuario ao grupo pertencente dele a empresa
                    Grupo grupo = grupoService.getGrupoPorTipo(usuario.getIdPerfil().getTipo());
                    if (grupo != null) {
                        GrupoEmpresa grupoEmpresa = grupoService.getGrupoEmpresa(grupo, empresa);
                        if (grupoEmpresa != null) {
                            UsuarioGrupoEmpresa uge = usuarioService.getUsuarioGrupoEmpresa(usuario, grupoEmpresa);
                            if (uge != null) {
                                usuarioService.removerUsuarioGrupoEmpresa(uge);
                            }
                        }
                    }
                    return usuario;
                })
                .map(usuario -> loginAcessoService.getLoginAcesso(usuario, empresa))
                .filter(Objects::nonNull)
                .forEachOrdered(loginAcessoService::remover);
    }

}
