package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CidadeRepositorio;
import br.com.villefortconsulting.util.CepUtil;
import br.com.villefortconsulting.util.operator.In;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CidadeService extends BasicService<Cidade, CidadeRepositorio, CidadeFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private UFService ufService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CidadeRepositorio(em);
    }

    public Cidade buscar(String descricao) {
        return repositorio.buscar(descricao);
    }

    public List<String> getCodigoIbgeMunicipiosISSQN() {
        List<String> cidades = new LinkedList<>();
        cidades.add("3106200");
        cidades.add("4323002");
        return cidades;
    }

    public List<Cidade> listarMunicipiosNFS() {
        return getCodigoIbgeMunicipiosISSQN().stream()
                .map(repositorio::buscarPeloCodigoIBGE)
                .collect(Collectors.toList());
    }

    public List<Cidade> listar(UF uf) {
        return repositorio.listar(uf);
    }

    public CepDTO getEnderecoPorCep(String cep) {
        CepDTO cepDTO = new CepDTO(CepUtil.consultarEndereco(cep));

        if (cepDTO.getDescricaoCidade() != null) {
            cepDTO.setCidade(repositorio.buscar(cepDTO.getDescricaoCidade()));
        }

        if (cepDTO.getDescricaoUf() != null) {
            cepDTO.setUf(ufService.buscar(cepDTO.getDescricaoUf()));
        }

        return cepDTO;
    }

    public CepDTO buscarEnderecoPorCep(Empresa empresa) {
        if (empresa.getEndereco().getCep() == null) {
            return null;
        }
        CepDTO cepDTO = getEnderecoPorCep(empresa.getEndereco().getCep());

        empresa.getEndereco().setIdCidade(cepDTO.getCidade());
        empresa.getEndereco().setEndereco(cepDTO.getEndereco());
        empresa.getEndereco().setBairro(cepDTO.getBairro());

        return cepDTO;
    }

    public Cidade buscarPeloCodigoIBGE(String codigoIBGE) {
        return repositorio.buscarPeloCodigoIBGE(codigoIBGE);
    }

    @Override
    public Criteria getModel(CidadeFiltro filtro) {
        Criteria criteria = super.getModel(filtro);
        criteria.createAlias("idUF", "idUF");
        addIlikeRestrictionTo(criteria, "descricao", filtro.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "idUF.descricao", filtro.getUf());
        if (filtro.isEmissaoNFS()) {
            addInRestrictionTo(criteria, "codigoIBGE", In.fromList(getCodigoIbgeMunicipiosISSQN()));
        }
        return criteria;

    }

}
