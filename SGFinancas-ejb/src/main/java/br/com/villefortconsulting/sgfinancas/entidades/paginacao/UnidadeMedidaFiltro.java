package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeMedidaFiltro extends BasicFilter<UnidadeMedida> {

    private static final long serialVersionUID = 1L;

    private String sigla;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        sigla = urlInfo.getFirst("sigla");
    }

}
