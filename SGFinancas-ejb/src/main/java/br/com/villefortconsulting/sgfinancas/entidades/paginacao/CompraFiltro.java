package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraFiltro extends BasicFilter<Compra> {

    private static final long serialVersionUID = 1L;

    public CompraFiltro() {
        data = new MinMax<>();
    }

    private MinMax<Date> data;

    private Fornecedor fornecedor;

    private Produto produto;

    private Double valor;

    private Integer parcelas;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        data = getMinMax(getArray(urlInfo, "data"), Date.class);
        if (urlInfo.getFirst("fornecedor") != null) {
            fornecedor = new Fornecedor();
            fornecedor.setId(stringToId(urlInfo.getFirst("fornecedor")));
        }
        if (urlInfo.getFirst("produto") != null) {
            produto = new Produto();
            produto.setId(stringToId(urlInfo.getFirst("produto")));
        }
        if (urlInfo.getFirst("valor") != null) {
            valor = Double.parseDouble(urlInfo.getFirst("valor"));
        }
        if (urlInfo.getFirst("parcelas") != null) {
            parcelas = Integer.parseInt(urlInfo.getFirst("parcelas"));
        }
        if ("A".equals(ativo)) {
            ativo = null;
        } else {
            String situacao = urlInfo.getFirst("situacao");
            if (situacao != null) {
                ativo = situacao;
            }
            if (ativo != null && ("A".equals(ativo) || "C".equals(ativo))) {
                ativo = "A".equals(ativo) ? "S" : "N";
            }

        }
    }

}
