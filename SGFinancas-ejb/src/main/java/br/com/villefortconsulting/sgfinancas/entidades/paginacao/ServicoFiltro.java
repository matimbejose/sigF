package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoFiltro extends BasicFilter<Servico> {

    private static final long serialVersionUID = 1L;

    private ProdutoCategoria idProdutoCategoria;

    private Double custoServico;

    private Double valorVenda;

    private Funcionario idFuncionario;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("custoServico") != null) {
            custoServico = Double.parseDouble(urlInfo.getFirst("custoServico"));
        }
        if (urlInfo.getFirst("idProdutoCategoria.descricao") != null) {
            idProdutoCategoria = new ProdutoCategoria();
            idProdutoCategoria.setId(stringToId(urlInfo.getFirst("idProdutoCategoria.descricao")));
        }
        if (urlInfo.getFirst("funcionario") != null) {
            idFuncionario = new Funcionario();
            idFuncionario.setId(stringToId(urlInfo.getFirst("funcionario")));
        }
        if (urlInfo.getFirst("valorVenda") != null) {
            valorVenda = Double.parseDouble(urlInfo.getFirst("valorVenda"));
        }
    }

}
