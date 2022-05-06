package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstoqueDTO;
import br.com.villefortconsulting.util.DataUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class EstoqueRepositorio extends BasicRepository<Estoque> {

    public EstoqueRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public EstoqueRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Estoque> listar() {
        String jpql = "select e from Estoque e";
        return getPureList(jpql);
    }

    public List<Estoque> listarEstoquePorProduto(Produto produto) {
        String jpql = "select e from Estoque e where e.idProduto = ?1 order by e.data desc, e.id desc";
        return getPureList(jpql, produto);
    }

    public List<Estoque> buscarLancamentosPosterioresDataCrescente(Date dataMovimentacao, Produto produto, Estoque estoque) {
        String jpql = "select b from Estoque b where b.data > ?1 and b.idProduto = ?2 or b.data = ?1 and b.id > ?3 order by b.data asc, b.id asc ";
        return getPureList(jpql, dataMovimentacao, produto, estoque.getId());
    }

    public List<Estoque> buscarLancamentosPosterioresData(Date dataMovimentacao, Produto produto, Estoque estoque) {
        String jpql = "select b from Estoque b where b.data > ?1 and b.idProduto = ?2 or b.data = ?1 and b.id > ?3 order by b.data desc, b.id desc ";
        return getPureList(jpql, dataMovimentacao, produto, estoque.getId());
    }

    public Double buscarSaldo(Date dataMovimentacao, Produto produto) {
        String jpql = "select b.saldo from Estoque b where b.data <= ?1 and b.idProduto = ?2 order by b.id desc ";
        return getPurePojoTop1(jpql, dataMovimentacao, produto);
    }

    public Estoque buscarLancamento(CompraProduto compraProduto) {
        String jpql = "select b from Estoque b where b.idCompraProduto = ?1 ";
        return getPurePojoTop1(jpql, compraProduto);
    }

    public Estoque buscarLancamento(VendaProduto vendaProduto) {
        String jpql = "select b from Estoque b where b.idVendaProduto = ?1 ";
        return getPurePojoTop1(jpql, vendaProduto);
    }

    public Estoque buscarLancamento(Produto produto) {
        String jpql = "select b from Estoque b where b.idProduto = ?1 ";
        return getPurePojoTop1(jpql, produto);
    }

    public List<Estoque> buscarGiroEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) {
        String jpql = "select e from Estoque e where e.tenatID =?2 and e.idProduto =?1 and e.data between ?3 and ?4 and e.idProduto.composto <> 'K' order by e.data desc, e.id desc ";
        return getPureList(jpql, produto, empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public List<Estoque> buscarGiroEstoque(Empresa empresa, Date dataInicio, Date dataFinal) {
        String jpql = "select e from Estoque e where e.tenatID =?1 and e.data between ?2 and ?3 and  e.idProduto.composto <> 'K' order by e.idProduto, e.data desc, e.id desc";
        return getPureList(jpql, empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public List<Estoque> buscarMovimentoEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) {
        return buscarGiroEstoque(produto, empresa, dataInicio, dataFinal);
    }

    public List<Estoque> buscarMovimentoEstoque(Empresa empresa, Date dataInicio, Date dataFinal) {
        String jpql = "select e from Estoque e where e.tenatID =?1 and e.data between ?2 and ?3 and  e.idProduto.composto <> 'K' order by e.data desc, e.id desc";
        return getPureList(jpql, empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public List<Estoque> buscarExtratoEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) {
        String jpql = "select e from Estoque e where e.tenatID =?2 and e.idProduto =?1 and e.data between ?3 and ?4 and  e.idProduto.composto <> 'K' order by e.idProduto, e.id desc";
        return getPureList(jpql, produto, empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public List<Estoque> buscarExtratoEstoque(Empresa empresa, Date dataInicio, Date dataFinal) {
        String jpql = "select e from Estoque e where e.tenatID =?1 and e.data between ?2 and ?3 and  e.idProduto.composto <> 'K' order by e.idProduto, e.id desc";
        return getPureList(jpql, empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public List<EstoqueDTO> buscarPosicaoEstoque(Produto produto, Empresa empresa) {
        Object[] param = new Object[produto == null ? 1 : 2];
        param[0] = empresa.getTenatID();
        StringBuilder sql = new StringBuilder();
        sql.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.EstoqueDTO(")
                .append("  p.descricao, ")
                .append("  p.idUnidadeMedida.sigla, ")
                .append("  p.id, ")
                .append("  e.saldo, ")
                .append("  '', ")
                .append("  coalesce(sum(case ")// Custo unnitário
                .append("    when e.tipoOperacao = 'EN' then (e.totalOperacao) ")
                .append("    when pp is not null then 0 ")
                // .append("    when e.tipoOperacao = 'EN' and e.origem != 'CP' then (p.valorCusto * e.quantidade)")
                .append("    else 0 end")
                .append("  ), 0), ")
                .append("  coalesce(sum(case ")// Valor de venda unnitário
                .append("     when e.tipoOperacao = 'SA'  then (e.totalOperacao ) ")
                .append("    when pp is not null then 0 ")
                // .append("    when e.tipoOperacao = 'SA' and e.origem != 'VP' then (p.valorVenda * e.quantidade) ")
                .append("    else 0 end")
                .append("  ), 0), ")
                .append("  coalesce(sum(case when e.tipoOperacao = 'EN' then e.quantidade else 0 end),0),")
                .append("  coalesce(sum(case when e.tipoOperacao = 'SA' then e.quantidade else 0 end),0),")
                .append("  coalesce(min(e.saldo), 0)")
                .append(") ")
                .append("from Estoque e ")
                .append("left join e.idCompraProduto cp ")
                .append("left join e.idVendaProduto vp ")
                .append("left join e.idProducaoProduto pp ")
                .append("join e.idProduto p ")
                .append("where e.tenatID = ?1 ")
                .append("and e.idProduto.composto <> 'K' ");
        if (produto != null) {
            sql.append("and e.idProduto = ?2 ");
            param[1] = produto;
        }
        sql.append("group by p.descricao, p.idUnidadeMedida.sigla, p.id, e.saldo ");

        List<EstoqueDTO> lista = new ArrayList<>();
        List<EstoqueDTO> listaAux = getPureList(sql.toString(), param);
        for (EstoqueDTO dto : listaAux) {
            EstoqueDTO aux = lista.stream()
                    .filter(item -> item.getIdProduto().equals(dto.getIdProduto()))
                    .findAny().orElse(null);
            if (aux == null) {
                lista.add(dto);
            } else {
                aux.setEstoqueMinimo(dto.getEstoqueMinimo() < aux.getEstoqueMinimo() ? dto.getEstoqueMinimo() : aux.getEstoqueMinimo());
                aux.setCusto(aux.getCusto() + dto.getCusto());
                aux.setValor(aux.getValor() + dto.getValor());
                aux.setQteCusto(aux.getQteCusto() + dto.getQteCusto());
                aux.setQteValor(aux.getQteValor() + dto.getQteValor());
            }
        }

        return lista;
    }

}
