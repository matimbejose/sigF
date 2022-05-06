package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.ReturnWrapper;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CadastroIuguDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CentroCustoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.CentroCustoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CentroCustoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CentroCustoRepositorio;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.springframework.http.ResponseEntity;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CentroCustoService extends BasicService<CentroCusto, CentroCustoRepositorio, CentroCustoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    transient CentroCustoMapper centroCustoMapper;

    @EJB
    private EmpresaService empresaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new CentroCustoRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(CentroCustoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());

        return criteria;
    }

    public CentroCusto addCentroCustoPorTurma(Turma turma) {

        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setDescricao("Turma: " + turma.getDescricao());

        return salvar(centroCusto);

    }
    
    public List<CentroCusto> listarCentroCustoAtivos(){
        return repositorio.listarAtivos();
    }

    public CentroCusto importDto(CentroCustoDTO centroCustoDTO, String tenatID) {
        CentroCusto centroCusto = centroCustoMapper.toEntity(centroCustoDTO);
        centroCusto.setTenatID(tenatID);
        return adicionar(centroCusto);
    }

    public CentroCusto buscaCentroCustoByCNPJ(String cnpj) {
        return repositorio.findByCNPJ(CpfCnpjUtil.mascararCnpj(CpfCnpjUtil.removerMascaraCnpj(cnpj)));
    }

    @Override
    public CentroCusto salvar(CentroCusto centroCusto) {
        String descricao = centroCusto.getDescricao();
        Integer id = centroCusto.getId();
        String cnpj = centroCusto.getCnpj();
        String token = centroCusto.getToken();
        boolean nomeRepetido = listar().stream()
                .filter(n -> descricao.equals(n.getDescricao()))
                .anyMatch(n -> id == null || !id.equals(n.getId()));
        if (nomeRepetido) {
            throw new CadastroException("Existe um centro de custo para descrição informada.", null);
        }
        if (cnpj != null && !cnpj.isEmpty()) {
            boolean cnpjRepetido = listar().stream()
                    .filter(n -> cnpj.equals(n.getCnpj()))
                    .anyMatch(n -> id == null || !id.equals(n.getId()));
            if (cnpjRepetido) {
                throw new CadastroException("Existe um centro de custo para o CNPJ informado.", null);
            }
        }
        if (StringUtils.isNotBlank(token)) {
            boolean tokenRepetido = listar().stream()
                    .filter(n -> token.equals(n.getToken()))
                    .anyMatch(n -> id == null || !id.equals(n.getId()));
            if (tokenRepetido) {
                throw new CadastroException("Existe um centro de custo para o token informado.", null);
            }
            CadastroIuguDTO empresaCadastro = new CadastroIuguDTO();
            empresaCadastro.setCnpj(cnpj);
            empresaCadastro.setRazaoSocial(descricao);
            empresaCadastro.setToken(token);
            empresaCadastro.setIdEmpresa(empresaService.getEmpresa().getId());
            empresaCadastro.setUrlCallback(SystemPreferencesUtil.getProp("defaults.urlCallBackIUGU"));
            ResponseEntity<String> doJsonPost = MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.IUGU, "iugo/v0/empresa", empresaCadastro, new HashMap<>());
            if (!doJsonPost.getStatusCode().is2xxSuccessful()) {
                throw new CadastroException("Ocorreu um erro ao cadastrar o centro de custo na importação do IUGU", null);
            } else if (doJsonPost.getStatusCode().is2xxSuccessful()) {
                try {
                    ObjectMapper m = new ObjectMapper();
                    ReturnWrapper<Object> n = m.readValue(doJsonPost.getBody(), ReturnWrapper.class);
                    if (!n.isStatus()) {
                        throw new CadastroException("Ocorreu um erro ao cadastrar o centro de custo na importação do IUGU", null);
                    }
                } catch (IOException ex) {
                    throw new CadastroException("Ocorreu um erro ao cadastrar o centro de custo na importação do IUGU", ex);
                }
            }
        }
        return super.salvar(centroCusto);
    }

    public CentroCusto findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }

    public List<String> empresasComIugu() {
        return repositorio.getEmpresaComIUGU();
    }

}
