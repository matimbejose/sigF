package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoFiltro extends BasicFilter<Produto> {

    private static final long serialVersionUID = 1L;

    private ProdutoCategoria idProdutoCategoria;

    private UnidadeMedida idUnidadeMedida;

    private List<String> tipoComposicao = new ArrayList<>();

    private String codigo;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (ativo == null) {
            ativo = "S";
        }
        codigo = urlInfo.getFirst("codigo");
        if (urlInfo.getFirst("idUnidadeMedida.descricao") != null) {
            idUnidadeMedida = new UnidadeMedida();
            idUnidadeMedida.setId(stringToId(urlInfo.getFirst("idUnidadeMedida.descricao")));
        }
        if (urlInfo.getFirst("idProdutoCategoria.descricao") != null) {
            idProdutoCategoria = new ProdutoCategoria();
            idProdutoCategoria.setId(stringToId(urlInfo.getFirst("idProdutoCategoria.descricao")));
        }
        tipoComposicao = urlInfo.getOrDefault("tipoComposicao", new ArrayList<>());
        descricao = urlInfo.getFirst("nome");
    }

}
