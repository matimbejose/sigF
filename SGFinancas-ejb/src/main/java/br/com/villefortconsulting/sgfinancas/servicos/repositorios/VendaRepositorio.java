package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaSeguradora;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutosMaisVendidosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoPorClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicosMaisVendidosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaPorVendedorDTO;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class VendaRepositorio extends BasicRepository<Venda> {

    public VendaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public VendaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public VendaProduto adicionarVendaProduto(VendaProduto vendaProduto) {
        addEntity(vendaProduto);
        return vendaProduto;
    }

    public VendaProduto alterarVendaProduto(VendaProduto vendaProduto) {
        return setEntity(vendaProduto);
    }

    public void removerVendaProduto(VendaProduto vendaProduto) {
        removeEntity(vendaProduto);
    }

    public VendaProduto buscarVendaProduto(int id) {
        return getEntity(VendaProduto.class, id);
    }

    public List<VendaProduto> listarVendaProduto(Venda venda) {
        String jpql = "select v from VendaProduto v where v.idVenda = ?1";
        return getPureList(jpql, venda);
    }

    public Boolean verificaStatusOrcamentoVinculado(Venda venda) {
        String jpql = "select v from Venda v where v.id = ?1 and v.statusNegociacao = 'AP'";
        return getPurePojo(jpql, venda.getId()) != null;
    }

    public List<VendaProduto> listarVendaProduto(Integer idVenda) {
        String jpql = "select v from VendaProduto v where v.idVenda.id = ?1";
        return getPureList(jpql, idVenda);
    }

    public List<VendaItemOrdemDeServico> listarVendaItensOrdemDeServico(Venda idVenda) {
        String jpql = "select viods from VendaItemOrdemDeServico viods where viods.idVenda = ?1";
        return getPureList(jpql, idVenda);
    }

    public List<VendaItemOrdemDeServico> listarVendaItensOrdemDeServico(Integer idVenda) {
        String jpql = "select viods from VendaItemOrdemDeServico viods where viods.idVenda.id = ?1";
        return getPureList(jpql, idVenda);
    }

    public VendaServico adicionarVendaServico(VendaServico vendaServico) {
        addEntity(vendaServico);
        return vendaServico;
    }

    public VendaServico alterarVendaServico(VendaServico vendaServico) {
        return setEntity(vendaServico);
    }

    public void removerVendaServico(VendaServico vendaServico) {
        removeEntity(vendaServico);
    }

    public VendaItemOrdemDeServico alterarVendaItensOrdemDeServico(VendaItemOrdemDeServico vendaServico) {
        return setEntity(vendaServico);
    }

    public void removerVendaItensOrdemDeServico(VendaItemOrdemDeServico vendaServico) {
        removeEntity(vendaServico);
    }

    public VendaItemOrdemDeServico buscarVendaItensOrdemDeServico(int id) {
        return getEntity(VendaItemOrdemDeServico.class, id);
    }

    public VendaServico buscarVendaServico(int id) {
        return getEntity(VendaServico.class, id);
    }

    public List<VendaServico> listarVendaServico(Venda venda) {
        String jpql = "select v from VendaServico v where v.idVenda = ?1";
        return getPureList(jpql, venda);
    }

    public VendaSeguradora buscaVendaSeguradoraID(Integer id) {
        String jpql = "select vs from VendaSeguradora vs where vs.id = ?1";
        return getPurePojo(jpql, id);
    }

    public List<VendaServico> listarVendaServico(Integer idVenda) {
        String jpql = "select v from VendaServico v where v.idVenda.id = ?1";
        return getPureList(jpql, idVenda);
    }

    public boolean vendaPossuiNF(Venda venda) {
        String jpq1 = "select p.id from NF p where p.idVenda = ?1 and p.situacao <> ?2";
        return getPurePojoTop1(Integer.class, jpq1, venda, EnumSituacaoNF.CANCELADA.getSituacao()) != null;
    }

    public boolean vendaPossuiNFServico(Venda venda) {
        String jpq1 = "select p.id from NFS p where p.idVenda = ?1";
        return getPurePojoTop1(Integer.class, jpq1, venda) != null;
    }

    public boolean vendaPossuiProduto(Venda venda) {
        String jpq1 = "select p.id from VendaProduto p where p.idVenda = ?1";
        return getPurePojoTop1(jpq1, venda) != null;
    }

    public boolean vendaPossuiServico(Venda venda) {
        String jpq1 = "select p.id from VendaServico p where p.idVenda = ?1";
        return getPurePojoTop1(jpq1, venda) != null;
    }

    public boolean vendaPossuiOrdemDeServico(Venda venda) {
        String jpq1 = "select p.id from VendaItemOrdemDeServico p where p.idVenda = ?1";
        return getPurePojoTop1(jpq1, venda) != null;
    }

    public List<ValorLancamentoDTO> obterValorPorProduto(Date dataInicio, Date dataFim, String tipoProduto) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(");
        jpql.append("year(cp.idVenda.dataVenda), month(cp.idVenda.dataVenda), sum(cp.dadosProduto.quantidade * cp.dadosProduto.valor) ");
        jpql.append(")  ");
        jpql.append("from VendaProduto cp   ");
        jpql.append("where cp.tenatID = ?3 and cp.idVenda.dataVenda >= ?1 and cp.idVenda.dataVenda <= ?2 ");
        jpql.append("and cp.dadosProduto.idProduto.tipo = ?4 ");
        jpql.append("group by year(cp.idVenda.dataVenda), month(cp.idVenda.dataVenda) ");

        return getPureList(jpql.toString(), dataInicio, dataFim, adHocTenant.getTenantID(), tipoProduto);
    }

    public List<ValorLancamentoDTO> obterValorPorServico(Date dataInicio, Date dataFim) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(");
        jpql.append("year(cp.idVenda.dataVenda), month(cp.idVenda.dataVenda), sum(cp.valorVenda) ");
        jpql.append(")  ");
        jpql.append("from VendaServico cp   ");
        jpql.append("where cp.tenatID = ?3 and cp.idVenda.dataVenda >= ?1 and cp.idVenda.dataVenda <= ?2 ");
        jpql.append("group by year(cp.idVenda.dataVenda), month(cp.idVenda.dataVenda) ");

        return getPureList(jpql.toString(), dataInicio, dataFim, adHocTenant.getTenantID());
    }

    public List<VendaPorVendedorDTO> listarVendaProdutoPorVendedor(Usuario usuario, Empresa empresa, Date dataInicio, Date dataFinal) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.VendaPorVendedorDTO( ");
        jpql.append("vp.idVenda.idUsuarioVendedor.nome, ")
                .append("vp.dadosProduto.idProduto.descricao, ")
                .append("vp.dadosProduto.idProduto.idUnidadeMedida.sigla, ")
                .append("sum(vp.dadosProduto.valor * vp.dadosProduto.quantidade), ")
                .append("sum(vp.dadosProduto.quantidade)")
                .append(") ")
                .append("from VendaProduto vp ")
                .append("where ")
                .append("vp.tenatID = ?1 and vp.idVenda.dataVenda between ?2 and ?3 and vp.idVenda.situacao = 'A' and vp.idVenda.statusNegociacao = 'VD' ");

        // usuario não é obrigatorio
        if (usuario != null) {
            jpql.append("and vp.idVenda.idUsuarioVendedor = ?4 ");
        }

        jpql.append("group by vp.idVenda.idUsuarioVendedor.nome, vp.dadosProduto.idProduto.descricao, vp.dadosProduto.idProduto.idUnidadeMedida.sigla ")
                .append("order by vp.idVenda.idUsuarioVendedor.nome, sum(vp.dadosProduto.quantidade) desc, vp.dadosProduto.idProduto.descricao");

        if (usuario != null) {
            return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal), usuario);
        }
        return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public List<VendaPorVendedorDTO> listarVendaProdutoPorCliente(Cliente cliente, Empresa empresa, Date dataInicio, Date dataFinal) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.VendaPorVendedorDTO( ");
        jpql.append("vp.idVenda.idCliente.nome, ")
                .append("vp.dadosProduto.idProduto.descricao, ")
                .append("vp.dadosProduto.idProduto.idUnidadeMedida.sigla, ")
                .append("sum(vp.dadosProduto.valor * vp.dadosProduto.quantidade), ")
                .append("sum(vp.dadosProduto.quantidade)")
                .append(") ")
                .append("from VendaProduto vp ");
        jpql.append("where ");
        jpql.append("vp.tenatID = ?1 and vp.idVenda.dataVenda between ?2 and ?3 and vp.idVenda.situacao = 'A' and vp.idVenda.statusNegociacao = 'VD' ");

        if (cliente != null) {
            jpql.append("and vp.idVenda.idCliente = ?4 ");
        }

        jpql.append("group by vp.idVenda.idCliente.nome, vp.dadosProduto.idProduto.descricao, vp.dadosProduto.idProduto.idUnidadeMedida.sigla ")
                .append("order by vp.idVenda.idCliente.nome, sum(vp.dadosProduto.quantidade) desc, vp.dadosProduto.idProduto.descricao");

        if (cliente != null) {
            return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal), cliente);
        } else {
            return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
        }
    }

    public List<ServicoPorClienteDTO> listarVendaServicoPorCliente(Cliente cliente, Empresa empresa, Date dataInicio, Date dataFinal) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoPorClienteDTO( ");
        jpql.append("vc.idVenda.idCliente.nome, vc.idServico.descricao, vc.valorVenda, vc.custo");
        jpql.append(") ");
        jpql.append("from VendaServico vc ");
        jpql.append("where ");
        jpql.append("vc.tenatID =?1 and vc.idVenda.statusNegociacao= 'VD' and vc.idVenda.dataVenda between ?2 and ?3 ");

        if (cliente != null) {
            jpql.append("and vc.idVenda.idCliente = ?4 ");
        }

        jpql.append("order by vc.idVenda.idCliente.nome, vc.idServico.descricao ");

        if (cliente != null) {
            return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal), cliente);
        } else {
            return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
        }
    }

    public List<ProdutosMaisVendidosDTO> listarProdutosMaisVendidos(Empresa empresa, Date dataInicio, Date dataFinal) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutosMaisVendidosDTO( ");
        jpql.append("vc.dadosProduto.idProduto.descricao, vc.dadosProduto.idProduto.idUnidadeMedida.sigla, sum(vc.dadosProduto.quantidade), sum(vc.dadosProduto.valor*vc.dadosProduto.quantidade) ");
        jpql.append(") ");
        jpql.append("from VendaProduto vc ");
        jpql.append("where ");
        jpql.append("vc.tenatID =?1 and vc.idVenda.statusNegociacao = 'VD' and vc.idVenda.dataVenda between ?2 and ?3 and vc.idVenda.dataCancelamento is null ");

        jpql.append("group by vc.dadosProduto.idProduto.descricao, vc.dadosProduto.idProduto.idUnidadeMedida.sigla ");
        jpql.append("order by sum(vc.dadosProduto.quantidade) desc ");

        return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));

    }

    public List<ServicosMaisVendidosDTO> listarServicosMaisVendidos(Empresa empresa, Date dataInicio, Date dataFinal) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ServicosMaisVendidosDTO( ");
        jpql.append("vs.idServico.descricao, sum(vs.quantidade), sum(vs.valorVenda*vs.quantidade) ");
        jpql.append(") ");
        jpql.append("from VendaServico vs ");
        jpql.append("where ");
        jpql.append("vs.tenatID =?1 and vs.idVenda.statusNegociacao = 'VD' and vs.idVenda.dataVenda between ?2 and ?3 and vs.idVenda.dataCancelamento is null ");

        jpql.append("group by vs.idServico.descricao ");
        jpql.append("order by vs.idServico.descricao ");

        return getPureList(jpql.toString(), empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));

    }

    public List<Cliente> listarCliente(Empresa empresa) {
        String jpql = "select distinct c.idCliente from Venda c where c.tenatID =?1 order by c.idCliente.nome";
        return getPureList(jpql, empresa.getTenatID());
    }

    public List<Cliente> listarClientePorServico(Empresa empresa) {
        String jpql = "select distinct c.idVenda.idCliente from VendaServico c where c.tenatID =?1 order by c.idVenda.idCliente.nome";
        return getPureList(jpql, empresa.getTenatID());
    }

    public List<Usuario> listarUsuario(Empresa empresa) {
        String jpql = "select distinct v.idUsuarioVendedor from Venda v where v.tenatID =?1 order by v.idUsuarioVendedor.nome";
        return getPureList(jpql, empresa.getTenatID());
    }

    public Venda vendaPorConta(Conta conta) {
        String jpql = "select v from Venda v where v.idConta = ?1";
        return getPurePojo(jpql, conta);
    }

    public List<VendaProduto> listarVendaPorProduto(Produto produto) {
        String jpql = " select vp from VendaProduto vp where vp.dadosProduto.idProduto = ?1 and vp.tenatID = ?2";
        return getPureList(jpql, produto, adHocTenant.getTenantID());
    }

    public List<VendaFormaPagamento> listarVendaFormaPagamento(Venda vendaSelecionada) {
        String jpql = " select vfp from VendaFormaPagamento vfp where vfp.idVenda = ?1 and vfp.tenatID = ?2 ";
        return getPureList(jpql, vendaSelecionada, adHocTenant.getTenantID());
    }

    public List<VendaFormaPagamento> listarVendaFormaPagamento(Integer vendaSelecionada) {
        String jpql = " select vfp from VendaFormaPagamento vfp where vfp.idVenda.id = ?1 and vfp.tenatID = ?2 ";
        return getPureList(jpql, vendaSelecionada, adHocTenant.getTenantID());
    }

    public List<String> getFormasDePagamentoAssociadas(Venda orcamento) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select fp.descricao ")
                .append(" from VendaFormaPagamento vfp ")
                .append(" join vfp.idFormaPagamento fp ")
                .append(" where vfp.idVenda = ?1 and vfp.tenatID = ?2 ");
        return getPureList(sql.toString(), orcamento, adHocTenant.getTenantID());
    }

    public boolean temVendaPorIdOrcamentoOsOrigem(Venda venda) {
        return getPurePojo(Long.class, "select count(v.id) from Venda v where v.idOrcamentoOSOrigem = ?1", venda) > 0l;
    }

}
