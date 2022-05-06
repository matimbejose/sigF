package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstoqueFiltro extends BasicFilter<Estoque> {

    private static final long serialVersionUID = 1L;

    private Produto produto;

    private String dataInicio;

    private String dataFim;

    private String tipoOperacao;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("produto") != null) {
            produto = new Produto();
            produto.setId(stringToId(urlInfo.getFirst("produto")));
        }
    }

}
