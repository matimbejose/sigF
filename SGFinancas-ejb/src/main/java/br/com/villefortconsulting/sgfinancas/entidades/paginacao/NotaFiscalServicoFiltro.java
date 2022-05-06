package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaFiscalServicoFiltro extends BasicFilter<NFS> {

    private static final long serialVersionUID = 1L;

    private String numero;

    private Double valorNota;

    private Cliente cliente;

    private String email;

    private MinMax<Date> data;

    private List<String> situacoes = new ArrayList<>();

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        numero = urlInfo.getFirst("numero");
        if (urlInfo.getFirst("valorNota") != null) {
            valorNota = Double.parseDouble(urlInfo.getFirst("valorNota"));
        }
        if (urlInfo.getFirst("cliente") != null) {
            cliente = new Cliente();
            cliente.setId(stringToId(urlInfo.getFirst("cliente")));
        }
        email = urlInfo.getFirst("email");
        data = getMinMax(getArray(urlInfo, "data"), Date.class);
        situacoes = urlInfo.get("situacoes");
    }

}
