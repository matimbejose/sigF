package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CfopFiltro extends BasicFilter<Cfop> {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private String aplicacao;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        codigo = urlInfo.getFirst("codigo");
        aplicacao = urlInfo.getFirst("aplicacao");
    }

}
