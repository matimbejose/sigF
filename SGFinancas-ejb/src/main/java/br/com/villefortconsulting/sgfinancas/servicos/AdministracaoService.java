package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAcessoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.AdministracaoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
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
import org.springframework.security.core.context.SecurityContextHolder;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AdministracaoService extends BasicService<EntityId, AdministracaoRepositorio, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private FipeService fipeService;

    @EJB
    private PagamentoMensalidadeModuloService pagamentoMensalidadeModuloService;

    @EJB
    private PagamentoMensalidadeModuloPermissaoService pagamentoMensalidadeModuloPermissaoService;

    private String tenatAtual;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new AdministracaoRepositorio(em);
    }

    public boolean executaAtualizacaoBancoDados(String consulta) {

        List<Empresa> acessos = loginAcessoService.getTodosTenats();
        EmpresaAcessoDTO prefeituraAcessoDTO;

        for (Empresa acesso : acessos) {
            prefeituraAcessoDTO = new EmpresaAcessoDTO(acesso.getTenatID());
            executaAtualizacaoBancoDadosIndividual(prefeituraAcessoDTO, consulta, "BD");
        }

        return true;
    }

    public List<EmpresaAcessoDTO> getTodasEmpresas() {
        return loginAcessoService.getTodasEmpresas();
    }

    // um comando pode ser executado em uma ou N prefeituras
    public boolean executaAtualizacaoDados(List<EmpresaAcessoDTO> empresas, String consulta) {

        EmpresaAcessoDTO emppresaAcessoDTO;

        for (EmpresaAcessoDTO empresa : empresas) {
            emppresaAcessoDTO = new EmpresaAcessoDTO(empresa.getTenat());
            executaAtualizacaoBancoDadosIndividual(emppresaAcessoDTO, consulta, "DA");
        }
        return true;
    }

    public void executaAtualizaoByNativeQuery(String consulta) {
        repositorio.executaAtualizaoByNativeQuery(consulta);
    }

    // sempre executa em todas
    private void executaAtualizacaoBancoDadosIndividual(EmpresaAcessoDTO prefeituraAcessoDTO, String consulta, String tipo) {

        boolean autenticado = salvaTenatAtual();

        // autentica o usuario no banco
        if (autenticado) {
            loginAcessoService.alteraTenat(prefeituraAcessoDTO.getTenat());
        } else {
            loginAcessoService.alteraTenatComUsuarioServico(prefeituraAcessoDTO);
        }

        if (tipo.equals("BD")) {
            repositorio.executaAtualizaoByNativeQuery(consulta);
        } else {
            repositorio.executaAtualizaoDados(consulta);
        }

        restauraTenatAtual();

    }

    public void executaAtualizaoTipoVeiculoFipe(String tipo) {
        fipeService.listaMarcasSistema(EnumTipoVeiculoFipe.valueOf(tipo.toUpperCase()));
    }

    public void limpaCache() {
        repositorio.limpaCache();
    }

    public String getTenatAtual() {
        return tenatAtual;
    }

    public void setTenatAtual(String tenatAtual) {
        this.tenatAtual = tenatAtual;
    }

    public boolean salvaTenatAtual() {
        boolean autenticado = false;
        if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
            setTenatAtual(((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getTenat());
            autenticado = true;
        }
        return autenticado;
    }

    public void restauraTenatAtual() {
        if (tenatAtual != null && !tenatAtual.isEmpty()) {
            EmpresaAcessoDTO prefeituraAcessoDTO = new EmpresaAcessoDTO();
            prefeituraAcessoDTO.setTenat(tenatAtual);

            // autentica o usuario no banco
            loginAcessoService.alteraTenat(prefeituraAcessoDTO.getTenat());
        }
    }

    public void aplicarNovaPermissao(ModuloPermissao mp) {
        pagamentoMensalidadeModuloService.getPagamentoMensalidadeModuloPor(mp.getIdModulo())
                .forEach(pmm -> {
                    List<PagamentoMensalidadeModuloPermissao> pmmList = pagamentoMensalidadeModuloPermissaoService.permissoesPor(pmm);
                    pmm.setPagamentoMensalidadeModuloPermissaoList(pmmList);
                    pagamentoMensalidadeModuloService.salvar(pmm);
                });
    }

}
