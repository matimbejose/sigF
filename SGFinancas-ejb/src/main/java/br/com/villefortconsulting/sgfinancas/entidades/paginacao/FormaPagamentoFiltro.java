package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoFiltro extends BasicFilter<FormaPagamento> {

    private static final long serialVersionUID = 1L;

    private String codigoNfe;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        codigoNfe = urlInfo.getFirst("codigoNfe");
    }

}
