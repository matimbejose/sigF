package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuloFiltro extends BasicFilter<Modulo> {

    private static final long serialVersionUID = 1L;

    private String permiteRenovacao;

    private String obrigatorio;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        permiteRenovacao = urlInfo.getFirst("permiteRenovacao");
        obrigatorio = urlInfo.getFirst("obrigatorio");
    }

}
