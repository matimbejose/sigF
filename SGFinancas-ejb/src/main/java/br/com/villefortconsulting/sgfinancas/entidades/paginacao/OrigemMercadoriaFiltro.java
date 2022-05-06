package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.OrigemMercadoria;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrigemMercadoriaFiltro extends BasicFilter<OrigemMercadoria> {

    private static final long serialVersionUID = 1L;

    private Integer codigo;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("codOrigemMercadoria") != null) {
            codigo = Integer.parseInt(urlInfo.getFirst("codOrigemMercadoria"));
        }
    }

}
