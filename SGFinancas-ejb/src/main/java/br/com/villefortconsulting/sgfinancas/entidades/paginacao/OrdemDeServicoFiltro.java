package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemDeServicoFiltro extends BasicFilter<OrdemDeServico> {

    private static final long serialVersionUID = 1L;

    public OrdemDeServicoFiltro() {
        valor = new MinMax<>();
        data = new MinMax<>();
    }

    private Integer numero;

    private Cliente cliente;

    private String email;

    private String telefone;

    private String formaPagamento;

    private Funcionario funcionarioEntrada;

    private Funcionario funcionarioSaida;

    private MinMax<Double> valor;

    private MinMax<Date> data;

    private Usuario usuarioLogado;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        data = getMinMax(getArray(urlInfo, "data"), Date.class);
        if (urlInfo.getFirst("cliente") != null) {
            cliente = new Cliente();
            cliente.setId(stringToId(urlInfo.getFirst("cliente")));
        }
        email = urlInfo.getFirst("email");
        telefone = urlInfo.getFirst("telefone");
        if (urlInfo.getFirst("valor") != null) {
            valor.setExact(Double.parseDouble(urlInfo.getFirst("valor")));
        }
    }

}
