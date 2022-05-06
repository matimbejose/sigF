package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BancoFiltro extends BasicFilter<Banco> {

    private static final long serialVersionUID = 1L;

    private String numero;

    private String site;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        site = urlInfo.getFirst("site");
        numero = urlInfo.getFirst("numero");
    }

}
