package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(dataType = "string", allowableValues = "VENDA, ORCAMENTO, PONTUACAO, ORCAMENTO_REJEITADO, ORCAMENTO_APROVADO")
    private EnumTipoVenda tipo;

    private ClienteDTO cliente;

    private String telefoneCliente;

    private UsuarioDTO usuarioVendedor;

    private Date dataVencimento;

    private String situacaoPagamento;

    private Date dataVenda;

    private Date dataCancelamento;

    private Double valor;

    private String situacaoSigla;

    private String situacaoDescricao;

    private Double desconto;

    private String condicaoPagamento;// Campo forma_pagamento (V, 1x, 2x, 3x, ...)

    private String observacao;

    private String observacaoCliente;

    private PlanoContaDTO planoConta;

    private Integer idConta;

    private ContaBancariaDTO contaBancaria;

    private CentroCustoDTO centroCusto;

    private String origem;

    private String idOrigem;

    private List<VendaProdutoRestDTO> produtos;

    private List<VendaServicoRestDTO> servicos;

    private List<VendaItemOrdemServicoRestDTO> itensOS;

    private List<VendaFormaPagamentoRestDTO> formasPagamento;

    private boolean emitiuNFe;

    private boolean emitiuNFSe;

    private VendaSeguradoraDTO vendaSeguradora;

    public String getHorarioInicial() {
        if (dataVencimento == null) {
            return "";
        }
        return new Time(dataVencimento).toString();
    }

    public String getHorarioFinal() {
        if (servicos == null || servicos.isEmpty() || dataVencimento == null) {
            return getHorarioInicial();
        }
        return servicos.stream()
                .map(VendaServicoRestDTO::getServico)
                .map(ServicoDTO::getTempoExecucao)
                .filter(servico -> servico != null && !servico.isEmpty())
                .map(Time::new)
                .reduce(new Time(dataVencimento), Time::add)
                .toString();
    }

}
