package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CnaeFiltro extends BasicFilter<Cnae> {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private String classificacao;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        codigo = urlInfo.getFirst("codigo");
        classificacao = urlInfo.getFirst("classificacao");
    }

}
