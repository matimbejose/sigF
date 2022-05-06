package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UnidadeMedidaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ProdutoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UnidadeMedidaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.UnidadeMedidaRepositorio;
import javax.annotation.PostConstruct;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UnidadeMedidaService extends BasicService<UnidadeMedida, UnidadeMedidaRepositorio, UnidadeMedidaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    transient ProdutoMapper produtoMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new UnidadeMedidaRepositorio(em, adHocTenant);
    }

    public UnidadeMedida buscarPorSigla(String sigla) {
        return repositorio.buscarPorSigla(sigla);
    }

    public UnidadeMedida buscar(String descricao) {
        return repositorio.buscar(descricao);
    }

    @Override
    public Criteria getModel(UnidadeMedidaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "sigla", filter.getSigla(), MatchMode.ANYWHERE);

        return criteria;
    }

    public void populaUnidadeMedida() {
        populaUnidadeMedida(adHocTenant.getTenantID());
    }

    public void populaUnidadeMedida(String tenant) {
        criaUnidadeMedida("AMPOLA", "AMPOLA", tenant);
        criaUnidadeMedida("BALDE", "BALDE", tenant);
        criaUnidadeMedida("BANDEJ", "BANDEJA", tenant);
        criaUnidadeMedida("BARRA", "BARRA", tenant);
        criaUnidadeMedida("BISNAG", "BISNAGA", tenant);
        criaUnidadeMedida("BLOCO", "BLOCO", tenant);
        criaUnidadeMedida("BOBINA", "BOBINA", tenant);
        criaUnidadeMedida("BOMB", "BOMBONA", tenant);
        criaUnidadeMedida("CAPS", "CAPSULA", tenant);
        criaUnidadeMedida("CART", "CARTELA", tenant);
        criaUnidadeMedida("CENTO", "CENTO", tenant);
        criaUnidadeMedida("CJ", "CONJUNTO", tenant);
        criaUnidadeMedida("CM", "CENTIMETRO", tenant);
        criaUnidadeMedida("CM2", "CENTIMETRO QUADRADO", tenant);
        criaUnidadeMedida("CX", "CAIXA", tenant);
        criaUnidadeMedida("CX2", "CAIXA COM 2 UNIDADES", tenant);
        criaUnidadeMedida("CX3", "CAIXA COM 3 UNIDADES", tenant);
        criaUnidadeMedida("CX5", "CAIXA COM 5 UNIDADES", tenant);
        criaUnidadeMedida("CX10", "CAIXA COM 10 UNIDADES", tenant);
        criaUnidadeMedida("CX15", "CAIXA COM 15 UNIDADES", tenant);
        criaUnidadeMedida("CX20", "CAIXA COM 20 UNIDADES", tenant);
        criaUnidadeMedida("CX25", "CAIXA COM 25 UNIDADES", tenant);
        criaUnidadeMedida("CX50", "CAIXA COM 50 UNIDADES", tenant);
        criaUnidadeMedida("CX100", "CAIXA COM 100 UNIDADES", tenant);
        criaUnidadeMedida("DISP", "DISPLAY", tenant);
        criaUnidadeMedida("DUZIA", "DUZIA", tenant);
        criaUnidadeMedida("EMBAL", "EMBALAGEM", tenant);
        criaUnidadeMedida("FARDO", "FARDO", tenant);
        criaUnidadeMedida("FOLHA", "FOLHA", tenant);
        criaUnidadeMedida("FRASCO", "FRASCO", tenant);
        criaUnidadeMedida("GALAO", "GALÃO", tenant);
        criaUnidadeMedida("GF", "GARRAFA", tenant);
        criaUnidadeMedida("GRAMAS", "GRAMAS", tenant);
        criaUnidadeMedida("JOGO", "JOGO", tenant);
        criaUnidadeMedida("KG", "QUILOGRAMA", tenant);
        criaUnidadeMedida("KIT", "KIT", tenant);
        criaUnidadeMedida("LATA", "LATA", tenant);
        criaUnidadeMedida("LITRO", "LITRO", tenant);
        criaUnidadeMedida("M", "METRO", tenant);
        criaUnidadeMedida("M2", "METRO QUADRADO", tenant);
        criaUnidadeMedida("M3", "METRO CÚBICO", tenant);
        criaUnidadeMedida("MILHEI", "MILHEIRO", tenant);
        criaUnidadeMedida("ML", "MILILITRO", tenant);
        criaUnidadeMedida("MWH", "MEGAWATT HORA", tenant);
        criaUnidadeMedida("PACOTE", "PACOTE", tenant);
        criaUnidadeMedida("PALETE", "PALETE", tenant);
        criaUnidadeMedida("PARES", "PARES", tenant);
        criaUnidadeMedida("PC", "PEÇA", tenant);
        criaUnidadeMedida("POTE", "POTE", tenant);
        criaUnidadeMedida("K", "QUILATE", tenant);
        criaUnidadeMedida("RESMA", "RESMA", tenant);
        criaUnidadeMedida("ROLO", "ROLO", tenant);
        criaUnidadeMedida("SACO", "SACO", tenant);
        criaUnidadeMedida("SACOLA", "SACOLA", tenant);
        criaUnidadeMedida("TAMBOR", "TAMBOR", tenant);
        criaUnidadeMedida("TANQUE", "TANQUE", tenant);
        criaUnidadeMedida("TON", "TONELADA", tenant);
        criaUnidadeMedida("TUBO", "TUBO", tenant);
        criaUnidadeMedida("UNID", "UNIDADE", tenant);
        criaUnidadeMedida("VASIL", "VASILHAME", tenant);
        criaUnidadeMedida("VIDRO", "VIDRO", tenant);
    }

    private void criaUnidadeMedida(String sigla, String descricao, String tenant) {
        UnidadeMedida unidadeMedida = new UnidadeMedida();
        unidadeMedida.setSigla(sigla);
        unidadeMedida.setDescricao(descricao);
        unidadeMedida.setTenatID(tenant);
        adicionar(unidadeMedida);
    }

    public UnidadeMedida importDto(UnidadeMedidaDTO unidadeMedidaDTO, String tenatID) {
        UnidadeMedida unidadeMedida = produtoMapper.toEntity(unidadeMedidaDTO);
        unidadeMedida.setTenatID(tenatID);
        return adicionar(unidadeMedida);
    }

    public UnidadeMedida findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }

}
