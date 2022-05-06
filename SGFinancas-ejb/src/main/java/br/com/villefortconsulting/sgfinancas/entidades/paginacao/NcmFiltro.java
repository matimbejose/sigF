package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcmFiltro extends BasicFilter<Ncm> {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private String codigoPai;

    private String tipo;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        codigo = urlInfo.getFirst("codigo");
        codigoPai = urlInfo.getFirst("codigoPai");
        tipo = urlInfo.getFirst("tipo");
    }

}
