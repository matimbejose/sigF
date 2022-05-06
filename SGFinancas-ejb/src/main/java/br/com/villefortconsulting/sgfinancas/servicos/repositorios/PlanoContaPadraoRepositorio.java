package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaPadrao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaCadastroAuxDTO;
import java.util.List;
import javax.persistence.EntityManager;

public class PlanoContaPadraoRepositorio extends BasicRepository<PlanoContaPadrao> {

    public PlanoContaPadraoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PlanoContaPadraoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<PlanoContaPadrao> obterTodosItensPlanoConta() {
        String jpq1 = "select p from PlanoContaPadrao p order by p.codigo";
        return getPureList(jpq1);
    }

    public PlanoContaPadrao buscarPlanoContaPadrao(String codigo) {
        String jpq1 = "select p from PlanoContaPadrao p where p.codigo = ?1 ";
        return getPurePojo(jpq1, codigo);
    }

    public void aplicarPlanoContaAEmpresa(String tenatID) {
        String sql = "insert into PlanoConta (codigo, descricao, tipoBalanco, mostraTelaInicial, registroPadrao, tipoIndicador, tipo, podeTerFilho, tenatID)"
                + "   select pcp.codigo, pcp.descricao, pcp.tipoBalanco, pcp.mostraTelaInicial, pcp.registroPadrao, pcp.tipoIndicador, pcp.tipo, pcp.podeTerFilho, ?1"
                + "   from PlanoContaPadrao pcp";

        executeCommand(sql, tenatID);

        StringBuilder sqlBusca = new StringBuilder();

        sqlBusca.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaCadastroAuxDTO( ")
                .append(" (select p2.id from PlanoConta p2 where pcp.codigoPai = p2.codigo and p2.tenatID = ?1), ")
                .append(" (select p1.id from PlanoConta p1 where pcp.codigo = p1.codigo and p1.tenatID = ?1) ")
                .append(" ) from PlanoContaPadrao pcp ")
                .append(" where pcp.codigo like '_.%'");

        getPureList(sqlBusca.toString(), tenatID).stream()
                .map(PlanoContaCadastroAuxDTO.class::cast)
                .map(cod -> "UPDATE PLANO_CONTA SET ID_CONTA_PAI = " + cod.getIdContaPai() + " WHERE ID = " + cod.getIdConta())
                .forEach(this::executeCommandByNativeQuery);
    }

}
