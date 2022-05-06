package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContratoFiltro extends BasicFilter<Contrato> {

    private static final long serialVersionUID = 1L;

    public ContratoFiltro() {
        data = new MinMax<>();
    }

    private Cliente cliente;

    private Fornecedor fornecedor;

    private PlanoConta planoConta;

    private MinMax<Date> data;

    private Integer numParcelas;

    private Double valor;

    private Double valorAdesao;

    private Double valorInstalacao;

    private String tipoContrato;

    private String situacao;

    private Boolean expirando = false;

    public boolean isExpirando() {
        return expirando;
    }

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        if (urlInfo.getFirst("cliente") != null) {
            cliente = new Cliente();
            cliente.setId(stringToId(urlInfo.getFirst("cliente")));
        }
        if (urlInfo.getFirst("fornecedor") != null) {
            fornecedor = new Fornecedor();
            fornecedor.setId(stringToId(urlInfo.getFirst("fornecedor")));
        }
        if (urlInfo.getFirst("planoConta") != null) {
            planoConta = new PlanoConta();
            planoConta.setId(stringToId(urlInfo.getFirst("planoConta")));
        }
        descricao = urlInfo.getFirst("observacao");
        if (urlInfo.getFirst("dataInicio") != null) {
            data.setMin(new Date(Long.parseLong(urlInfo.getFirst("dataInicio"))));
        }
        if (urlInfo.getFirst("dataFim") != null) {
            data.setMax(new Date(Long.parseLong(urlInfo.getFirst("dataFim"))));
            data.setMax(DataUtil.adicionarDias(data.getMax(), 1));
        }
        if (urlInfo.getFirst("numeroParcelas") != null) {
            numParcelas = Integer.parseInt(urlInfo.getFirst("numeroParcelas"));
        }
        if (urlInfo.getFirst("valorContrato") != null) {
            valor = Double.parseDouble(urlInfo.getFirst("valorContrato"));
        }
        if (urlInfo.getFirst("taxaAdesao") != null) {
            valorAdesao = Double.parseDouble(urlInfo.getFirst("taxaAdesao"));
        }
        if (urlInfo.getFirst("taxaInstalacao") != null) {
            valorInstalacao = Double.parseDouble(urlInfo.getFirst("taxaInstalacao"));
        }
        tipoContrato = urlInfo.getFirst("tipoContrato");
    }

}
