package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeFiltro extends BasicFilter<Cidade> {

    private static final long serialVersionUID = 1L;

    private String uf;

    private boolean emissaoNFS = false;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        uf = urlInfo.getFirst("uf");
        emissaoNFS = "true".equals(urlInfo.getFirst("emissaoNFS"));
    }

}
