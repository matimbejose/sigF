package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControleFinanceiroDTO;
import java.util.List;
import javax.persistence.EntityManager;

public class FinanceiroRepositorio extends BasicRepository<EntityId> {

    public FinanceiroRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FinanceiroRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<ControleFinanceiroDTO> listar(Integer mes, Integer ano, String nome, String tipo) {
        StringBuilder sql = new StringBuilder();
        if (nome == null) {
            nome = "";
        }

        sql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.ControleFinanceiroDTO(pc.descricao, cl.nome, cp.dataVencimento, cp.valor, cp.situacao) ")
                .append(" from ContaParcela cp ")
                .append(" join cp.idConta co ")
                .append(" join co.idCliente cl ")
                .append(" join co.idPlanoConta pc ")
                .append(" where pc.tipo = ?1 and year(cp.dataVencimento) = ?2 and month(cp.dataVencimento) = ?3 ")
                .append(nome.isEmpty() ? "" : " and cl.nome = ?4 ")
                .append(" order by pc.descricao asc ");

        if (nome.isEmpty()) {
            return getPureList(sql.toString(), tipo, ano, mes);
        } else {
            return getPureList(sql.toString(), tipo, ano, mes, nome);
        }
    }

}
