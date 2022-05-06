package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaFiscalProdutoFiltro extends BasicFilter<NF> {

    private static final long serialVersionUID = 1L;

    private Integer numero;

    private Double valor;

    private Cliente cliente;

    private Fornecedor fornecedor;

    private String email;

    private Date dataEmissaoInicio;

    private Date dataEmissaoFim;

    private MinMax<Date> dataGeracao = new MinMax<>(true);

    private List<String> situacoes;

    private List<String> modelos;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("numeroNotaFiscal") != null) {
            numero = Integer.parseInt(urlInfo.getFirst("numeroNotaFiscal"));
        }
        if (urlInfo.getFirst("valorNota") != null) {
            valor = Double.parseDouble(urlInfo.getFirst("valorNota"));
        }
        if (urlInfo.getFirst("cliente") != null) {
            cliente = new Cliente();
            cliente.setId(stringToId(urlInfo.getFirst("cliente")));
        }
        email = urlInfo.getFirst("email");
        MinMax<Date> mm = getMinMax(getArray(urlInfo, "dataEmissao"), Date.class);
        dataEmissaoInicio = mm.getMin();
        dataEmissaoFim = mm.getMax();
    }

}
