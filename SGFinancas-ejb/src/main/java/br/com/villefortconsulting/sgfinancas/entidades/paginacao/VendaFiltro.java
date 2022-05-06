package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.In;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Arrays;
import java.util.Date;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaFiltro extends BasicFilter<Venda> {

    private static final long serialVersionUID = 1L;

    public VendaFiltro() {
        data = new MinMax<>();
        dataVencimento = new MinMax<>();
    }

    public VendaFiltro(Date data, Funcionario funcionario) {
        this.data = new MinMax<>();
        this.dataVencimento = new MinMax<>();
        this.dataVencimento.setMin(data);
        this.dataVencimento.setMax(DataUtil.adicionarDias(data, 1));
        this.funcionario = funcionario;
        this.origem = In.fromList(Arrays.asList(EnumOrigemVenda.AGENDAMENTO.getOrigem(), EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem()));
    }

    private String id;

    private MinMax<Date> data;

    private MinMax<Date> dataVencimento;

    private EnumTipoVenda tipo;

    private String statusNegociacao;

    private Cliente cliente;

    private Double valor;

    private String numero;

    private FormaPagamento idFormaPagamento;

    private Integer parcelas;

    private Usuario usuarioLogado;

    private Funcionario funcionario;

    private Servico servico;

    private In<String> origem;

    private In<String> situacao;

    private String tenantID;

    private String placa;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        MinMax<Date> mm = getMinMax(getArray(urlInfo, "data"), Date.class);
        if (mm.getMax() != null) {
            mm.setMax(DataUtil.adicionarDias(mm.getMax(), 1));
        }
        if (mm.getMax() != null || mm.getMin() != null) {
            data = mm;
        }

        mm = getMinMax(getArray(urlInfo, "dataVencimento"), Date.class);
        if (mm.getMax() != null && new Time(mm.getMax()).toMillis() == 0) {
            mm.setMax(DataUtil.adicionarDias(mm.getMax(), 1));
        }
        if (mm.getMax() != null || mm.getMin() != null) {
            dataVencimento = mm;
        }

        if (urlInfo.getFirst("tipo") != null) {
            tipo = EnumTipoVenda.retornaEnumSelecionado(urlInfo.getFirst("tipo"));
        }
        if (urlInfo.getFirst("cliente") != null) {
            cliente = new Cliente();
            cliente.setId(stringToId(urlInfo.getFirst("cliente")));
        }
        if (urlInfo.getFirst("funcionario") != null) {
            funcionario = new Funcionario();
            funcionario.setId(stringToId(urlInfo.getFirst("funcionario")));
        }
        if (urlInfo.getFirst("servico") != null) {
            servico = new Servico();
            servico.setId(stringToId(urlInfo.getFirst("servico")));
        }
        if (urlInfo.getFirst("valor") != null) {
            valor = Double.parseDouble(urlInfo.getFirst("valor"));
        }
        numero = urlInfo.getFirst("id");
        if (urlInfo.getFirst("formaPagamento") != null) {
            idFormaPagamento = new FormaPagamento();
            idFormaPagamento.setId(stringToId(urlInfo.getFirst("formaPagamento")));
        }
        if (urlInfo.getFirst("statusNegociacao") != null) {
            tipo = EnumTipoVenda.valueOf(urlInfo.getFirst("statusNegociacao"));
        }
        if (urlInfo.getFirst("parcelas") != null) {
            parcelas = Integer.parseInt(urlInfo.getFirst("parcelas"));
        }
        origem = In.fromList(getArray(urlInfo, "origem"));
        situacao = In.fromList(getArray(urlInfo, "situacao"));
        placa = urlInfo.getFirst("placa");
    }

}
