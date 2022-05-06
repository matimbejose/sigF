package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioAnaliticoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteSaldoDTO;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class ClienteMovimentacaoRepositorio extends BasicRepository<ClienteMovimentacao> {

    public ClienteMovimentacaoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<ClienteMovimentacao> listaTodasMovimentacoesPor(Cliente cliente) {
        return getPureList("select cm from ClienteMovimentacao cm where cm.idCliente = ?1 and cm.ativo = 'S' order by cm.dataVencimento", cliente);
    }

    public List<ClienteMovimentacao> listaTodasMovimentacoesPor(Cliente cliente, String idIntegracao) {
        return getPureList("select cm from ClienteMovimentacao cm where cm.idCliente = ?1 and cm.idIntegracao = ?2 and cm.ativo = 'S'", cliente, idIntegracao);
    }

    public ClienteMovimentacao findMovimentacaobyIdIntegracao(Cliente cliente, String idIntegracao) {
        return getPurePojo("select cm from ClienteMovimentacao cm where cm.idCliente = ?1 and cm.idIntegracao = ?2 and cm.ativo = 'S'", cliente, idIntegracao);
    }

    public ClienteMovimentacao findMovimentacaobyIdIntegracao(String idIntegracao) {
        return getPurePojoTop1("select cm from ClienteMovimentacao cm where cm.idIntegracao = ?1 and cm.ativo = 'S'", idIntegracao);
    }

    public List<ClienteSaldoDTO> listaSaldo(Date dataInicial, Date dataFinal, CentroCusto centro) {
        StringBuilder sb = new StringBuilder();
        sb.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteSaldoDTO(");
        sb.append("cm.idCliente.nome,");
        sb.append("sum(cm.valorSaldo)");
        sb.append(") from ClienteMovimentacao cm ");
        sb.append("where cm.dataVencimento between ?1 and ?2 and cm.tenatID = ?3 and cm.ativo = 'S'");
        if (centro != null) {
            sb.append(" and cm.idCentroCusto = ?4 group by cm.idCliente.nome order by cm.idCliente.nome");
            return getPureList(sb.toString(), dataInicial, dataFinal, adHocTenant.getTenantID(), centro);
        }
        sb.append(" group by cm.idCliente.nome");
        sb.append(" order by cm.idCliente.nome");
        return getPureList(sb.toString(), dataInicial, dataFinal, adHocTenant.getTenantID());
    }

    public Double getSaldoAnterior(Date dataInicial) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(cm1.valorSaldoAnterior) ")
                .append("from ClienteMovimentacao cm1 ")
                .append("where cm1.tenatID = ?2 and cm1.dataVencimento <= ?1 and cm1.ativo = 'S' ")
                .append("and cm1.dataVencimento = (")
                .append("  select max(cm2.dataVencimento) ")
                .append("  from ClienteMovimentacao cm2 ")
                .append("  where cm2.idCliente = cm1.idCliente and cm2.ativo = 'S'")
                .append(")")
                .append("and cm1.id = (")
                .append("  select max(cm3.id) ")
                .append("  from ClienteMovimentacao cm3 ")
                .append("  where cm3.idCliente = cm1.idCliente and cm3.dataVencimento = cm1.dataVencimento and cm3.ativo = 'S'")
                .append(")");
        return getPurePojoTop1(Double.class, sb.toString(), dataInicial, adHocTenant.getTenantID());
    }

    public List<ClienteMovimentacaoRelatorioAnaliticoDTO> listaMovimentacoesRelatorio(Cliente cliente, Date inicio, Date fim, Empresa empresa, CentroCusto centro) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioAnaliticoDTO( ");
        jpql.append(" cm.id, cm.idCliente, cm.origem, cm.dataMovimentacao, cm.dataVencimento, cm.dataPagamento, cm.valorPrevisto, ");
        jpql.append(" cm.valorPago, cm.valorJuros, cm.valorSaldo, cm.valorSaldoAnterior )");
        jpql.append(" from ClienteMovimentacao cm ");
        jpql.append(" where cm.dataMovimentacao >= ?1 and cm.dataMovimentacao <= ?2 and cm.ativo = 'S' ");
        jpql.append(" and cm.tenatID = ?3 ");
        jpql.append(" and cm.dataCancelamento is null ");
        if (cliente != null) {
            jpql.append(" and cm.idCliente = ?4 order by cm.idCliente, cm.dataVencimento");
            return getPureList(jpql.toString(), inicio, fim, empresa.getTenatID(), cliente);
        }
        if (centro != null) {
            jpql.append(" and cm.idCentroCusto = ?4 order by cm.idCliente, cm.dataVencimento");
            return getPureList(jpql.toString(), inicio, fim, empresa.getTenatID(), centro);
        }
        jpql.append(" order by cm.idCliente, cm.dataVencimento");
        return getPureList(jpql.toString(), inicio, fim, empresa.getTenatID());
    }

    public List<ClienteMovimentacaoRelatorioAnaliticoDTO> listaMovimentacoesSemIuguRelatorio(Cliente cliente, Date inicio, Date fim, Empresa empresa) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioAnaliticoDTO( ");
        jpql.append(" cm.id, cm.idCliente, cm.origem, cm.dataMovimentacao, cm.dataVencimento, cm.valorPrevisto) ");
        jpql.append(" from ClienteMovimentacao cm ");
        jpql.append(" where cm.dataMovimentacao >= ?1 and cm.dataMovimentacao <= ?2 and cm.ativo = 'S' ");
        jpql.append(" and cm.tenatID = ?3 ");
        jpql.append(" and cm.dataCancelamento is null ");
        jpql.append(" and cm.idCliente = ?4 order by cm.idCliente, cm.dataVencimento");
        return getPureList(jpql.toString(), inicio, fim, empresa.getTenatID(), cliente);
    }

    public List<Cliente> clientesComNotasNoPeriodo(Date inicio, Date fim, String tenat) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select distinct cm.idCliente ");
        jpql.append(" from ClienteMovimentacao cm ");
        jpql.append(" where (cm.dataMovimentacao >= ?1 and cm.dataMovimentacao <= ?2 and cm.ativo = 'S' ");
        jpql.append(" and (cm.origem='NFSE' or cm.origem='NFE' or cm.origem = 'NFCE' or cm.origem = 'CFE')) ");
        jpql.append(" and cm.tenatID = ?3  ");
        return getPureList(jpql.toString(), inicio, fim, tenat);
    }

    public List<Cliente> clientesComIuguNoPeriodo(Date inicio, Date fim, String tenat) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select distinct cm.idCliente ");
        jpql.append(" from ClienteMovimentacao cm ");
        jpql.append(" where cm.dataMovimentacao >= ?1 and cm.dataMovimentacao <= ?2 and cm.ativo = 'S' ");
        jpql.append(" and cm.origem='IUGU'  ");
        jpql.append(" and cm.tenatID = ?3  ");
        return getPureList(jpql.toString(), inicio, fim, tenat);
    }

    public List<ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO> acumuladoPorTipoMovimentacao(Date inicio, Date fim, String tenat) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO( ");
        jpql.append(" cm.origem, cm.tipoMovimentacao, SUM(cm.valorPrevisto) )");
        jpql.append(" from ClienteMovimentacao cm ");
        jpql.append(" where cm.dataMovimentacao >= ?1 and cm.dataMovimentacao <= ?2 and cm.ativo = 'S' ");
        jpql.append(" and cm.tenatID = ?3 and cm.dataCancelamento is null");
        jpql.append(" group by cm.origem, cm.tipoMovimentacao order by cm.origem ");
        return getPureList(jpql.toString(), inicio, fim, tenat);
    }

}
