package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CtissFiltro extends BasicFilter<Ctiss> {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private Cidade cidade;

    private String subItem;

    private Double aliquota;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        codigo = urlInfo.getFirst("codigo");
        subItem = urlInfo.getFirst("subitem");
        if (urlInfo.getFirst("cidade") != null) {
            cidade = new Cidade();
            cidade.setId(Integer.parseInt(urlInfo.getFirst("cidade")));
        }
        if (urlInfo.getFirst("aliquota") != null) {
            aliquota = Double.parseDouble(urlInfo.getFirst("aliquota"));
        }
    }

}
